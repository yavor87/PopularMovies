package com.yavor.popularmovies.utils;

import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDBUtils {
    public static final String POSTER_GET_BASAE_URL = "http://image.tmdb.org/t/p/";
    public static final String REQUEST_SIZE = "w185";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.ENGLISH);

    public static String getFullPosterUrl(String posterPath, String size) {
        return Uri.parse(POSTER_GET_BASAE_URL).buildUpon()
                .appendPath(size)
                .appendEncodedPath(posterPath)
                .toString();
    }

    public static String getFullPosterUrl(String posterPath) {
        return getFullPosterUrl(posterPath, REQUEST_SIZE);
    }

    public static String getReleaseYear(String releaseDate) {
        String year = "";
        if (releaseDate != null) {
            try {
                Date movieDate = DATE_FORMAT.parse(releaseDate);
                year = YEAR_FORMAT.format(movieDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return year;
    }
}
