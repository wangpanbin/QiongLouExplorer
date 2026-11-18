package com.bin.qionglouexplorer.data.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.bin.qionglouexplorer.data.local.AppDatabase;
import com.bin.qionglouexplorer.data.local.dao.CheckinDao;
import com.bin.qionglouexplorer.data.local.dao.MessageDao;
import com.bin.qionglouexplorer.data.local.dao.ProfileDao;
import com.bin.qionglouexplorer.data.local.dao.TaskDao;
import com.bin.qionglouexplorer.data.local.entity.CheckinRecordEntity;
import com.bin.qionglouexplorer.data.local.entity.ExplorationTaskEntity;
import com.bin.qionglouexplorer.data.local.entity.MessageEntity;
import com.bin.qionglouexplorer.data.local.entity.ProfileEntity;
import com.bin.qionglouexplorer.util.AppExecutors;

import java.util.List;

/**
 * 探索任务、打卡、奖励与通知的统一入口。
 * 打卡写入与经验积分累加在同一事务里，避免部分成功。
 */
public class TaskRepository {

    private final AppDatabase db;
    private final TaskDao taskDao;
    private final CheckinDao checkinDao;
    private final ProfileDao profileDao;
    private final MessageDao messageDao;

    public TaskRepository(Context context) {
        this.db = AppDatabase.get(context);
        this.taskDao = db.taskDao();
        this.checkinDao = db.checkinDao();
        this.profileDao = db.profileDao();
        this.messageDao = db.messageDao();
    }

    public LiveData<List<ExplorationTaskEntity>> observeTasks() {
        return taskDao.observeAll();
    }

    public LiveData<List<Integer>> observeCheckedTaskIds() {
        return checkinDao.observeCheckedTaskIds();
    }

    public LiveData<Integer> observeCheckedPointCount() {
        return checkinDao.observeCheckedPointCount();
    }

    public LiveData<Integer> observePendingSyncCount() {
        return checkinDao.observePendingCount();
    }

    public ExplorationTaskEntity loadTaskByPointSync(int pointId) {
        return taskDao.loadByPoint(pointId);
    }

    /**
     * 打卡/提交任务：写入打卡记录 + 累加经验积分 + 生成系统通知。
     * 已存在相同 (pointId, taskId) 记录时视为重复，直接回调 duplicated。
     */
    public void submitCheckin(int pointId, int taskId, String type,
                              int rewardExp, int rewardPoints,
                              Callback<CheckinResult> callback) {
        AppExecutors.io().execute(() -> {
            boolean exists = checkinDao.countByPointAndTask(pointId, taskId) > 0;
            if (exists) {
                AppExecutors.main(() -> callback.onResult(CheckinResult.duplicated()));
                return;
            }
            CheckinRecordEntity record = CheckinRecordEntity.create(
                    pointId, taskId, type, rewardExp, rewardPoints);
            checkinDao.insert(record);
            if (taskId == 0) {
                profileDao.addReward(rewardExp, rewardPoints);
            }
            final int pointIdFinal = pointId;
            final int taskIdFinal = taskId;
            final int expFinal = rewardExp;
            final int pointsFinal = rewardPoints;
            AppExecutors.main(() -> callback.onResult(
                    CheckinResult.success(pointIdFinal, taskIdFinal, expFinal, pointsFinal)));
        });
    }

    /** 提交任务：写打卡 + 加奖励 + 生成通知，均在同一 IO 任务内完成。 */
    public void submitTask(ExplorationTaskEntity task, Callback<CheckinResult> callback) {
        AppExecutors.io().execute(() -> {
            boolean exists = checkinDao.countByTask(task.id) > 0;
            if (exists) {
                AppExecutors.main(() -> callback.onResult(CheckinResult.duplicated()));
                return;
            }
            CheckinRecordEntity record = CheckinRecordEntity.create(
                    task.pointId, task.id, CheckinRecordEntity.TYPE_TASK_SUBMIT,
                    task.rewardExp, task.rewardPoints);
            checkinDao.insert(record);
            profileDao.addReward(task.rewardExp, task.rewardPoints);
            messageDao.insert(MessageEntity.create(
                    MessageEntity.TYPE_SYSTEM,
                    "任务完成：" + task.title,
                    "你完成了探索任务「" + task.title + "」，获得经验 +"
                            + task.rewardExp + "、积分 +" + task.rewardPoints + "。",
                    "刚刚"));
            AppExecutors.main(() -> callback.onResult(
                    CheckinResult.success(task.pointId, task.id, task.rewardExp, task.rewardPoints)));
        });
    }

    /** 离线待同步打卡批量上传（模拟服务端校验）。返回处理条数。 */
    public void syncPending(Callback<Integer> callback) {
        AppExecutors.io().execute(() -> {
            int pending = checkinDao.pendingCount();
            if (pending > 0) {
                checkinDao.markAllSynced();
                messageDao.insert(MessageEntity.create(
                        MessageEntity.TYPE_SYSTEM,
                        "离线打卡已同步",
                        pending + " 条离线打卡记录已完成服务端校验并同步。",
                        "刚刚"));
            }
            final int count = pending;
            AppExecutors.main(() -> callback.onResult(count));
        });
    }

    public interface Callback<T> {
        void onResult(T value);
    }

    /** 打卡结果。duplicated 为 true 时 reward 字段无意义。 */
    public static class CheckinResult {
        public final boolean duplicated;
        public final int pointId;
        public final int taskId;
        public final int rewardExp;
        public final int rewardPoints;

        private CheckinResult(boolean duplicated, int pointId, int taskId,
                              int rewardExp, int rewardPoints) {
            this.duplicated = duplicated;
            this.pointId = pointId;
            this.taskId = taskId;
            this.rewardExp = rewardExp;
            this.rewardPoints = rewardPoints;
        }

        static CheckinResult duplicated() {
            return new CheckinResult(true, 0, 0, 0, 0);
        }

        static CheckinResult success(int pointId, int taskId, int rewardExp, int rewardPoints) {
            return new CheckinResult(false, pointId, taskId, rewardExp, rewardPoints);
        }
    }
}
