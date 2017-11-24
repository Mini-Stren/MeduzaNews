package com.ministren.meduzanews.ui;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ministren.meduzanews.R;
import com.ministren.meduzanews.helpers.RecyclerViewEndlessScrollListener;
import com.ministren.meduzanews.helpers.RecyclerViewWithEmptyView;
import com.ministren.meduzanews.model.News;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsListActivity extends AppCompatActivity implements NewsAdapter.OnItemClick {

    private static final String LOADED_PAGES_COUNTER_KEY = "loaded_pages_counter_key";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recyclerView)
    RecyclerViewWithEmptyView mRecyclerView;

    @BindView(R.id.empty)
    View mEmptyView;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private NewsAdapter mNewsAdapter;
    private RecyclerViewEndlessScrollListener mRvEndlessScrollListener;
    private int mLoadedPagesCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (savedInstanceState != null && savedInstanceState.containsKey(LOADED_PAGES_COUNTER_KEY)) {
            mLoadedPagesCounter = savedInstanceState.getInt(LOADED_PAGES_COUNTER_KEY);
        } else {
            mLoadedPagesCounter = 1;
        }

        mNewsAdapter = new NewsAdapter(this);
        mSwipeRefreshLayout.setOnRefreshListener(this::reloadFromStart);

        LinearLayoutManager rvLayoutManager = new LinearLayoutManager(this);
        mRvEndlessScrollListener = new RecyclerViewEndlessScrollListener(rvLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNewsPage(true, ++mLoadedPagesCounter);
            }
        };

        mRecyclerView.setLayoutManager(rvLayoutManager);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mNewsAdapter);
        mRecyclerView.addOnScrollListener(mRvEndlessScrollListener);

        load(mLoadedPagesCounter, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LOADED_PAGES_COUNTER_KEY, mLoadedPagesCounter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload_news:
                reloadFromStart();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(@NonNull News news) {
        String url = news.getUrl();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void reloadFromStart() {
        mLoadedPagesCounter = 1;
        load(mLoadedPagesCounter, true);
        mRvEndlessScrollListener.resetState();
    }

    private void load(int newsPages, boolean restart) {
        mSwipeRefreshLayout.setRefreshing(true);
        for (int i = 0; i < newsPages; i++) {
            loadNewsPage(restart, i);
        }
    }

    private void loadNewsPage(boolean restart, int newsPage) {
        boolean reloadNews = restart && mLoadedPagesCounter == 1;
        LoaderManager.LoaderCallbacks<List<News>> callbacks = new NewsPageCallbacks(newsPage, reloadNews);
        if (restart) {
            getSupportLoaderManager().restartLoader(newsPage, Bundle.EMPTY, callbacks);
        } else {
            getSupportLoaderManager().initLoader(newsPage, Bundle.EMPTY, callbacks);
        }
    }

    private void showNews(List<News> news) {
        mSwipeRefreshLayout.setRefreshing(false);

        if (news.isEmpty()) {
            return;
        }

        if (mLoadedPagesCounter == 1) {
            mNewsAdapter.resetNews(news);
        } else {
            mNewsAdapter.addNews(news);
        }
    }

    private class NewsPageCallbacks implements LoaderManager.LoaderCallbacks<List<News>> {

        private int newsPage;
        private boolean reload;

        NewsPageCallbacks(int newsPage, boolean reload) {
            this.newsPage = newsPage;
            this.reload = reload;
        }

        @Override
        public Loader<List<News>> onCreateLoader(int id, Bundle args) {
            return new NewsPageLoader(NewsListActivity.this, newsPage, reload);
        }

        @Override
        public void onLoadFinished(Loader<List<News>> loader, List<News> newsPage) {
            showNews(newsPage);
        }

        @Override
        public void onLoaderReset(Loader<List<News>> loader) {

        }

    }

}
