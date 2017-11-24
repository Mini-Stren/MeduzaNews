package com.ministren.meduzanews.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class NewsTag extends RealmObject implements Serializable {

    @SerializedName("show")
    private boolean isShown;

    @SerializedName("name")
    private String name;

    public boolean isShown() {
        return isShown;
    }

    public String getName() {
        return name;
    }

}
