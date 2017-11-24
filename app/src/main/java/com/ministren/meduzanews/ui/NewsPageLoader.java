package com.ministren.meduzanews.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import com.ministren.meduzanews.model.News;
import com.ministren.meduzanews.model.NewsResponse;
import com.ministren.meduzanews.model.cache.NewsCacheTransformer;
import com.ministren.meduzanews.network.MeduzaApi;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsPageLoader extends Loader<List<News>> {

    private final Observable<NewsResponse> mNewsPageObservable;

    @Nullable
    private List<News> mNewsList;
    private boolean mReload;

    NewsPageLoader(@NonNull Context context, int newsPage, boolean reload) {
        super(context);
        mNewsPageObservable = MeduzaApi.getApiService().getNewsPage(newsPage);
        mReload = reload;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mNewsList != null) {
            deliverResult(mNewsList);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mNewsPageObservable
                .map(NewsResponse::getNews)
                .compose(new NewsCacheTransformer(mReload))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newsPage -> {
                            mNewsList = newsPage;
                            deliverResult(mNewsList);
                        },
                        throwable -> deliverResult(null)
                );
    }

}
