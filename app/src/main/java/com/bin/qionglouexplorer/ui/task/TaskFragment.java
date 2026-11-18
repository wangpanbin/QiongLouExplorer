package com.bin.qionglouexplorer.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.adapter.TaskAdapter;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.data.local.entity.ExplorationTaskEntity;
import com.bin.qionglouexplorer.data.repo.CultureRepository;
import com.bin.qionglouexplorer.data.repo.TaskRepository;
import com.bin.qionglouexplorer.ui.detail.DetailActivity;
import com.bin.qionglouexplorer.util.UiUtils;

import java.util.List;

/** 任务挑战：进度、列表、提交发奖。 */
public class TaskFragment extends Fragment implements TaskAdapter.TaskActionListener {

    private TaskRepository taskRepo;
    private CultureRepository cultureRepo;
    private TaskAdapter adapter;

    private TextView textProgress;
    private ProgressBar progressBar;
    private TextView textEmpty;

    private List<ExplorationTaskEntity> cachedTasks;
    private List<CulturePointEntity> cachedPoints;
    private List<Integer> cachedDoneIds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskRepo = new TaskRepository(requireContext());
        cultureRepo = new CultureRepository(requireContext());

        textProgress = view.findViewById(R.id.text_progress);
        progressBar = view.findViewById(R.id.progress_bar);
        textEmpty = view.findViewById(R.id.text_empty);

        adapter = new TaskAdapter(this);
        RecyclerView list = view.findViewById(R.id.list_tasks);
        list.setLayoutManager(new LinearLayoutManager(requireContext()));
        list.setAdapter(adapter);

        taskRepo.observeTasks().observe(getViewLifecycleOwner(), tasks -> {
            cachedTasks = tasks;
            render();
        });
        cultureRepo.observePoints().observe(getViewLifecycleOwner(), points -> {
            cachedPoints = points;
            render();
        });
        taskRepo.observeCheckedTaskIds().observe(getViewLifecycleOwner(), ids -> {
            cachedDoneIds = ids;
            render();
        });
    }

    private void render() {
        if (cachedTasks == null) {
            return;
        }
        adapter.submit(cachedTasks, cachedPoints, cachedDoneIds);
        int done = adapter.completedCount();
        int total = adapter.totalCount();
        textProgress.setText(getString(R.string.task_progress, done, total));
        progressBar.setProgress(total == 0 ? 0 : (int) (done * 100f / total));
        textEmpty.setVisibility(total == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onChallenge(ExplorationTaskEntity task) {
        Intent intent = new Intent(requireContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POINT_ID, task.pointId);
        startActivity(intent);
    }

    @Override
    public void onSubmit(ExplorationTaskEntity task) {
        taskRepo.submitTask(task, result -> {
            if (result.duplicated) {
                UiUtils.toast(requireView(), getString(R.string.task_submit_duplicated));
                return;
            }
            UiUtils.toast(requireView(), getString(R.string.task_submit_success,
                    result.rewardExp, result.rewardPoints));
        });
    }
}
