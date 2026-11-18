package com.bin.qionglouexplorer;

import android.app.Application;

import com.bin.qionglouexplorer.data.seed.SeedImporter;
import com.bin.qionglouexplorer.util.AppExecutors;

/** 应用入口：负责首帧前的种子数据准备。 */
public class QiongLouApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppExecutors.io().execute(() -> SeedImporter.ensureSeeded(this));
    }
}
