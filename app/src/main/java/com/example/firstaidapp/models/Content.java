package com.example.firstaidapp.models;

import android.database.Cursor;

public class Content {
    private int contentId;
    private int moduleId;
    private String contentTitle;
    private String contentText;
    private String contentImage;
    private String contentURL;
    private int contentOrder;
    private String contentType;

    // Constructor

    public Content() {
        // Empty constructor for manual field setting
    }

    public Content(int moduleId, String title, String text, String image, String url, int order, String type) {
        this.moduleId = moduleId;
        this.contentTitle = title;
        this.contentText = text;
        this.contentImage = image;
        this.contentURL = url;
        this.contentOrder = order;
        this.contentType = type;
    }


    // Getters
    public int getContentId() {
        return contentId;
    }

    public int getModuleId() {
        return moduleId;
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

    public String getContentType() {
        return contentType;
    }


    // Setters
    // Setters
    public void setContentId(int contentID) {
        this.contentId = contentID; // fix
    }

    public void setModuleId(int moduleID) {
        this.moduleId = moduleID; // fix
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

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public static Content fromCursor(Cursor cursor) {
        Content content = new Content();
        content.setContentId(cursor.getInt(cursor.getColumnIndexOrThrow("ContentID")));
        content.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow("ModuleID")));
        content.setContentTitle(cursor.getString(cursor.getColumnIndexOrThrow("ContentTitle")));
        content.setContentText(cursor.getString(cursor.getColumnIndexOrThrow("ContentText")));
        content.setContentImage(cursor.getString(cursor.getColumnIndexOrThrow("ContentImage")));
        content.setContentURL(cursor.getString(cursor.getColumnIndexOrThrow("ContentURL")));
        content.setContentOrder(cursor.getInt(cursor.getColumnIndexOrThrow("ContentOrder")));
        content.setContentType(cursor.getString(cursor.getColumnIndexOrThrow("ContentType")));
        return content;
    }


}
