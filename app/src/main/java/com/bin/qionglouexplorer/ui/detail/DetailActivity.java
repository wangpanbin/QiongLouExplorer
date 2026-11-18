package com.bin.qionglouexplorer.ui.detail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.qionglouexplorer.BaseActivity;
import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.adapter.PointHotAdapter;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.data.local.entity.ExplorationTaskEntity;
import com.bin.qionglouexplorer.data.repo.CultureRepository;
import com.bin.qionglouexplorer.data.repo.TaskRepository;
import com.bin.qionglouexplorer.ui.scan.ScanActivity;
import com.bin.qionglouexplorer.util.GeoUtils;
import com.bin.qionglouexplorer.util.UiUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

/** 景点详情：封面、评分、简介/任务/评论三 Tab、收藏、扫码打卡、关联点位。 */
public class DetailActivity extends BaseActivity {

    public static final String EXTRA_POINT_ID = "extra_point_id";

    private CultureRepository cultureRepo;
    private TaskRepository taskRepo;

    private int pointId;
    @Nullable
    private CulturePointEntity point;
    @Nullable
    private ExplorationTaskEntity task;
    private boolean favorite = false;
    private boolean cachedRequested = false;

    private TabLayout tabs;
    private LinearLayout panelIntro;
    private LinearLayout panelTask;
    private LinearLayout panelComment;
    private ImageView btnFavorite;
    private PointHotAdapter relatedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        applySystemBarInsets(R.id.detail_root, false, true);

        pointId = getIntent().getIntExtra(EXTRA_POINT_ID, 0);
        if (pointId == 0) {
            finish();
            return;
        }
        cultureRepo = new CultureRepository(this);
        taskRepo = new TaskRepository(this);

