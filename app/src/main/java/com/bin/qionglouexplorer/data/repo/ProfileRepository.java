package com.bin.qionglouexplorer.data.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.bin.qionglouexplorer.data.local.AppDatabase;
import com.bin.qionglouexplorer.data.local.dao.CheckinDao;
import com.bin.qionglouexplorer.data.local.dao.ProfileDao;
import com.bin.qionglouexplorer.data.local.entity.ProfileEntity;

/** 用户档案与探索统计。 */
public class ProfileRepository {

    private final ProfileDao profileDao;
    private final CheckinDao checkinDao;

    public ProfileRepository(Context context) {
        AppDatabase db = AppDatabase.get(context);
        this.profileDao = db.profileDao();
        this.checkinDao = db.checkinDao();
    }

    public LiveData<ProfileEntity> observeProfile() {
        return profileDao.observe();
    }

    public LiveData<Integer> observeCheckedPointCount() {
        return checkinDao.observeCheckedPointCount();
    }

    public LiveData<Integer> observePendingCount() {
        return checkinDao.observePendingCount();
    }
}
