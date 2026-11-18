package com.bin.qionglouexplorer.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** assets/seed_points.json 的反序列化模型。 */
public class SeedBundle {

    @SerializedName("seedVersion")
    public int seedVersion;

    @SerializedName("points")
    public List<SeedPoint> points;

    @SerializedName("tasks")
    public List<SeedTask> tasks;

    @SerializedName("messages")
    public List<SeedMessage> messages;

    @SerializedName("profile")
    public SeedProfile profile;

    public static class SeedPoint {
        public int id;
        public String name;
        public String category;
        public String era;
        public String style;
        public String address;
        public String summary;
        public String description;
        public String tags;
        public double latitude;
        public double longitude;
        public float rating;
        public int visitCount;
        public String coverRes;
        public String sourceNote;
        public int cached;
        public int status;
        public int sortOrder;
    }

    public static class SeedTask {
        public int id;
        public int pointId;
        public String title;
        public String description;
        public int rewardExp;
        public int rewardPoints;
    }

    public static class SeedMessage {
        public long id;
        public String type;
        public String title;
        public String content;
        public String timeLabel;
        public int isRead;
        public long createdAt;
    }

    public static class SeedProfile {
        public int id;
        public String nickname;
        public String signature;
        public String avatarRes;
        public int exp;
        public int points;
        public int badgeCount;
    }
}
