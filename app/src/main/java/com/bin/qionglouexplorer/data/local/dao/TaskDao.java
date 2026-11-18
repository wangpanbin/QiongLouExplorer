package com.bin.qionglouexplorer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bin.qionglouexplorer.data.local.entity.ExplorationTaskEntity;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM exploration_task ORDER BY id ASC")
    LiveData<List<ExplorationTaskEntity>> observeAll();

    @Query("SELECT * FROM exploration_task ORDER BY id ASC")
    List<ExplorationTaskEntity> loadAll();

    @Query("SELECT * FROM exploration_task WHERE point_id = :pointId LIMIT 1")
    ExplorationTaskEntity loadByPoint(int pointId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ExplorationTaskEntity> tasks);

    @Query("DELETE FROM exploration_task")
    void clear();
}
