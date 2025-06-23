package com.example.firstaidapp.models;

public class VideoClickLog {
    private int id;
    private int moduleId;
    private String videoTitle;
    private String youtubeVideoId;
    private String timestamp;

    private String tag; // ✅ NEW

    public VideoClickLog(int id, int moduleId, String videoTitle, String youtubeVideoId, String timestamp, String tag) {
        this.id = id;
        this.moduleId = moduleId;
        this.videoTitle = videoTitle;
        this.youtubeVideoId = youtubeVideoId;
        this.timestamp = timestamp;
        this.tag = tag;
    }

    public VideoClickLog(int moduleId, String videoTitle, String youtubeVideoId, String timestamp, String tag) {
        this(-1, moduleId, videoTitle, youtubeVideoId, timestamp, tag);
    }

    // Getters
    public int getId() { return id; }
    public int getModuleId() { return moduleId; }
    public String getVideoTitle() { return videoTitle; }
    public String getYoutubeVideoId() { return youtubeVideoId; }
    public String getTimestamp() { return timestamp; }
    public String getTag() { return tag; }
}
