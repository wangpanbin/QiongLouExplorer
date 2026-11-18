package com.bin.qionglouexplorer.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * 打卡记录。以 (pointId, taskId) 唯一索引防重复打卡；
 * syncStatus 承载离线待同步队列语义（PENDING / SYNCED）。
 */
@Entity(
        tableName = "checkin_record",
        indices = {@Index(value = {"point_id", "task_id"}, unique = true)}
)
public class CheckinRecordEntity {

    public static final String TYPE_CAMERA_SCAN = "CAMERA_SCAN";
    public static final String TYPE_MANUAL_CODE = "MANUAL_CODE";
    public static final String TYPE_TASK_SUBMIT = "TASK_SUBMIT";

    public static final String SYNC_PENDING = "PENDING";
    public static final String SYNC_SYNCED = "SYNCED";

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "point_id")
    public int pointId;

    /** 0 表示与任务无关的纯点位打卡 */
    @ColumnInfo(name = "task_id")
    public int taskId;

    @ColumnInfo(name = "checkin_type")
    public String checkinType;

    @ColumnInfo(name = "checkin_time")
    public long checkinTime;

    @ColumnInfo(name = "sync_status")
    public String syncStatus;

    @ColumnInfo(name = "reward_exp")
    public int rewardExp;

    @ColumnInfo(name = "reward_points")
    public int rewardPoints;

    public CheckinRecordEntity() {
    }

    @NonNull
    public static CheckinRecordEntity create(int pointId, int taskId, String type,
                                             int rewardExp, int rewardPoints) {
        CheckinRecordEntity e = new CheckinRecordEntity();
        e.pointId = pointId;
        e.taskId = taskId;
        e.checkinType = type;
        e.checkinTime = System.currentTimeMillis();
        e.syncStatus = SYNC_PENDING;
        e.rewardExp = rewardExp;
        e.rewardPoints = rewardPoints;
        return e;
    }
}
