package com.bin.qionglouexplorer.data.seed;

import android.content.Context;
import android.content.res.AssetManager;

import com.bin.qionglouexplorer.data.local.AppDatabase;
import com.bin.qionglouexplorer.data.local.entity.CheckinRecordEntity;
import com.bin.qionglouexplorer.data.local.entity.CulturePointEntity;
import com.bin.qionglouexplorer.data.local.entity.ExplorationTaskEntity;
import com.bin.qionglouexplorer.data.local.entity.FavoriteEntity;
import com.bin.qionglouexplorer.data.local.entity.MessageEntity;
import com.bin.qionglouexplorer.data.local.entity.ProfileEntity;
import com.bin.qionglouexplorer.data.model.SeedBundle;
import com.bin.qionglouexplorer.util.PrefsManager;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** 首次启动注入种子数据；「清除本地数据」时以同样逻辑重建。 */
public final class SeedImporter {

    private static final String ASSET_NAME = "seed_points.json";

    private SeedImporter() {
    }

    /**
     * 数据库为空或版本号变化时导入种子数据。
     *
     * @return true 表示本次执行了导入
     */
    public static boolean ensureSeeded(Context context) {
        AppDatabase db = AppDatabase.get(context);
        SeedBundle bundle = readSeed(context);
        if (bundle == null) {
            return false;
        }
        int seededVersion = PrefsManager.getSeedVersion(context);
        boolean empty = db.pointDao().loadAll().isEmpty();
        if (!empty && seededVersion == bundle.seedVersion) {
            return false;
        }
        importBundle(db, bundle);
        PrefsManager.setSeedVersion(context, bundle.seedVersion);
        return true;
    }

    /** 清除全部业务数据并重建种子数据（保留种子版本号）。 */
    public static void reset(Context context) {
        AppDatabase db = AppDatabase.get(context);
        db.checkinDao().clear();
        db.favoriteDao().clear();
        db.profileDao().clear();
        db.messageDao().clear();
        db.taskDao().clear();
        db.pointDao().clear();
        SeedBundle bundle = readSeed(context);
        if (bundle != null) {
            importBundle(db, bundle);
        }
    }

    private static void importBundle(AppDatabase db, SeedBundle bundle) {
        List<CulturePointEntity> points = new ArrayList<>();
        if (bundle.points != null) {
            for (SeedBundle.SeedPoint s : bundle.points) {
                CulturePointEntity p = new CulturePointEntity();
                p.id = s.id;
                p.name = s.name;
                p.category = s.category;
                p.era = s.era;
                p.style = s.style;
                p.address = s.address;
                p.summary = s.summary;
                p.description = s.description;
                p.tags = s.tags;
                p.latitude = s.latitude;
                p.longitude = s.longitude;
                p.rating = s.rating;
                p.visitCount = s.visitCount;
                p.coverRes = s.coverRes;
                p.sourceNote = s.sourceNote;
                p.cached = s.cached;
                p.status = s.status;
                p.sortOrder = s.sortOrder;
                points.add(p);
            }
        }
        db.pointDao().insertAll(points);

        List<ExplorationTaskEntity> tasks = new ArrayList<>();
        if (bundle.tasks != null) {
            for (SeedBundle.SeedTask s : bundle.tasks) {
                ExplorationTaskEntity t = new ExplorationTaskEntity();
                t.id = s.id;
                t.pointId = s.pointId;
                t.title = s.title;
                t.description = s.description;
                t.rewardExp = s.rewardExp;
                t.rewardPoints = s.rewardPoints;
                tasks.add(t);
            }
        }
        db.taskDao().insertAll(tasks);

        List<MessageEntity> messages = new ArrayList<>();
        if (bundle.messages != null) {
            long base = System.currentTimeMillis();
            for (SeedBundle.SeedMessage s : bundle.messages) {
                MessageEntity m = new MessageEntity();
                m.id = s.id;
                m.type = s.type;
                m.title = s.title;
                m.content = s.content;
                m.timeLabel = s.timeLabel;
                m.isRead = s.isRead;
                // 种子消息按序号错开时间，保证倒序稳定
                m.createdAt = base - s.id;
                messages.add(m);
            }
        }
        db.messageDao().insertAll(messages);

        if (bundle.profile != null) {
            ProfileEntity p = new ProfileEntity();
            p.id = 1;
            p.nickname = bundle.profile.nickname;
            p.signature = bundle.profile.signature;
            p.avatarRes = bundle.profile.avatarRes;
            p.exp = bundle.profile.exp;
            p.points = bundle.profile.points;
            p.badgeCount = bundle.profile.badgeCount;
            db.profileDao().insert(p);
        }
    }

    private static SeedBundle readSeed(Context context) {
        AssetManager assets = context.getAssets();
        try (InputStream is = assets.open(ASSET_NAME);
             Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return new Gson().fromJson(reader, SeedBundle.class);
        } catch (IOException e) {
            return null;
        }
    }

    /** 供测试与调试使用：直接读 assets 内容（不落库）。 */
    public static SeedBundle readSeedForTest(Context context) {
        return readSeed(context);
    }
}
