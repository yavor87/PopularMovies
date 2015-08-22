package com.yavor.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yavor.popularmovies.R;
import com.yavor.popularmovies.data.MoviesContract;

public class ReviewsAdapter extends SimpleAdapter {
    public ReviewsAdapter(Context context, ViewGroup container, View emptyView) {
        super(context, container, emptyView);
        mInflater = LayoutInflater.from(context);
    }

    private LayoutInflater mInflater;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.list_item_review, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor data) {
        // Author
        TextView authorView = (TextView) view.findViewById(R.id.item_review_author);
        String author = data.getString(data.getColumnIndex(MoviesContract.Review.AUTHOR));
        authorView.setText(author);

        // Text
        TextView reviewView = (TextView) view.findViewById(R.id.item_review_text);
        String content = data.getString(data.getColumnIndex(MoviesContract.Review.CONTENT));
        reviewView.setText(content);
    }
}
