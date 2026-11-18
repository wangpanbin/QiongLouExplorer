package com.bin.qionglouexplorer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.bin.qionglouexplorer.data.local.entity.CheckinRecordEntity;

import java.util.List;

@Dao
public interface CheckinDao {

    @Insert
    long insert(CheckinRecordEntity record);

    @Query("SELECT COUNT(*) FROM checkin_record WHERE point_id = :pointId AND task_id = :taskId")
    int countByPointAndTask(int pointId, int taskId);

    @Query("SELECT COUNT(*) FROM checkin_record WHERE task_id = :taskId AND task_id != 0")
    int countByTask(int taskId);

    @Query("SELECT COUNT(DISTINCT point_id) FROM checkin_record")
    LiveData<Integer> observeCheckedPointCount();

    @Query("SELECT COUNT(DISTINCT point_id) FROM checkin_record")
    int checkedPointCount();

    @Query("SELECT task_id FROM checkin_record WHERE task_id != 0")
    LiveData<List<Integer>> observeCheckedTaskIds();

    @Query("SELECT task_id FROM checkin_record WHERE task_id != 0")
    List<Integer> checkedTaskIds();

    @Query("SELECT COUNT(*) FROM checkin_record WHERE sync_status = 'PENDING'")
    LiveData<Integer> observePendingCount();

    @Query("SELECT COUNT(*) FROM checkin_record WHERE sync_status = 'PENDING'")
    int pendingCount();

    @Query("UPDATE checkin_record SET sync_status = 'SYNCED' WHERE sync_status = 'PENDING'")
    int markAllSynced();

    @Query("SELECT * FROM checkin_record ORDER BY checkin_time DESC")
    LiveData<List<CheckinRecordEntity>> observeAll();

    @Query("DELETE FROM checkin_record")
    void clear();
}
