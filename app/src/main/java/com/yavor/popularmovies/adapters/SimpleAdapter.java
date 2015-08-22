package com.yavor.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

public abstract class SimpleAdapter {
    public SimpleAdapter(Context context, ViewGroup container, View emptyView) {
        mContext = context;
        mContainer = container;
        mEmptyView = emptyView;
    }

    private Context mContext;
    private ViewGroup mContainer;
    private View mEmptyView;

    public void swapCursor(Cursor data) {
        if (data != null && data.getCount() > 0) {
            while (data.moveToNext()) {
                View view = getViewForCursor(data);
                bindView(view, mContext, data);
            }
            mEmptyView.setVisibility(View.GONE);
            mContainer.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mContainer.setVisibility(View.GONE);
        }
    }

    public abstract View newView(Context context, Cursor cursor, ViewGroup parent);

    public abstract void bindView(View view, Context context, Cursor cursor);

    private View getViewForCursor(Cursor data) {
        int position = data.getPosition();
        View v;
        if (position < mContainer.getChildCount()) {
            v = mContainer.getChildAt(position);
        } else {
            v = newView(mContext, data, mContainer);
            mContainer.addView(v);
        }
        return v;
    }
}
