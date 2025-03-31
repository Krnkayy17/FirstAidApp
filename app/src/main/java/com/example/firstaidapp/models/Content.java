package com.example.firstaidapp.models;

public class Content {
    private int contentID;
    private int moduleID;
    private String contentTitle;
    private String contentText;
    private String contentImage;
    private String contentURL;
    private int contentOrder;

    // Constructor
    public Content(int contentID, int moduleID, String contentTitle, String contentText, String contentImage, String contentURL, int contentOrder) {
        this.contentID = contentID;
        this.moduleID = moduleID;
        this.contentTitle = contentTitle;
        this.contentText = contentText;
        this.contentImage = contentImage;
        this.contentURL = contentURL;
        this.contentOrder = contentOrder;
    }

    // Getters
    public int getContentID() {
        return contentID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public String getContentText() {
        return contentText;
    }

    public String getContentImage() {
        return contentImage;
    }

    public String getContentURL() {
        return contentURL;
    }

    public int getContentOrder() {
        return contentOrder;
    }

    // Setters
    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public void setContentImage(String contentImage) {
        this.contentImage = contentImage;
    }

    public void setContentURL(String contentURL) {
        this.contentURL = contentURL;
    }

    public void setContentOrder(int contentOrder) {
        this.contentOrder = contentOrder;
    }
}

