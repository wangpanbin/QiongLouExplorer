package com.bin.qionglouexplorer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.data.local.entity.ExplorationTaskEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 任务列表：进度、状态、立即挑战 / 提交。 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> {

    public interface TaskActionListener {
        void onChallenge(ExplorationTaskEntity task);

        void onSubmit(ExplorationTaskEntity task);
    }

    private final List<ExplorationTaskEntity> items = new ArrayList<>();
    private final Set<Integer> completedTaskIds = new HashSet<>();
    private final Map<Integer, String> pointNames = new HashMap<>();
    private final TaskActionListener listener;

    public TaskAdapter(TaskActionListener listener) {
        this.listener = listener;
    }

    public void submit(List<ExplorationTaskEntity> tasks,
                       List<CulturePointEntity> points,
                       List<Integer> completedIds) {
        items.clear();
        if (tasks != null) {
            items.addAll(tasks);
        }
        pointNames.clear();
        if (points != null) {
            for (CulturePointEntity p : points) {
                pointNames.put(p.id, p.name);
            }
        }
        completedTaskIds.clear();
        if (completedIds != null) {
            completedTaskIds.addAll(completedIds);
        }
        notifyDataSetChanged();
    }

    public int completedCount() {
        int count = 0;
        for (ExplorationTaskEntity t : items) {
            if (completedTaskIds.contains(t.id)) {
                count++;
            }
        }
        return count;
    }

    public int totalCount() {
        return items.size();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
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
        final TextView title;
        final TextView desc;
        final TextView status;
        final TextView point;
        final TextView reward;
        final TextView action;

        Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            desc = itemView.findViewById(R.id.text_desc);
            status = itemView.findViewById(R.id.text_status);
            point = itemView.findViewById(R.id.text_point);
            reward = itemView.findViewById(R.id.text_reward);
            action = itemView.findViewById(R.id.btn_action);
        }

        void bind(ExplorationTaskEntity task) {
            Context ctx = itemView.getContext();
            boolean done = completedTaskIds.contains(task.id);

            title.setText(task.title);
            desc.setText(task.description);
            reward.setText(ctx.getString(R.string.task_reward, task.rewardExp, task.rewardPoints));

            String name = pointNames.get(task.pointId);
            point.setText(name == null ? "" : "点位 · " + name);

            if (done) {
                status.setText(R.string.task_status_done);
                status.setBackgroundResource(R.drawable.bg_task_done);
                status.setTextColor(ctx.getColor(R.color.brand_success));
                action.setText(R.string.task_status_done);
                action.setEnabled(false);
                action.setAlpha(0.5f);
                action.setOnClickListener(null);
            } else {
                status.setText(R.string.task_status_available);
                status.setBackgroundResource(R.drawable.bg_task_open);
                status.setTextColor(ctx.getColor(R.color.brand_primary));
                action.setText(R.string.task_action_submit);
                action.setEnabled(true);
                action.setAlpha(1f);
                action.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onSubmit(task);
                    }
                });
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onChallenge(task);
                }
            });
        }
    }
}
