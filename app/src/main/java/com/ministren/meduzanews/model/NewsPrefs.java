package com.ministren.meduzanews.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class NewsPrefs extends RealmObject implements Serializable {

    @SerializedName("outer")
    private NewsPrefsOuter outer;

    public NewsPrefsOuter getOuter() {
        return outer;
    }

}
