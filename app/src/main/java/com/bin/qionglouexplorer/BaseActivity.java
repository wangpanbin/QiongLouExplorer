package com.bin.qionglouexplorer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 统一处理 edge-to-edge 与系统栏内边距，供各 Activity 复用。
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
    }

    /** 给根布局应用系统栏 padding；状态栏区域需要绘制背景时用 applyTop=false。 */
    protected void applySystemBarInsets(int rootViewId, boolean applyTop, boolean applyBottom) {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(rootViewId), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left,
                    applyTop ? bars.top : 0,
                    bars.right,
                    applyBottom ? bars.bottom : 0);
            return insets;
        });
    }
}
