package com.ministren.meduzanews.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class MeduzaApi {

    private static final String BASE_URL = "https://meduza.io/api/v3/";

    private static volatile MeduzaApi sInstance;

    private MeduzaApiService mService;

    private MeduzaApi() {
        if (sInstance != null) {
            throw new RuntimeException("Only single instance of this class is acceptable");
        }
        mService = buildRetrofit().create(MeduzaApiService.class);
    }

    public static MeduzaApiService getApiService() {
        return getInstance().mService;
    }

    private static MeduzaApi getInstance() {
        if (sInstance == null) {
            synchronized (MeduzaApi.class) {
                if (sInstance == null) {
                    sInstance = new MeduzaApi();
                }
            }
        }
        return sInstance;
    }

    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
