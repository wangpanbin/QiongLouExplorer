package com.bin.qionglouexplorer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.util.GeoUtils;
import com.bin.qionglouexplorer.util.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** 纵向点位条目：附近推荐、搜索结果、收藏列表复用。 */
public class PointAdapter extends RecyclerView.Adapter<PointAdapter.PointHolder> {

    public interface OnPointClickListener {
        void onPointClick(CulturePointEntity point);
    }

    private final List<CulturePointEntity> items = new ArrayList<>();
    private final OnPointClickListener listener;
    private final int fallbackCover;

    public PointAdapter(OnPointClickListener listener) {
        this.listener = listener;
        this.fallbackCover = R.drawable.bg_cover_1;
    }

    public void submit(List<CulturePointEntity> points) {
        items.clear();
        if (points != null) {
            items.addAll(points);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_point, parent, false);
        return new PointHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PointHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class PointHolder extends RecyclerView.ViewHolder {
        final ImageView cover;
        final TextView name;
        final TextView category;
        final TextView summary;
        final TextView rating;
        final TextView distance;

        PointHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.img_cover);
            name = itemView.findViewById(R.id.text_name);
            category = itemView.findViewById(R.id.text_category);
            summary = itemView.findViewById(R.id.text_summary);
            rating = itemView.findViewById(R.id.text_rating);
            distance = itemView.findViewById(R.id.text_distance);
        }

        void bind(CulturePointEntity point) {
            Context ctx = itemView.getContext();
            cover.setBackgroundResource(UiUtils.drawableId(ctx, point.coverRes, fallbackCover));
            name.setText(point.name);
            category.setText(UiUtils.categoryLabel(ctx, point.category));
            UiUtils.tintTag(category, UiUtils.categoryColor(ctx, point.category));
            summary.setText(point.summary);
            rating.setText(String.format(Locale.getDefault(), "%.1f", point.rating));
            double meters = GeoUtils.distanceFromAnchor(point.latitude, point.longitude);
            distance.setText(GeoUtils.formatDistance(ctx, meters));
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPointClick(point);
                }
            });
        }
    }
}
