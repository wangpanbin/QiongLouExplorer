package com.bin.qionglouexplorer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.data.local.entity.MessageEntity;

import java.util.ArrayList;
import java.util.List;

/** 消息列表：系统通知 / 好友消息两段式过滤。 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Holder> {

    private final List<MessageEntity> all = new ArrayList<>();
    private final List<MessageEntity> shown = new ArrayList<>();
    private String filter = MessageEntity.TYPE_SYSTEM;

    public void submit(List<MessageEntity> messages) {
        all.clear();
        if (messages != null) {
            all.addAll(messages);
        }
        applyFilter();
    }

    public void setFilter(String type) {
        this.filter = type;
        applyFilter();
    }

    private void applyFilter() {
        shown.clear();
        for (MessageEntity m : all) {
            if (filter.equals(m.type)) {
                shown.add(m);
            }
        }
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return shown.isEmpty();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(shown.get(position));
    }

    @Override
    public int getItemCount() {
        return shown.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView icon;
        final TextView title;
        final TextView time;
        final TextView content;
        final View dot;

        Holder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.img_icon);
            title = itemView.findViewById(R.id.text_title);
            time = itemView.findViewById(R.id.text_time);
            content = itemView.findViewById(R.id.text_content);
            dot = itemView.findViewById(R.id.dot_unread);
        }

        void bind(MessageEntity message) {
            boolean friend = MessageEntity.TYPE_FRIEND.equals(message.type);
            icon.setImageResource(friend ? R.drawable.ic_tab_profile : R.drawable.ic_message);
            title.setText(message.title);
            time.setText(message.timeLabel);
            content.setText(message.content);
            dot.setVisibility(message.isRead == 0 ? View.VISIBLE : View.GONE);
        }
    }
}
