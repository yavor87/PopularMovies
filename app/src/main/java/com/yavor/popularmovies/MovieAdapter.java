package com.yavor.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yavor.popularmovies.utils.MovieDBUtils;

import java.util.ArrayList;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieAdapter extends BaseAdapter {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private final Context mContext;
    private final ArrayList<MovieDb> mItemsArrayList;

    public MovieAdapter(Context context) {
        mContext = context;
        mItemsArrayList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mItemsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void reset(Iterable<MovieDb> movies) {
        if (movies == null)
            return;
        
        mItemsArrayList.clear();
        for (MovieDb m : movies) {
            mItemsArrayList.add(m);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(LOG_TAG, "getView #" + position);
        ImageView rowView;
        if (convertView != null) {
            rowView = (ImageView) convertView;
        } else {
            rowView = new ImageView(mContext);
            rowView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            rowView.setAdjustViewBounds(true);
            rowView.setLayoutParams(new GridView.LayoutParams(
                    GridView.LayoutParams.MATCH_PARENT,
                    GridView.LayoutParams.MATCH_PARENT));
        }

        MovieDb currentItem = mItemsArrayList.get(position);
        String posterPath = MovieDBUtils.getFullPosterUrl(currentItem.getPosterPath());
        Picasso.with(mContext)
                .load(posterPath)
                .into(rowView);

        return rowView;
    }
}