        bindViews();
        bindTabs();
        bindActions();
        observeData();
    }

    private void bindViews() {
        tabs = findViewById(R.id.tabs);
        panelIntro = findViewById(R.id.panel_intro);
        panelTask = findViewById(R.id.panel_task);
        panelComment = findViewById(R.id.panel_comment);
        btnFavorite = findViewById(R.id.btn_favorite);

        relatedAdapter = new PointHotAdapter(p -> openDetail(p.id));
        RecyclerView listRelated = findViewById(R.id.list_related);
        listRelated.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        listRelated.setAdapter(relatedAdapter);

        // 离线提示：详情页始终从 Room 读取，网络不可用时给出显式说明
        findViewById(R.id.banner_offline)
                .setVisibility(GeoUtils.isOnline(this) ? View.GONE : View.VISIBLE);
    }

    private void bindTabs() {
        tabs.addTab(tabs.newTab().setText(R.string.detail_tab_intro));
        tabs.addTab(tabs.newTab().setText(R.string.detail_tab_task));
        tabs.addTab(tabs.newTab().setText(R.string.detail_tab_comment));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                panelIntro.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
                panelTask.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
                panelComment.setVisibility(index == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void bindActions() {
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        btnFavorite.setOnClickListener(v -> toggleFavorite());
        findViewById(R.id.btn_scan).setOnClickListener(v -> {
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra(ScanActivity.EXTRA_POINT_ID, pointId);
            startActivity(intent);
        });
        findViewById(R.id.btn_navigate).setOnClickListener(v -> navigateToPoint());
        findViewById(R.id.btn_submit_task).setOnClickListener(v -> submitTask());
    }

    private void observeData() {
        cultureRepo.observePoint(pointId).observe(this, p -> {
            if (p == null) {
                return;
            }
            point = p;
            render(p);
        });

        cultureRepo.observeFavoriteExists(pointId).observe(this, exists -> {
            favorite = exists != null && exists;
            renderFavorite();
        });

        taskRepo.observeTasks().observe(this, tasks -> {
            task = null;
            if (tasks != null) {
                for (ExplorationTaskEntity t : tasks) {
                    if (t.pointId == pointId) {
                        task = t;
                        break;
                    }
                }
            }
            renderTask();
        });

        taskRepo.observeCheckedTaskIds().observe(this, ids -> {
            boolean done = false;
            if (ids != null && task != null) {
                done = ids.contains(task.id);
            }
            renderTaskDone(done);
        });
    }

    private void render(CulturePointEntity p) {
        // 详情页读取即预缓存：离线时同一页面仍可直接打开
        if (!p.isCached() && !cachedRequested) {
            cachedRequested = true;
            cultureRepo.cachePoint(p.id);
        }
        ImageView cover = findViewById(R.id.img_cover);
        cover.setBackgroundResource(UiUtils.drawableId(this, p.coverRes, R.drawable.bg_cover_1));

        ((TextView) findViewById(R.id.text_name)).setText(p.name);
        ((TextView) findViewById(R.id.text_rating)).setText(
                String.format(Locale.getDefault(), "%.1f", p.rating));
        ((TextView) findViewById(R.id.text_visits)).setText(
                getString(R.string.detail_visits, UiUtils.formatVisits(p.visitCount)));
        ((TextView) findViewById(R.id.text_summary)).setText(p.summary);

        TextView category = findViewById(R.id.text_category);
        category.setText(UiUtils.categoryLabel(this, p.category));
        UiUtils.tintTag(category, UiUtils.categoryColor(this, p.category));

        ((TextView) findViewById(R.id.text_era)).setText(p.era);
        ((TextView) findViewById(R.id.text_style)).setText(p.style);
        ((TextView) findViewById(R.id.text_address)).setText(p.address);
        ((TextView) findViewById(R.id.text_description)).setText(p.description);
        ((TextView) findViewById(R.id.text_source_note)).setText(
                getString(R.string.detail_source_note, p.sourceNote));

        findViewById(R.id.text_cached_tag)
                .setVisibility(p.isCached() ? View.VISIBLE : View.GONE);

        cultureRepo.observeRelated(p.category, p.id)
                .observe(this, relatedAdapter::submit);
    }

    private void renderFavorite() {
        btnFavorite.setImageResource(favorite
                ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_outline);
        btnFavorite.setContentDescription(getString(
                favorite ? R.string.detail_favorited : R.string.detail_favorite));
    }

    private void renderTask() {
        TextView title = findViewById(R.id.text_task_title);
        TextView desc = findViewById(R.id.text_task_desc);
        TextView reward = findViewById(R.id.text_task_reward);
        TextView submit = findViewById(R.id.btn_submit_task);
        TextView empty = findViewById(R.id.text_task_empty);

        if (task == null) {
            title.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
            reward.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            return;
        }
        title.setVisibility(View.VISIBLE);
        desc.setVisibility(View.VISIBLE);
        reward.setVisibility(View.VISIBLE);
        submit.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);

        title.setText(task.title);
        desc.setText(task.description);
        reward.setText(getString(R.string.task_reward, task.rewardExp, task.rewardPoints));
    }

    private void renderTaskDone(boolean done) {
        TextView submit = findViewById(R.id.btn_submit_task);
        if (done) {
            submit.setText(R.string.task_status_done);
            submit.setEnabled(false);
            submit.setAlpha(0.55f);
        } else {
            submit.setText(R.string.task_action_submit);
            submit.setEnabled(true);
            submit.setAlpha(1f);
        }
    }

    private void toggleFavorite() {
        cultureRepo.toggleFavorite(pointId, nowFavorite ->
                UiUtils.toast(findViewById(R.id.detail_root), getString(
                        nowFavorite ? R.string.detail_favorited : R.string.detail_favorite)));
    }

    private void submitTask() {
        if (task == null) {
            return;
        }
        taskRepo.submitTask(task, result -> {
            if (result.duplicated) {
                UiUtils.toast(findViewById(R.id.detail_root),
                        getString(R.string.task_submit_duplicated));
                return;
            }
            UiUtils.toast(findViewById(R.id.detail_root),
                    getString(R.string.task_submit_success, result.rewardExp, result.rewardPoints));
            renderTaskDone(true);
        });
    }

    private void navigateToPoint() {
        if (point == null) {
            return;
        }
        String query = point.latitude + "," + point.longitude;
        Uri uri = Uri.parse("geo:" + query + "?q=" + query
                + "(" + Uri.encode(point.name) + ")");
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException e) {
            UiUtils.toast(findViewById(R.id.detail_root),
                    getString(R.string.map_no_navigation_app));
        }
    }

    private void openDetail(int id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_POINT_ID, id);
        startActivity(intent);
        finish();
    }
}
