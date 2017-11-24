package com.ministren.meduzanews.helpers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerViewWithEmptyView extends RecyclerView {

    private View mEmptyView;

    private AdapterDataObserver mEmptyAdapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter == null || mEmptyView == null) {
                super.onChanged();
                return;
            }

            if (adapter.getItemCount() == 0) {
                if (mEmptyView.getVisibility() != VISIBLE) {
                    mEmptyView.setAlpha(0);
                    mEmptyView.setVisibility(VISIBLE);
                    mEmptyView.animate()
                            .alpha(1)
                            .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime))
                            .setListener(null);
                }
            } else if (mEmptyView.getVisibility() != GONE) {
                mEmptyView.animate()
                        .alpha(0)
                        .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mEmptyView.setVisibility(GONE);
                            }
                        });
            }
        }
    };

    public RecyclerViewWithEmptyView(Context context) {
        super(context);
    }

    public RecyclerViewWithEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewWithEmptyView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(mEmptyAdapterDataObserver);
        }

        mEmptyAdapterDataObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

}
