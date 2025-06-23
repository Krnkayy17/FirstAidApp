package com.example.firstaidapp.models;

public class VideoRecommendation {
    private int id;
    private int moduleId;
    private String title;
    private String url;
    private String thumbnailUrl;
    private String tag;  // ✅ New field for topic/AI tag

    public VideoRecommendation() {}

    public VideoRecommendation(int moduleId, String title, String url, String thumbnailUrl, String tag) {
        this.moduleId = moduleId;
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.tag = tag;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getModuleId() { return moduleId; }
    public void setModuleId(int moduleId) { this.moduleId = moduleId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public String getYoutubeVideoId() {
        if (url != null && url.contains("youtu.be/")) {
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return "";
    }
}
