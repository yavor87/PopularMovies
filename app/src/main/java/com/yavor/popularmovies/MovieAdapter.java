package com.yavor.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MoviesAdapter extends BaseAdapter {
    public static final String POSTER_GET_BASAE_URL = "http://image.tmdb.org/t/p/";
    public static final String REQUEST_SIZE = "w185";
    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();
    private final Context mContext;
    private final ArrayList<MovieInfo> mItemsArrayList;

    public MoviesAdapter(Context context) {
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

    public void reset(MovieInfo[] movies) {
        mItemsArrayList.clear();
        for (MovieInfo m : movies) {
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

        MovieInfo currentItem = mItemsArrayList.get(position);
        String posterPath = buildPosterUrl(currentItem);
        Picasso.with(mContext)
                .load(posterPath)
                .into(rowView);

        return rowView;
    }

    private String buildPosterUrl(MovieInfo info) {
        return Uri.parse(POSTER_GET_BASAE_URL).buildUpon()
                .appendPath(REQUEST_SIZE)
                .appendEncodedPath(info.getPosterPath())
                .toString();
    }
}
