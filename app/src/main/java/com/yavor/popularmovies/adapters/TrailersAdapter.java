package com.yavor.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yavor.popularmovies.R;
import com.yavor.popularmovies.data.MoviesContract;
import com.yavor.popularmovies.views.YoutubePlayView;

public class TrailersAdapter extends SimpleAdapter {
    public TrailersAdapter(Context context, ViewGroup container, View emptyView) {
        super(context, container, emptyView);
        mInflater = LayoutInflater.from(context);
    }

    private LayoutInflater mInflater;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.list_item_trailer, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor data) {
        // Trailer video
        String site = data.getString(data.getColumnIndex(MoviesContract.Trailer.SITE));
        String key = data.getString(data.getColumnIndex(MoviesContract.Trailer.KEY));
        YoutubePlayView playView = (YoutubePlayView) view.findViewById(R.id.item_trailer_play);
        Uri uri = YoutubePlayView.createVideoPath(site, key);
        if (uri != null) {
            playView.setVideoPath(uri);
        }

        // Trailer name
        String name = data.getString(data.getColumnIndex(MoviesContract.Trailer.NAME));
        TextView trailerView = (TextView) view.findViewById(R.id.item_trailer_name);
        trailerView.setText(name);
    }
}
