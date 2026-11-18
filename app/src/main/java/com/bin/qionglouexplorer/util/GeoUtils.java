package com.bin.qionglouexplorer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

/**
 * 距离计算与网络状态工具。
 * 距离用 Haversine 公式，单位米；地图与首页的「距你」均以此为准。
 */
public final class GeoUtils {

    private static final double EARTH_RADIUS_METERS = 6_371_000d;

    /** 街区中心点（骑楼老街附近），V0.1 作为「我的位置」锚点。 */
    public static final double ANCHOR_LATITUDE = 20.0448;
    public static final double ANCHOR_LONGITUDE = 110.3435;

    private GeoUtils() {
    }

    /** Haversine 距离，返回米。 */
    public static double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_METERS * c;
    }

    /** 距街区中心的距离。 */
    public static double distanceFromAnchor(double lat, double lon) {
        return distanceMeters(ANCHOR_LATITUDE, ANCHOR_LONGITUDE, lat, lon);
    }

    /** 人类可读距离：<1km 显示米（十位取整），否则显示公里一位小数。 */
    public static String formatDistance(Context context, double meters) {
        if (meters < 1000) {
            int rounded = (int) (Math.round(meters / 10d) * 10);
            return context.getString(com.bin.qionglouexplorer.R.string.common_meters,
                    Math.max(rounded, 10));
        }
        return context.getString(com.bin.qionglouexplorer.R.string.common_km, meters / 1000d);
    }

    /** 当前是否有可用网络（离线提示条与离线页判断依据）。 */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return true;
        }
        Network network = cm.getActiveNetwork();
        if (network == null) {
            return false;
        }
        NetworkCapabilities caps = cm.getNetworkCapabilities(network);
        return caps != null
                && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
