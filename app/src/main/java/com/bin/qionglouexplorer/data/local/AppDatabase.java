package com.bin.qionglouexplorer.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bin.qionglouexplorer.data.local.dao.CheckinDao;
import com.bin.qionglouexplorer.data.local.dao.FavoriteDao;
import com.bin.qionglouexplorer.data.local.dao.MessageDao;
import com.bin.qionglouexplorer.data.local.dao.PointDao;
import com.bin.qionglouexplorer.data.local.dao.ProfileDao;
import com.bin.qionglouexplorer.data.local.dao.TaskDao;
import com.bin.qionglouexplorer.data.local.entity.CheckinRecordEntity;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.data.local.entity.ExplorationTaskEntity;
import com.bin.qionglouexplorer.data.local.entity.FavoriteEntity;
import com.bin.qionglouexplorer.data.local.entity.MessageEntity;
import com.bin.qionglouexplorer.data.local.entity.ProfileEntity;

@Database(
        entities = {
                CulturePointEntity.class,
                ExplorationTaskEntity.class,
                CheckinRecordEntity.class,
                FavoriteEntity.class,
                MessageEntity.class,
                ProfileEntity.class
        },
        version = 1,
        exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "qionglou.db";
    private static volatile AppDatabase instance;

    public abstract PointDao pointDao();

    public abstract TaskDao taskDao();

    public abstract CheckinDao checkinDao();

    public abstract FavoriteDao favoriteDao();

    public abstract MessageDao messageDao();

    public abstract ProfileDao profileDao();

    public static AppDatabase get(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
