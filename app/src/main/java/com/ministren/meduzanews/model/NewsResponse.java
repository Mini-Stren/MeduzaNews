package com.ministren.meduzanews.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewsResponse {

    @SerializedName("has_next")
    private boolean hasNext;

    @SerializedName("collection")
    private List<String> newsLinks;

    @SerializedName("documents")
    private JsonObject newsJson;

    public boolean isHasNext() {
        return hasNext;
    }

    public List<News> getNews() {
        List<News> newsList = new ArrayList<>();
        Gson gson = new Gson();
        for (String link : newsLinks) {
            JsonElement newsElement = newsJson.get(link);
            News news = gson.fromJson(newsElement, News.class);
            if (news != null) {
                newsList.add(news);
            }
        }
        return newsList;
    }

}
