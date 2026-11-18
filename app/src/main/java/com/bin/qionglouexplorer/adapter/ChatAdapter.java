package com.bin.qionglouexplorer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.qionglouexplorer.R;

import java.util.ArrayList;
import java.util.List;

/** 助手对话气泡列表：区分用户与机器人两种布局。 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    private static final int TYPE_USER = 1;
    private static final int TYPE_BOT = 2;

    public static class ChatMessage {
        public final String content;
        public final boolean fromUser;

        public ChatMessage(String content, boolean fromUser) {
            this.content = content;
            this.fromUser = fromUser;
        }
    }

    private final List<ChatMessage> items = new ArrayList<>();

    public void add(ChatMessage message) {
        items.add(message);
        notifyItemInserted(items.size() - 1);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).fromUser ? TYPE_USER : TYPE_BOT;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == TYPE_USER
                ? R.layout.item_chat_user : R.layout.item_chat_assistant;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.content.setText(items.get(position).content);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView content;

        Holder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.text_content);
        }
    }
}
