package com.bin.qionglouexplorer.ui.map;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.data.repo.CultureRepository;
import com.bin.qionglouexplorer.ui.detail.DetailActivity;
import com.bin.qionglouexplorer.util.GeoUtils;
import com.bin.qionglouexplorer.util.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** 文化地图：自绘街区图 + 分类筛选 + 底部点位卡 + 系统导航。 */
public class MapFragment extends Fragment {

    private static final String[] FILTER_CODES =
            {null, "architecture", "history", "folk", "food"};

    private CultureRepository cultureRepo;
    private MapCanvasView canvas;
    private View cardPoint;
    private LinearLayout groupFilters;

    private final List<CulturePointEntity> allPoints = new ArrayList<>();
    private String activeFilter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cultureRepo = new CultureRepository(requireContext());
        canvas = view.findViewById(R.id.map_canvas);
        cardPoint = view.findViewById(R.id.card_point);
        groupFilters = view.findViewById(R.id.group_filters);

        buildFilters();
        canvas.setOnPointSelectedListener(this::showCard);

        view.findViewById(R.id.btn_close_card).setOnClickListener(v -> {
            cardPoint.setVisibility(View.GONE);
            canvas.setSelected(null);
        });
        view.findViewById(R.id.btn_list_mode).setOnClickListener(v -> {
            if (getActivity() instanceof com.bin.qionglouexplorer.MainActivity) {
                ((com.bin.qionglouexplorer.MainActivity) getActivity())
                        .switchTab(R.id.nav_home);
            }
        });

        cultureRepo.observePoints().observe(getViewLifecycleOwner(), points -> {
            allPoints.clear();
            if (points != null) {
                allPoints.addAll(points);
            }
            applyFilter();
        });
    }

    private void buildFilters() {
        String[] labels = getResources().getStringArray(R.array.map_filter_labels);
        groupFilters.removeAllViews();
        for (int i = 0; i < labels.length; i++) {
            final String code = i < FILTER_CODES.length ? FILTER_CODES[i] : null;
            TextView chip = (TextView) LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_filter_chip, groupFilters, false);
            chip.setText(labels[i]);
            chip.setSelected(code == null);
            chip.setOnClickListener(v -> {
                activeFilter = code;
                for (int c = 0; c < groupFilters.getChildCount(); c++) {
                    View child = groupFilters.getChildAt(c);
                    child.setSelected(child == v);
                }
                applyFilter();
                cardPoint.setVisibility(View.GONE);
                canvas.setSelected(null);
            });
            groupFilters.addView(chip);
        }
    }

    private void applyFilter() {
        List<CulturePointEntity> filtered = new ArrayList<>();
        for (CulturePointEntity p : allPoints) {
            if (activeFilter == null || activeFilter.equals(p.category)) {
                filtered.add(p);
            }
        }
        canvas.setPoints(filtered);
    }

    private void showCard(@Nullable CulturePointEntity point) {
        if (point == null) {
            cardPoint.setVisibility(View.GONE);
            return;
        }
        cardPoint.setVisibility(View.VISIBLE);
        ImageView cover = cardPoint.findViewById(R.id.img_cover);
        cover.setBackgroundResource(
                UiUtils.drawableId(requireContext(), point.coverRes, R.drawable.bg_cover_1));

        TextView name = cardPoint.findViewById(R.id.text_name);
        name.setText(point.name);

        TextView meta = cardPoint.findViewById(R.id.text_meta);
        double meters = GeoUtils.distanceFromAnchor(point.latitude, point.longitude);
        meta.setText(String.format(Locale.getDefault(), "%s · %s · %.1f 分",
                UiUtils.categoryLabel(requireContext(), point.category),
                GeoUtils.formatDistance(requireContext(), meters), point.rating));

        cardPoint.findViewById(R.id.btn_detail).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_POINT_ID, point.id);
            startActivity(intent);
        });
        cardPoint.findViewById(R.id.btn_navigate).setOnClickListener(v -> navigateTo(point));
    }

    /** 唤起系统地图做真实导航：geo:lat,lng?q=lat,lng(名称)。 */
    private void navigateTo(CulturePointEntity point) {
        String query = point.latitude + "," + point.longitude;
        String label = Uri.encode(point.name);
        Uri uri = Uri.parse("geo:" + query + "?q=" + query + "(" + label + ")");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // 极简系统无地图应用时，退回网页地图
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://uri.amap.com/marker?position=" + point.longitude
                                + "," + point.latitude + "&name=" + label)));
            } catch (ActivityNotFoundException ignored) {
                UiUtils.toast(requireView(), getString(R.string.map_no_navigation_app));
            }
        }
    }
}
