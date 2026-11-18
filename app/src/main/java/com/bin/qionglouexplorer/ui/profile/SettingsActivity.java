package com.bin.qionglouexplorer.ui.profile;

import android.os.Bundle;

import com.bin.qionglouexplorer.BaseActivity;
import com.bin.qionglouexplorer.R;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_placeholder);
        applySystemBarInsets(android.R.id.content, true, true);
    }
}
