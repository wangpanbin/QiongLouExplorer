package com.bin.qionglouexplorer.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/** 消息通知。type = SYSTEM / FRIEND。 */
@Entity(
        tableName = "user_message",
        indices = {@Index("is_read")}
)
public class MessageEntity {

    public static final String TYPE_SYSTEM = "SYSTEM";
    public static final String TYPE_FRIEND = "FRIEND";

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "time_label")
    public String timeLabel;

    @ColumnInfo(name = "is_read")
    public int isRead;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    public MessageEntity() {
    }

    @NonNull
    public static MessageEntity create(String type, String title, String content, String timeLabel) {
        MessageEntity e = new MessageEntity();
        e.type = type;
        e.title = title;
        e.content = content;
        e.timeLabel = timeLabel;
        e.isRead = 0;
        e.createdAt = System.currentTimeMillis();
        return e;
    }
}
