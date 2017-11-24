package com.ministren.meduzanews.model.cache;

import com.ministren.meduzanews.model.News;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmResults;

public class NewsCacheTransformer implements ObservableTransformer<List<News>, List<News>> {

    private boolean mReload;

    private final Function<List<News>, Observable<List<News>>> mSaveFunc = news -> {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            if (mReload) {
                realm.delete(News.class);
            }
            realm.insert(news);
        });
        return Observable.just(news);
    };

    private final Function<Throwable, Observable<List<News>>> mCacheErrorHandler = throwable -> {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<News> results = realm.where(News.class).findAll();
        return Observable.just(realm.copyFromRealm(results));
    };

    public NewsCacheTransformer(boolean reload) {
        mReload = reload;
    }

    @Override
    public ObservableSource<List<News>> apply(Observable<List<News>> upstream) {
        return upstream
                .flatMap(mSaveFunc)
                .onErrorResumeNext(mCacheErrorHandler);
    }

}
