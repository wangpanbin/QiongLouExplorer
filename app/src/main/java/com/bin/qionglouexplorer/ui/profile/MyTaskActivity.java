package com.bin.qionglouexplorer.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bin.qionglouexplorer.BaseActivity;
import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.data.local.entity.ExplorationTaskEntity;
import com.bin.qionglouexplorer.data.repo.CultureRepository;
import com.bin.qionglouexplorer.data.repo.TaskRepository;
import com.bin.qionglouexplorer.ui.detail.DetailActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 我的任务：已完成 / 待完成分组。 */
public class MyTaskActivity extends BaseActivity {

    private LinearLayout groupPending;
    private LinearLayout groupDone;
    private TextView textEmpty;
    private TextView textGroupPending;
    private TextView textGroupDone;

    private Map<Integer, String> pointNames = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        applySystemBarInsets(R.id.mytask_root, true, true);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        groupPending = findViewById(R.id.group_pending);
        groupDone = findViewById(R.id.group_done);
        textEmpty = findViewById(R.id.text_empty);
        textGroupPending = findViewById(R.id.text_group_pending);
        textGroupDone = findViewById(R.id.text_group_done);

        TaskRepository taskRepo = new TaskRepository(this);
        CultureRepository cultureRepo = new CultureRepository(this);

        cultureRepo.observePoints().observe(this, points -> {
            pointNames.clear();
            if (points != null) {
                for (CulturePointEntity p : points) {
                    pointNames.put(p.id, p.name);
                }
            }
            render();
        });
        taskRepo.observeTasks().observe(this, tasks -> {
            this.tasks = tasks;
            render();
        });
        taskRepo.observeCheckedTaskIds().observe(this, ids -> {
            this.doneIds = ids;
            render();
        });
    }

    private List<ExplorationTaskEntity> tasks;
    private List<Integer> doneIds;

    private void render() {
        if (tasks == null) {
            return;
        }
        groupPending.removeAllViews();
        groupDone.removeAllViews();
        int pendingCount = 0;
        int doneCount = 0;
        LayoutInflater inflater = LayoutInflater.from(this);
        for (ExplorationTaskEntity task : tasks) {
            boolean done = doneIds != null && doneIds.contains(task.id);
            View row = inflater.inflate(R.layout.item_my_task, null, false);
            ((TextView) row.findViewById(R.id.text_title)).setText(task.title);
            String pointName = pointNames.get(task.pointId);
            ((TextView) row.findViewById(R.id.text_point)).setText(pointName == null ? "" : pointName);
            TextView status = row.findViewById(R.id.text_status);
            if (done) {
                status.setText(R.string.task_status_done);
                status.setBackgroundResource(R.drawable.bg_task_done);
                status.setTextColor(getColor(R.color.brand_success));
                groupDone.addView(row);
                doneCount++;
            } else {
                status.setText(R.string.task_status_available);
                status.setBackgroundResource(R.drawable.bg_task_open);
                status.setTextColor(getColor(R.color.brand_primary));
                groupPending.addView(row);
                pendingCount++;
            }
            row.setOnClickListener(v -> {
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_POINT_ID, task.pointId);
                startActivity(intent);
            });
        }
        textGroupPending.setVisibility(pendingCount == 0 ? View.GONE : View.VISIBLE);
        textGroupDone.setVisibility(doneCount == 0 ? View.GONE : View.VISIBLE);
        boolean empty = pendingCount == 0 && doneCount == 0;
        textEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
    }
}
