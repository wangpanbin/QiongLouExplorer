package com.bin.qionglouexplorer.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

/** 视图与资源的小工具：dp 换算、drawable 名解析、Snackbar 提示、分类配色。 */
public final class UiUtils {

    private UiUtils() {
    }

    public static int dp(Context context, float value) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics()));
    }

    /** 按 drawable 资源名取 id，取不到时回退到默认封面。 */
    public static int drawableId(Context context, String name, int fallbackId) {
        if (name == null || name.isEmpty()) {
            return fallbackId;
        }
        Resources res = context.getResources();
        int id = res.getIdentifier(name, "drawable", context.getPackageName());
        return id == 0 ? fallbackId : id;
    }

    public static void toast(View anchor, String message) {
        if (anchor == null || message == null) {
            return;
        }
        Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT).show();
    }

    /** 分类中文名。 */
    public static String categoryLabel(Context context, String category) {
        int resId;
        if ("history".equals(category)) {
            resId = com.bin.qionglouexplorer.R.string.category_history;
        } else if ("folk".equals(category)) {
            resId = com.bin.qionglouexplorer.R.string.category_folk;
        } else if ("food".equals(category)) {
            resId = com.bin.qionglouexplorer.R.string.category_food;
        } else {
            resId = com.bin.qionglouexplorer.R.string.category_architecture;
        }
        return context.getString(resId);
    }

    /** 分类主题色，用于标签底纹。 */
    public static int categoryColor(Context context, String category) {
        int resId;
        if ("history".equals(category)) {
            resId = com.bin.qionglouexplorer.R.color.brand_info;
        } else if ("folk".equals(category)) {
            resId = com.bin.qionglouexplorer.R.color.brand_warning;
        } else if ("food".equals(category)) {
            resId = com.bin.qionglouexplorer.R.color.brand_accent;
        } else {
            resId = com.bin.qionglouexplorer.R.color.brand_primary;
        }
        return context.getColor(resId);
    }

    /** 给 TextView 施加圆角底纹（标签用）。 */
    public static void tintTag(TextView view, int color) {
        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(dp(view.getContext(), 8));
        bg.setColor(withAlpha(color, 0x1F));
        view.setBackground(bg);
        view.setTextColor(color);
    }

    public static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    /** 访问量格式化：1.2 万。 */
    public static String formatVisits(int count) {
        if (count >= 10000) {
            return String.format(java.util.Locale.getDefault(), "%.1f 万", count / 10000f);
        }
        return String.valueOf(count);
    }
}
