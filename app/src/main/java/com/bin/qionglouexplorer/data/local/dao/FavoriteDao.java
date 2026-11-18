package com.bin.qionglouexplorer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.data.local.entity.FavoriteEntity;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteEntity favorite);

    @Query("DELETE FROM favorite WHERE point_id = :pointId")
    void delete(int pointId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite WHERE point_id = :pointId)")
    LiveData<Boolean> observeExists(int pointId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite WHERE point_id = :pointId)")
    boolean exists(int pointId);

    @Query("SELECT COUNT(*) FROM favorite")
    LiveData<Integer> observeCount();

    @Query("SELECT p.* FROM culture_point p INNER JOIN favorite f ON p.id = f.point_id "
            + "ORDER BY f.created_at DESC")
    LiveData<List<CulturePointEntity>> observeFavoritePoints();

    @Query("DELETE FROM favorite")
    void clear();
}
