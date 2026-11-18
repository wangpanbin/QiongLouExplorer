package com.bin.qionglouexplorer.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** 统一的线程池：Room 写操作与一次性任务走 IO，UI 回调用 main。 */
public final class AppExecutors {

    private static final ExecutorService IO = Executors.newFixedThreadPool(3);
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    private AppExecutors() {
    }

    public static ExecutorService io() {
        return IO;
    }

    public static void main(Runnable runnable) {
        MAIN.post(runnable);
    }
}
