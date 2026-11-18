package com.bin.qionglouexplorer.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bin.qionglouexplorer.BaseActivity;
import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.data.repo.CultureRepository;
import com.bin.qionglouexplorer.data.repo.TaskRepository;
import com.bin.qionglouexplorer.util.GeoUtils;
import com.bin.qionglouexplorer.util.UiUtils;

/** 离线使用：已缓存点位数、待同步打卡数、手动同步。 */
public class OfflineActivity extends BaseActivity {

    private TextView textCached;
    private TextView textPending;
    private TextView btnSync;
    private TaskRepository taskRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        applySystemBarInsets(R.id.offline_root, true, true);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        textCached = findViewById(R.id.text_cached);
        textPending = findViewById(R.id.text_pending);
        btnSync = findViewById(R.id.btn_sync);

        CultureRepository cultureRepo = new CultureRepository(this);
        taskRepo = new TaskRepository(this);

        findViewById(R.id.banner_network)
                .setVisibility(GeoUtils.isOnline(this) ? View.GONE : View.VISIBLE);

        cultureRepo.observeCachedCount().observe(this, count ->
                textCached.setText(String.valueOf(count == null ? 0 : count)));
        taskRepo.observePendingSyncCount().observe(this, count ->
                textPending.setText(String.valueOf(count == null ? 0 : count)));

        btnSync.setOnClickListener(v -> syncNow());
    }

    private void syncNow() {
        btnSync.setEnabled(false);
        btnSync.setText(R.string.offline_syncing);
        taskRepo.syncPending(count -> {
            btnSync.setEnabled(true);
            btnSync.setText(R.string.offline_sync_now);
            if (count == 0) {
                UiUtils.toast(findViewById(R.id.offline_root),
                        getString(R.string.offline_sync_empty));
            } else {
                UiUtils.toast(findViewById(R.id.offline_root),
                        getString(R.string.offline_sync_success, count));
            }
        });
    }
}
