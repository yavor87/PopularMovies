package com.yavor.popularmovies.utils;

import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDBUtils {
    public static final String API_KEY = ""; // TODO: Put in API_KEY
    public static final String POSTER_GET_BASAE_URL = "http://image.tmdb.org/t/p/";
    public static final String REQUEST_SIZE = "w185";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public static String getFullPosterUrl(String posterPath, String size) {
        return Uri.parse(POSTER_GET_BASAE_URL).buildUpon()
                .appendPath(size)
                .appendEncodedPath(posterPath)
                .toString();
    }

    public static String getFullPosterUrl(String posterPath) {
        return getFullPosterUrl(posterPath, REQUEST_SIZE);
    }

    public static Date parseDate(String releaseDate) {
        if (releaseDate != null) {
            try {
                return DATE_FORMAT.parse(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
