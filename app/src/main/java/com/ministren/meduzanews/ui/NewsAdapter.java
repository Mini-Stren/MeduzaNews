package com.ministren.meduzanews.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ministren.meduzanews.R;
import com.ministren.meduzanews.model.News;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsHolder> {

    private final List<News> mNews = new ArrayList<>();
    private final OnItemClick mOnItemClick;

    public NewsAdapter(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_news, parent, false);
        return new NewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        News news = mNews.get(position);
        holder.bind(news, mOnItemClick);
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public void addNews(@NonNull List<News> newsList) {
        int size = mNews.size();
        for (News news : newsList) {
            boolean notFind = true;
            for (News n : mNews) {
                if (n.getUrl().equals(news.getUrl())) {
                    notFind = false;
                }
            }
            if (notFind) {
                mNews.add(news);
            }
        }
        if (size != mNews.size()) {
            sortNews();
            notifyItemRangeInserted(size, mNews.size() - size + 1);
        }
    }

    public void resetNews(@NonNull List<News> newsList) {
        int size = mNews.size();
        mNews.clear();
        notifyItemRangeRemoved(0, size);
        mNews.addAll(newsList);
        sortNews();
        notifyItemRangeInserted(0, mNews.size());
    }

    public interface OnItemClick {
        void onItemClick(@NonNull News news);
    }

    private void sortNews() {
        Collections.sort(mNews, (news1, news2) -> {
            if (news1.getPublishedTime() < news2.getPublishedTime()) {
                return 1;
            } else if (news1.getPublishedTime() > news2.getPublishedTime()) {
                return -1;
            } else {
                return 0;
            }
        });
    }

}
