package com.bin.qionglouexplorer.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Haversine 距离计算的边界与量级验证。
 * 只测纯函数部分：formatDistance 依赖 Context，放在界面验收里验证。
 */
public class GeoUtilsTest {

    private static final double DELTA_METERS = 5d;

    @Test
    public void samePointDistanceIsZero() {
        double d = GeoUtils.distanceMeters(20.0448, 110.3430, 20.0448, 110.3430);
        assertEquals(0d, d, 0.001d);
    }

    @Test
    public void oneDegreeOfLatitudeIsAbout111Km() {
        double d = GeoUtils.distanceMeters(0d, 0d, 1d, 0d);
        // 1 度纬度 ≈ 111.19 km，允许 0.1 km 误差
        assertEquals(111_190d, d, 100d);
    }

    @Test
    public void nearbyPointsWithinRiderStreetAreHundredsOfMeters() {
        // 骑楼老街(20.0448,110.3430) → 海口钟楼(20.0420,110.3468)
        double d = GeoUtils.distanceMeters(20.0448, 110.3430, 20.0420, 110.3468);
        assertTrue("实际约 460 米，得到 " + d, d > 300d && d < 700d);
    }

    @Test
    public void distanceIsSymmetric() {
        double ab = GeoUtils.distanceMeters(20.0466, 110.3402, 20.0432, 110.3452);
        double ba = GeoUtils.distanceMeters(20.0432, 110.3452, 20.0466, 110.3402);
        assertEquals(ab, ba, 0.001d);
    }

    @Test
    public void anchorDistanceMatchesDirectCall() {
        double direct = GeoUtils.distanceMeters(
                GeoUtils.ANCHOR_LATITUDE, GeoUtils.ANCHOR_LONGITUDE, 20.0420, 110.3468);
        double viaAnchor = GeoUtils.distanceFromAnchor(20.0420, 110.3468);
        assertEquals(direct, viaAnchor, 0.001d);
    }

    @Test
    public void farAwayPointIsKilometersScale() {
        // 海口 → 北京天安门，约 2200 公里量级
        double d = GeoUtils.distanceMeters(20.0448, 110.3430, 39.9087, 116.3975);
        assertTrue("实际约 2200 公里，得到 " + d, d > 2_000_000d && d < 2_400_000d);
    }
}
