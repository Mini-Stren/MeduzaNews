package com.ministren.meduzanews.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class NewsPrefsElements extends RealmObject implements Serializable {

    @SerializedName("tag")
    private NewsTag tag;

    @SerializedName("image")
    private NewsImage image;

    public NewsTag getTag() {
        return tag;
    }

    public NewsImage getImage() {
        return image;
    }

}
