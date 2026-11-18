package com.bin.qionglouexplorer.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** 收藏，pointId 作主键天然去重。 */
@Entity(tableName = "favorite")
public class FavoriteEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "point_id")
    public int pointId;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    public FavoriteEntity() {
    }

    public static FavoriteEntity create(int pointId) {
        FavoriteEntity e = new FavoriteEntity();
        e.pointId = pointId;
        e.createdAt = System.currentTimeMillis();
        return e;
    }
}
