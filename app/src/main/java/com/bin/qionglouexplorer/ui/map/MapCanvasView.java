package com.bin.qionglouexplorer.ui.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.util.GeoUtils;
import com.bin.qionglouexplorer.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自绘街区地图。
 *
 * 为什么不用高德 SDK：V0.1 的底线是零外部依赖（无 Key、无网络、可离线）。
 * 这里只承担「发现与定位感知」职责：底图给出街区肌理，点位给出坐标关系，
 * 真实路线规划交给系统地图 App。V0.2 若接入高德，替换本 View 即可，
 * 对外暴露的接口（setPoints / setSelected / OnPointSelectedListener）保持不变。
 */
public class MapCanvasView extends View {

    public interface OnPointSelectedListener {
        void onPointSelected(@Nullable CulturePointEntity point);
    }

    /** 覆盖的点位地理范围（略大于实际点位包围盒，留出边距）。 */
    private static final double MIN_LAT = 20.0400;
    private static final double MAX_LAT = 20.0490;
    private static final double MIN_LNG = 110.3370;
    private static final double MAX_LNG = 110.3495;

    private final Paint blockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint blockAltPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint waterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint roadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint roadMinorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint markerSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint markerStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint labelBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint userPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint userHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint areaLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final List<CulturePointEntity> points = new ArrayList<>();
    private final List<PointF> screenPositions = new ArrayList<>();
    private CulturePointEntity selected;
    private OnPointSelectedListener listener;

    private final Path roadPath = new Path();
    private final RectF blockRect = new RectF();

    public MapCanvasView(Context context) {
        this(context, null);
    }

    public MapCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        blockPaint.setColor(getContext().getColor(R.color.map_block));
        blockAltPaint.setColor(getContext().getColor(R.color.map_block_alt));
        waterPaint.setColor(getContext().getColor(R.color.map_water));
        roadPaint.setColor(getContext().getColor(R.color.map_road));
        roadMinorPaint.setColor(getContext().getColor(R.color.map_road_secondary));
        markerPaint.setColor(getContext().getColor(R.color.brand_primary));
        markerSelectedPaint.setColor(getContext().getColor(R.color.brand_accent));
        markerStrokePaint.setColor(Color.WHITE);
        markerStrokePaint.setStyle(Paint.Style.STROKE);
        markerStrokePaint.setStrokeWidth(UiUtils.dp(getContext(), 2));
        labelPaint.setColor(getContext().getColor(R.color.text_primary));
        labelPaint.setTextSize(UiUtils.dp(getContext(), 11));
        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelBgPaint.setColor(Color.WHITE);
        userPaint.setColor(getContext().getColor(R.color.brand_info));
        userHaloPaint.setColor(UiUtils.withAlpha(
                getContext().getColor(R.color.brand_info), 0x33));
        pathPaint.setColor(getContext().getColor(R.color.brand_primary));
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(UiUtils.dp(getContext(), 3));
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setPathEffect(new android.graphics.DashPathEffect(
                new float[]{UiUtils.dp(getContext(), 8), UiUtils.dp(getContext(), 6)}, 0));
        areaLabelPaint.setColor(getContext().getColor(R.color.map_label));
        areaLabelPaint.setTextSize(UiUtils.dp(getContext(), 12));
        areaLabelPaint.setLetterSpacing(0.08f);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public void setOnPointSelectedListener(OnPointSelectedListener listener) {
        this.listener = listener;
    }

    public void setPoints(List<CulturePointEntity> data) {
        points.clear();
        if (data != null) {
            points.addAll(data);
        }
        screenPositions.clear();
        for (int i = 0; i < points.size(); i++) {
            screenPositions.add(new PointF());
        }
        projectAll();
        invalidate();
    }

    public void setSelected(@Nullable CulturePointEntity point) {
        this.selected = point;
        invalidate();
    }

    @Nullable
    public CulturePointEntity getSelected() {
        return selected;
    }

    private void projectAll() {
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) {
            return;
        }
        float padX = UiUtils.dp(getContext(), 36);
        float padTop = UiUtils.dp(getContext(), 60);
        float padBottom = UiUtils.dp(getContext(), 96);
        float usableW = w - padX * 2;
        float usableH = h - padTop - padBottom;
        for (int i = 0; i < points.size(); i++) {
            CulturePointEntity p = points.get(i);
            float x = padX + (float) ((p.longitude - MIN_LNG) / (MAX_LNG - MIN_LNG) * usableW);
            float y = padTop + (float) ((MAX_LAT - p.latitude) / (MAX_LAT - MIN_LAT) * usableH);
            screenPositions.get(i).set(x, y);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        projectAll();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        drawBaseMap(canvas);
        drawPath(canvas);
        drawUserLocation(canvas);
        drawMarkers(canvas);
    }

