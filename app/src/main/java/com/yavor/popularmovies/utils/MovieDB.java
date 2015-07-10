package com.yavor.popularmovies.utils;

import android.net.Uri;

public class MovieDB {
    public static final String POSTER_GET_BASAE_URL = "http://image.tmdb.org/t/p/";
    public static final String REQUEST_SIZE = "w185";

    public static String getFullPosterUrl(String posterPath, String size) {
        return Uri.parse(POSTER_GET_BASAE_URL).buildUpon()
                .appendPath(size)
                .appendEncodedPath(posterPath)
                .toString();
    }

    public static String getFullPosterUrl(String posterPath) {
        return getFullPosterUrl(posterPath, REQUEST_SIZE);
    }
}
