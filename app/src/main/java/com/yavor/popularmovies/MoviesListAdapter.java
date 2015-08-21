package com.yavor.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yavor.popularmovies.utils.MovieDBUtils;

public class MoviesListAdapter extends CursorAdapter {
    public MoviesListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ImageView view = new ImageView(context);
        view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        view.setAdjustViewBounds(true);
        view.setLayoutParams(new GridView.LayoutParams(
                GridView.LayoutParams.MATCH_PARENT,
                GridView.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView rowView = (ImageView) view;

        String title = cursor.getString(MoviesFragment.MOVIE_TITLE);
        rowView.setContentDescription(title);

        String posterPath = cursor.getString(MoviesFragment.MOVIE_POSTER_PATH);
        String fullPosterPath = MovieDBUtils.getFullPosterUrl(posterPath);
        Picasso.with(context).load(fullPosterPath).into(rowView);
    }
}
