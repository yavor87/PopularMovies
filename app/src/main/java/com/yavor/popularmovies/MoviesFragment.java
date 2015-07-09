package com.yavor.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
    public MoviesFragment() {
    }

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    GridLayout mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        mGridView = (GridLayout) view.findViewById(R.id.grid_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_pref = prefs.getString(getResources().getString(R.string.pref_sort_key),
                getResources().getStringArray(R.array.sort_prefs)[0]);
        new FetchMoviesTask().execute(sort_pref);
    }

    private class MoviesAdapter extends BaseAdapter {
        MoviesAdapter(Context context) {
            mContext = context;
        }

        private Context mContext;

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
