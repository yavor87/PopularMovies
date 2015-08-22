package com.yavor.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yavor.popularmovies.R;

public class Utility {
    public static String getPreferedSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getResources().getString(R.string.pref_sort_key),
                context.getResources().getString(R.string.pref_sort_popularity));
    }
}
