package com.bin.qionglouexplorer.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** 探索任务，一个点位对应一个挑战任务。 */
@Entity(tableName = "exploration_task")
public class ExplorationTaskEntity {

    @PrimaryKey
    @NonNull
    public int id;

    @ColumnInfo(name = "point_id")
    public int pointId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "reward_exp")
    public int rewardExp;

    @ColumnInfo(name = "reward_points")
    public int rewardPoints;

    public ExplorationTaskEntity() {
    }
}
