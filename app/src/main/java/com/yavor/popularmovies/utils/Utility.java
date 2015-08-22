package com.yavor.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yavor.popularmovies.R;

public class Utility {
    public static final String FAVOURITES = "is_favourite";

    public static String getPreferredSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String preference = prefs.getString(context.getResources().getString(R.string.pref_sort_key),
                context.getResources().getString(R.string.pref_sort_popularity));

        if (preference.equals(context.getString(R.string.pref_filter_favourite)))
            return FAVOURITES;
        return preference;
    }
}