    /** 底图：水系 + 街区色块 + 主次路网。纯示意，不追求地理精度。 */
    private void drawBaseMap(Canvas canvas) {
        canvas.drawColor(getContext().getColor(R.color.map_land));

        float w = getWidth();
        float h = getHeight();

        // 北侧海甸溪水系
        RectF water = new RectF(-20, -20, w + 20, h * 0.085f);
        canvas.drawRoundRect(water, 12, 12, waterPaint);

        // 街区色块（网格状，模拟骑楼密集肌理）
        float blockTop = h * 0.14f;
        float blockBottom = h * 0.90f;
        float blockW = w / 5.4f;
        float blockH = (blockBottom - blockTop) / 6.2f;
        float gap = UiUtils.dp(getContext(), 7);
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                float left = col * blockW + gap;
                float top = blockTop + row * blockH + gap;
                blockRect.set(left, top, left + blockW - gap * 2, top + blockH - gap * 2);
                canvas.drawRoundRect(blockRect, 6, 6, (row + col) % 2 == 0
                        ? blockPaint : blockAltPaint);
            }
        }

        // 主路：三条横向 + 两条纵向，白色宽线
        float mainRoadW = UiUtils.dp(getContext(), 12);
        float minorRoadW = UiUtils.dp(getContext(), 6);
        roadPaint.setStrokeWidth(mainRoadW);
        roadMinorPaint.setStrokeWidth(minorRoadW);

        float[] horizontalY = {blockTop + blockH * 1.6f, blockTop + blockH * 3.2f,
                blockTop + blockH * 4.8f};
        for (int i = 0; i < horizontalY.length; i++) {
            canvas.drawLine(0, horizontalY[i], w, horizontalY[i],
                    i == 1 ? roadPaint : roadMinorPaint);
        }
        float[] verticalX = {blockW * 1.5f, blockW * 3.5f};
        for (int i = 0; i < verticalX.length; i++) {
            canvas.drawLine(verticalX[i], blockTop - UiUtils.dp(getContext(), 10),
                    verticalX[i], h, i == 0 ? roadPaint : roadMinorPaint);
        }

        // 街区名称
        canvas.drawText("海口骑楼历史文化街区", w / 2f, blockTop - UiUtils.dp(getContext(), 18),
                areaLabelPaint);
        canvas.drawText("海甸溪", w / 2f, h * 0.058f, areaLabelPaint);
    }

    private void drawPath(Canvas canvas) {
        if (selected == null) {
            return;
        }
        PointF user = userPoint();
        int index = points.indexOf(selected);
        if (index < 0 || index >= screenPositions.size()) {
            return;
        }
        PointF target = screenPositions.get(index);
        roadPath.reset();
        roadPath.moveTo(user.x, user.y);
        // 直角折线，模拟「沿街走」的路由观感
        float midY = (user.y + target.y) / 2f;
        roadPath.lineTo(user.x, midY);
        roadPath.lineTo(target.x, midY);
        roadPath.lineTo(target.x, target.y);
        canvas.drawPath(roadPath, pathPaint);
    }

    private PointF userPoint() {
        float padX = UiUtils.dp(getContext(), 36);
        float usableW = getWidth() - padX * 2;
        float padTop = UiUtils.dp(getContext(), 60);
        float usableH = getHeight() - padTop - UiUtils.dp(getContext(), 96);
        float x = padX + (float) ((GeoUtils.ANCHOR_LONGITUDE - MIN_LNG) / (MAX_LNG - MIN_LNG)
                * usableW);
        float y = padTop + (float) ((MAX_LAT - GeoUtils.ANCHOR_LATITUDE) / (MAX_LAT - MIN_LAT)
                * usableH);
        return new PointF(x, y);
    }

    private void drawUserLocation(Canvas canvas) {
        PointF user = userPoint();
        canvas.drawCircle(user.x, user.y, UiUtils.dp(getContext(), 16), userHaloPaint);
        canvas.drawCircle(user.x, user.y, UiUtils.dp(getContext(), 7), userPaint);
        canvas.drawCircle(user.x, user.y, UiUtils.dp(getContext(), 7), markerStrokePaint);
    }

    private void drawMarkers(Canvas canvas) {
        float r = UiUtils.dp(getContext(), 7);
        float rSelected = UiUtils.dp(getContext(), 11);
        for (int i = 0; i < points.size(); i++) {
            CulturePointEntity p = points.get(i);
            PointF pos = screenPositions.get(i);
            boolean isSelected = selected != null && selected.id == p.id;
            float radius = isSelected ? rSelected : r;
            canvas.drawCircle(pos.x, pos.y, radius, isSelected ? markerSelectedPaint : markerPaint);
            canvas.drawCircle(pos.x, pos.y, radius, markerStrokePaint);

            if (isSelected || points.size() <= 10) {
                drawLabel(canvas, p.name, pos.x, pos.y - radius - UiUtils.dp(getContext(), 10));
            }
        }
    }

    private void drawLabel(Canvas canvas, String text, float cx, float cy) {
        float textWidth = labelPaint.measureText(text);
        float padH = UiUtils.dp(getContext(), 6);
        float padV = UiUtils.dp(getContext(), 3);
        RectF bg = new RectF(cx - textWidth / 2 - padH, cy - UiUtils.dp(getContext(), 11) - padV,
                cx + textWidth / 2 + padH, cy + UiUtils.dp(getContext(), 3) + padV);
        canvas.drawRoundRect(bg, UiUtils.dp(getContext(), 6), UiUtils.dp(getContext(), 6),
                labelBgPaint);
        canvas.drawText(text, cx, cy, labelPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            performClick();
            int hit = hitTest(event.getX(), event.getY());
            if (hit >= 0) {
                selected = points.get(hit);
                invalidate();
                if (listener != null) {
                    listener.onPointSelected(selected);
                }
            }
        }
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    /** 命中测试：容错半径 48dp，避免小屏上点不准。 */
    private int hitTest(float x, float y) {
        float tolerance = UiUtils.dp(getContext(), 48);
        int best = -1;
        double bestDistance = Double.MAX_VALUE;
        for (int i = 0; i < screenPositions.size(); i++) {
            PointF pos = screenPositions.get(i);
            double d = Math.hypot(pos.x - x, pos.y - y);
            if (d <= tolerance && d < bestDistance) {
                best = i;
                bestDistance = d;
            }
        }
        return best;
    }
}
