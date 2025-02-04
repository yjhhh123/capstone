package com.oneconnect.techblax.OneConnectActivities;

public class URL_PrewindowData {
    private String title;
    private String description;
    private String imageUrl;

    public URL_PrewindowData(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

