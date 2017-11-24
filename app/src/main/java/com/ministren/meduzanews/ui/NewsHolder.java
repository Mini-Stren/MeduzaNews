package com.ministren.meduzanews.ui;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ministren.meduzanews.R;
import com.ministren.meduzanews.model.News;
import com.ministren.meduzanews.model.NewsImage;
import com.ministren.meduzanews.model.NewsPrefsElements;
import com.ministren.meduzanews.model.NewsTag;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.news_main_container)
    View mMainContainer;
    @BindView(R.id.news_image)
    ImageView mImage;
    @BindView(R.id.news_image_progress)
    ProgressBar mImageProgressBar;
    @BindView(R.id.news_tag)
    TextView mTvTag;
    @BindView(R.id.news_title)
    TextView mTvTitle;
    @BindView(R.id.news_second_title)
    TextView mTvSecondTitle;
    @BindView(R.id.news_time)
    TextView mTvTime;

    public NewsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull News news, @Nullable NewsAdapter.OnItemClick onItemClick) {
        mTvTitle.setText(news.getTitle());
        bindSecondTitle(news.getSecondTitle());
        bindPublishedTime(news.getPublishedTime());

        NewsPrefsElements elements = news.getPrefs().getOuter().getElements();
        bindImage(elements.getImage());
        bindTag(elements.getTag());

        if (onItemClick != null) {
            mMainContainer.setOnClickListener(view -> onItemClick.onItemClick(news));
        }
    }

    private void bindSecondTitle(String secondTitle) {
        if (secondTitle != null && !secondTitle.isEmpty()) {
            mTvSecondTitle.setVisibility(View.VISIBLE);
            mTvSecondTitle.setText(secondTitle);
        } else {
            mTvSecondTitle.setVisibility(View.GONE);
        }
    }

    private void bindPublishedTime(long publishedTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date date = new Date(publishedTime * 1000L);
        mTvTime.setText(dateFormat.format(date));
    }

    private void bindImage(NewsImage image) {
        if (image.isShown() && image.getUrl() != null) {
            mImage.setVisibility(View.VISIBLE);
            mImageProgressBar.setVisibility(View.VISIBLE);

            Callback callback = new Callback() {
                @Override
                public void onSuccess() {
                    mImageProgressBar.setVisibility(View.GONE);
                    mTvTitle.setTypeface(Typeface.DEFAULT_BOLD);
                }

                @Override
                public void onError() {
                    mImage.setVisibility(View.GONE);
                    mImageProgressBar.setVisibility(View.GONE);
                    mTvTitle.setTypeface(Typeface.DEFAULT);
                }
            };

            Picasso.with(itemView.getContext())
                    .load(image.getUrl())
                    .fit().centerCrop()
                    .into(mImage, callback);
        } else {
            mImage.setVisibility(View.GONE);
            mImageProgressBar.setVisibility(View.GONE);
            mTvTitle.setTypeface(Typeface.DEFAULT);
        }
    }

    private void bindTag(NewsTag tag) {
        if (tag.isShown()) {
            mTvTag.setVisibility(View.VISIBLE);
            mTvTag.setText(tag.getName());
        } else {
            mTvTag.setVisibility(View.GONE);
        }
    }

}
