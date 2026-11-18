package com.bin.qionglouexplorer.ui.profile;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.bin.qionglouexplorer.BaseActivity;
import com.bin.qionglouexplorer.R;
import com.bin.qionglouexplorer.data.seed.SeedImporter;
import com.bin.qionglouexplorer.util.AppExecutors;
import com.bin.qionglouexplorer.util.UiUtils;

/** 设置：清除本地数据、关于。 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        applySystemBarInsets(R.id.settings_root, true, true);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.item_clear).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(R.string.settings_clear_data)
                .setMessage(R.string.settings_clear_data_desc)
                .setNegativeButton(R.string.common_cancel, null)
                .setPositiveButton(R.string.common_confirm, (dialog, which) -> resetData())
                .show());

        findViewById(R.id.item_about).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(R.string.settings_about)
                .setMessage(R.string.settings_about_content)
                .setPositiveButton(R.string.common_confirm, null)
                .show());
    }

    private void resetData() {
        AppExecutors.io().execute(() -> {
            SeedImporter.reset(this);
            AppExecutors.main(() -> UiUtils.toast(findViewById(R.id.settings_root),
                    getString(R.string.settings_cleared)));
        });
    }
}
