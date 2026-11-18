package com.bin.qionglouexplorer.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** 单用户档案，id 固定为 1。 */
@Entity(tableName = "user_profile")
public class ProfileEntity {

    @PrimaryKey
    @NonNull
    public int id = 1;

    @ColumnInfo(name = "nickname")
    public String nickname;

    @ColumnInfo(name = "signature")
    public String signature;

    @ColumnInfo(name = "avatar_res")
    public String avatarRes;

    @ColumnInfo(name = "exp")
    public int exp;

    @ColumnInfo(name = "points")
    public int points;

    @ColumnInfo(name = "badge_count")
    public int badgeCount;

    public ProfileEntity() {
    }

    /** 经验值换算等级：每 100 经验一级，最低 1 级。 */
    public int level() {
        return Math.max(1, exp / 100 + 1);
    }
}
