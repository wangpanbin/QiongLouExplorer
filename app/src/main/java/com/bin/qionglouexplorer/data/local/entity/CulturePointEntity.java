package com.bin.qionglouexplorer.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 文化点位。字段与 plan.md 4.3 的 culture_point 表一一映射，便于 V0.2 迁移到 MySQL。
 */
@Entity(tableName = "culture_point")
public class CulturePointEntity {

    @PrimaryKey
    @NonNull
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    /** architecture / history / folk / food */
    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "era")
    public String era;

    @ColumnInfo(name = "style")
    public String style;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "summary")
    public String summary;

    @ColumnInfo(name = "description")
    public String description;

    /** 逗号分隔标签 */
    @ColumnInfo(name = "tags")
    public String tags;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "rating")
    public float rating;

    @ColumnInfo(name = "visit_count")
    public int visitCount;

    /** drawable 资源名，V0.2 可换成图片 URL */
    @ColumnInfo(name = "cover_res")
    public String coverRes;

    @ColumnInfo(name = "source_note")
    public String sourceNote;

    /** 是否已离线缓存 */
    @ColumnInfo(name = "cached")
    public int cached;

    @ColumnInfo(name = "status")
    public int status;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    public CulturePointEntity() {
    }

    public boolean isCached() {
        return cached == 1;
    }

    /** 标签数组，供 UI 直接使用 */
    public String[] tagArray() {
        if (tags == null || tags.trim().isEmpty()) {
            return new String[0];
        }
        return tags.split(",");
    }
}
