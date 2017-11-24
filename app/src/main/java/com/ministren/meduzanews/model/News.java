package com.ministren.meduzanews.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class News extends RealmObject {

    @Required
    @PrimaryKey
    @SerializedName("url")
    private String url;

    @Required
    @SerializedName("title")
    private String title;

    @SerializedName("second_title")
    private String secondTitle;

    @SerializedName("published_at")
    private long publishedTime;

    @SerializedName("prefs")
    private NewsPrefs prefs;

    public String getUrl() {
        if (url.startsWith("https://meduza.io/")) {
            return url;
        } else {
            return "https://meduza.io/" + url;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getSecondTitle() {
        return secondTitle;
    }

    public long getPublishedTime() {
        return publishedTime;
    }

    public NewsPrefs getPrefs() {
        return prefs;
    }

}
