package com.ministren.meduzanews.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class NewsPrefsOuter extends RealmObject implements Serializable {

    @SerializedName("elements")
    private NewsPrefsElements elements;

    public NewsPrefsElements getElements() {
        return elements;
    }

}
