package com.bin.qionglouexplorer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;

import java.util.List;

@Dao
public interface PointDao {

    @Query("SELECT * FROM culture_point WHERE status = 1 ORDER BY sort_order ASC")
    LiveData<List<CulturePointEntity>> observeAll();

    @Query("SELECT * FROM culture_point WHERE status = 1 ORDER BY sort_order ASC")
    List<CulturePointEntity> loadAll();

    @Query("SELECT * FROM culture_point WHERE id = :id LIMIT 1")
    LiveData<CulturePointEntity> observeById(int id);

    @Query("SELECT * FROM culture_point WHERE id = :id LIMIT 1")
    CulturePointEntity loadById(int id);

    @Query("SELECT * FROM culture_point WHERE status = 1 AND category = :category "
            + "AND id != :excludeId ORDER BY sort_order ASC LIMIT 6")
    LiveData<List<CulturePointEntity>> observeRelated(String category, int excludeId);

    @Query("SELECT * FROM culture_point WHERE status = 1 AND category = :category "
            + "ORDER BY sort_order ASC")
    List<CulturePointEntity> loadByCategory(String category);

    @Query("SELECT * FROM culture_point WHERE cached = 1 AND status = 1")
    List<CulturePointEntity> loadCached();

    @Query("SELECT COUNT(*) FROM culture_point WHERE cached = 1 AND status = 1")
    LiveData<Integer> observeCachedCount();

    @Query("SELECT * FROM culture_point WHERE status = 1 AND ("
            + "name LIKE '%' || :keyword || '%' OR tags LIKE '%' || :keyword || '%' "
            + "OR summary LIKE '%' || :keyword || '%') ORDER BY sort_order ASC")
    LiveData<List<CulturePointEntity>> search(String keyword);

    @Query("UPDATE culture_point SET cached = 1 WHERE id = :id")
    void markCached(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CulturePointEntity> points);

    @Update
    void update(CulturePointEntity point);

    @Query("DELETE FROM culture_point")
    void clear();
}
