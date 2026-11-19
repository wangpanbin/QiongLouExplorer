package com.bin.qionglouexplorer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

/**
 * 网络可达性监听。离线提示需要跟随网络状态实时变化，
 * 只在 onResume 检查一次会漏掉「停在页面上切换飞行模式」的场景。
 */
public final class NetworkMonitor {

    public interface Listener {
        void onNetworkChanged(boolean online);
    }

    private final Context appContext;
    private final ConnectivityManager manager;
    private ConnectivityManager.NetworkCallback callback;

    public NetworkMonitor(Context context) {
        this.appContext = context.getApplicationContext();
        this.manager = (ConnectivityManager)
                appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /** 开始监听，注册后立即回调一次当前状态。 */
    public void start(Listener listener) {
        if (manager == null) {
            listener.onNetworkChanged(true);
            return;
        }
        stop();
        callback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                AppExecutors.main(() -> listener.onNetworkChanged(true));
            }

            @Override
            public void onLost(Network network) {
                AppExecutors.main(() -> listener.onNetworkChanged(false));
            }

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities caps) {
                boolean online = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                AppExecutors.main(() -> listener.onNetworkChanged(online));
            }
        };
        try {
            manager.registerNetworkCallback(new NetworkRequest.Builder().build(), callback);
        } catch (SecurityException ignored) {
            // 缺少 ACCESS_NETWORK_STATE 时不崩溃，界面保持在线假设
        }
        listener.onNetworkChanged(GeoUtils.isOnline(appContext));
    }

    public void stop() {
        if (manager != null && callback != null) {
            try {
                manager.unregisterNetworkCallback(callback);
            } catch (IllegalArgumentException ignored) {
                // 未注册成功时忽略
            }
        }
        callback = null;
    }
}
