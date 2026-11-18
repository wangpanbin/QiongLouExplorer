package com.bin.qionglouexplorer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bin.qionglouexplorer.data.local.entity.ProfileEntity;

@Dao
public interface ProfileDao {

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    LiveData<ProfileEntity> observe();

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    ProfileEntity load();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProfileEntity profile);

    @Update
    void update(ProfileEntity profile);

    /** 事务内累加经验与积分，避免读改写竞态。 */
    @Query("UPDATE user_profile SET exp = exp + :exp, points = points + :points WHERE id = 1")
    void addReward(int exp, int points);

    @Query("UPDATE user_profile SET badge_count = :badgeCount WHERE id = 1")
    void updateBadgeCount(int badgeCount);

    @Query("DELETE FROM user_profile")
    void clear();
}
