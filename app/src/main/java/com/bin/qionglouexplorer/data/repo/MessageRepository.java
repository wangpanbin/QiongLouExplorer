package com.bin.qionglouexplorer.data.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.bin.qionglouexplorer.data.local.AppDatabase;
import com.bin.qionglouexplorer.data.local.dao.MessageDao;
import com.bin.qionglouexplorer.data.local.entity.MessageEntity;
import com.bin.qionglouexplorer.util.AppExecutors;

import java.util.List;

/** 消息通知。 */
public class MessageRepository {

    private final MessageDao messageDao;

    public MessageRepository(Context context) {
        this.messageDao = AppDatabase.get(context).messageDao();
    }

    public LiveData<List<MessageEntity>> observeMessages() {
        return messageDao.observeAll();
    }

    public LiveData<Integer> observeUnreadCount() {
        return messageDao.observeUnreadCount();
    }

    public void markAllRead() {
        AppExecutors.io().execute(messageDao::markAllRead);
    }
}
