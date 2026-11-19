package com.bin.qionglouexplorer;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import com.bin.qionglouexplorer.ui.assistant.AssistantFragment;
import com.bin.qionglouexplorer.ui.home.HomeFragment;
import com.bin.qionglouexplorer.ui.map.MapFragment;
import com.bin.qionglouexplorer.ui.profile.ProfileFragment;
import com.bin.qionglouexplorer.ui.task.TaskFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/** 5 Tab 容器：首页 / 地图 / 任务 / 助手 / 我的。 */
public class MainActivity extends BaseActivity {

    private static final String STATE_TAB = "state_tab";

    private BottomNavigationView bottomNav;
    private int currentTab = R.id.nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applySystemBarInsets(R.id.main, true, true);

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            showTab(item.getItemId());
            return true;
        });

        if (savedInstanceState != null) {
            currentTab = savedInstanceState.getInt(STATE_TAB, R.id.nav_home);
        }
        bottomNav.setSelectedItemId(currentTab);
        if (savedInstanceState == null) {
            showTab(currentTab);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_TAB, currentTab);
    }

    private void showTab(@IdRes int itemId) {
        currentTab = itemId;
        Fragment fragment = createFragment(itemId);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private Fragment createFragment(@IdRes int itemId) {
        if (itemId == R.id.nav_map) {
            return new MapFragment();
        } else if (itemId == R.id.nav_task) {
            return new TaskFragment();
        } else if (itemId == R.id.nav_assistant) {
            return new AssistantFragment();
        } else if (itemId == R.id.nav_profile) {
            return new ProfileFragment();
        }
        return new HomeFragment();
    }

    /** 供子页面跨 Tab 跳转（如四宫格「任务挑战」）。 */
    public void switchTab(@IdRes int itemId) {
        bottomNav.setSelectedItemId(itemId);
    }
}
