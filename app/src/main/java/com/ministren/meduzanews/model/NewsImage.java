package com.ministren.meduzanews.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class NewsImage extends RealmObject implements Serializable {

    @SerializedName("show")
    private boolean isShown;

    @SerializedName("small_url")
    private String url;

    public boolean isShown() {
        return isShown;
    }

    public String getUrl() {
        if (url.startsWith("https://meduza.io")) {
            return url;
        } else {
            return "https://meduza.io" + url;
        }
    }

}
