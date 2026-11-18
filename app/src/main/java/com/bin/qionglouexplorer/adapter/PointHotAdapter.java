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
import com.bin.qionglouexplorer.util.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** 首页热门推荐横向卡片。 */
public class PointHotAdapter extends RecyclerView.Adapter<PointHotAdapter.Holder> {

    public interface OnPointClickListener {
        void onPointClick(CulturePointEntity point);
    }

    private final List<CulturePointEntity> items = new ArrayList<>();
    private final OnPointClickListener listener;

    public PointHotAdapter(OnPointClickListener listener) {
        this.listener = listener;
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
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_point_hot, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        final ImageView cover;
        final TextView name;
        final TextView meta;

        Holder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.img_cover);
            name = itemView.findViewById(R.id.text_name);
            meta = itemView.findViewById(R.id.text_meta);
        }

        void bind(CulturePointEntity point) {
            Context ctx = itemView.getContext();
            cover.setBackgroundResource(
                    UiUtils.drawableId(ctx, point.coverRes, R.drawable.bg_cover_1));
            name.setText(point.name);
            meta.setText(String.format(Locale.getDefault(), "%s · %.1f",
                    UiUtils.categoryLabel(ctx, point.category), point.rating));
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPointClick(point);
                }
            });
        }
    }
}
