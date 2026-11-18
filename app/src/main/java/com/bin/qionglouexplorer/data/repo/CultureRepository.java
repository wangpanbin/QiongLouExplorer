package com.bin.qionglouexplorer.data.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bin.qionglouexplorer.data.local.AppDatabase;
import com.bin.qionglouexplorer.data.local.dao.FavoriteDao;
import com.bin.qionglouexplorer.data.local.dao.PointDao;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.data.local.entity.FavoriteEntity;
import com.bin.qionglouexplorer.util.AppExecutors;
import com.bin.qionglouexplorer.util.GeoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 文化点位与收藏的统一入口。
 * 全部暴露 LiveData，V0.2 换成 Retrofit + Room 双源时调用方无需改动。
 */
public class CultureRepository {

    /** 分类编码 → 中文名资源 id，供筛选芯片与列表使用。 */
    public static final String[] CATEGORIES = {"architecture", "history", "folk", "food"};

    private final PointDao pointDao;
    private final FavoriteDao favoriteDao;
    private final MutableLiveData<Boolean> favoriteChanged = new MutableLiveData<>();

    public CultureRepository(Context context) {
        AppDatabase db = AppDatabase.get(context);
        this.pointDao = db.pointDao();
        this.favoriteDao = db.favoriteDao();
    }

    public LiveData<List<CulturePointEntity>> observePoints() {
        return pointDao.observeAll();
    }

    public LiveData<CulturePointEntity> observePoint(int id) {
        return pointDao.observeById(id);
    }

    public LiveData<List<CulturePointEntity>> observeRelated(String category, int excludeId) {
        return pointDao.observeRelated(category, excludeId);
    }

    public LiveData<List<CulturePointEntity>> search(String keyword) {
        return pointDao.search(keyword == null ? "" : keyword.trim());
    }

    public LiveData<List<CulturePointEntity>> observeFavorites() {
        return favoriteDao.observeFavoritePoints();
    }

    public LiveData<Boolean> observeFavoriteExists(int pointId) {
        return favoriteDao.observeExists(pointId);
    }

    public LiveData<Integer> observeCachedCount() {
        return pointDao.observeCachedCount();
    }

    public MutableLiveData<Boolean> favoriteChanged() {
        return favoriteChanged;
    }

    /** 切换收藏状态，回调切换后的状态。 */
    public void toggleFavorite(int pointId, Callback<Boolean> callback) {
        AppExecutors.io().execute(() -> {
            boolean nowFavorite;
            if (favoriteDao.exists(pointId)) {
                favoriteDao.delete(pointId);
                nowFavorite = false;
            } else {
                favoriteDao.insert(FavoriteEntity.create(pointId));
                pointDao.markCached(pointId);
                nowFavorite = true;
            }
            boolean result = nowFavorite;
            AppExecutors.main(() -> {
                favoriteChanged.setValue(result);
                if (callback != null) {
                    callback.onResult(result);
                }
            });
        });
    }

    /** 同步取全部点位（后台线程用），按距街区中心距离升序。 */    public List<CulturePointEntity> loadNearestSync(int limit) {
        List<CulturePointEntity> all = new ArrayList<>(pointDao.loadAll());
        Collections.sort(all, (a, b) -> Double.compare(
                GeoUtils.distanceFromAnchor(a.latitude, a.longitude),
                GeoUtils.distanceFromAnchor(b.latitude, b.longitude)));
        if (all.size() > limit) {
            return new ArrayList<>(all.subList(0, limit));
        }
        return all;
    }

    public List<CulturePointEntity> loadAllSync() {
        return pointDao.loadAll();
    }

    public CulturePointEntity loadPointSync(int id) {
        return pointDao.loadById(id);
    }

    /** 标记某点位为已离线缓存。 */
    public void cachePoint(int pointId) {
        AppExecutors.io().execute(() -> pointDao.markCached(pointId));
    }

    public interface Callback<T> {
        void onResult(T value);
    }
}
