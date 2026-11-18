package com.bin.qionglouexplorer.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bin.qionglouexplorer.BaseActivity;
import com.bin.qionglouexplorer.MainActivity;
import com.bin.qionglouexplorer.R;

/** 启动页：品牌视觉 + 开始探索。 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        applySystemBarInsets(R.id.splash_root, true, true);

        TextView start = findViewById(R.id.btn_start);
        start.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
