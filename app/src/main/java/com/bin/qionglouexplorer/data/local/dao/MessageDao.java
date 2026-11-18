package com.bin.qionglouexplorer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bin.qionglouexplorer.data.local.entity.MessageEntity;

import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM user_message ORDER BY created_at DESC, id DESC")
    LiveData<List<MessageEntity>> observeAll();

    @Query("SELECT COUNT(*) FROM user_message WHERE is_read = 0")
    LiveData<Integer> observeUnreadCount();

    @Query("UPDATE user_message SET is_read = 1")
    void markAllRead();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MessageEntity message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MessageEntity> messages);

    @Query("DELETE FROM user_message")
    void clear();
}
