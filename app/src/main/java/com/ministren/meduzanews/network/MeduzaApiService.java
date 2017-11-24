package com.ministren.meduzanews.network;

import com.ministren.meduzanews.model.NewsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MeduzaApiService {

    @GET("search?chrono=news&locale=ru&per_page=24")
    Observable<NewsResponse> getNewsPage(@Query("page") int page);

}
