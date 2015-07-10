package com.yavor.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class MoviesFragment extends Fragment {
    public MoviesFragment() {
    }

    private MovieArrayAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        mAdapter = new MovieArrayAdapter(this.getActivity());

        GridView gridView = (GridView)rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        return rootView;
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
        new FetchMoviesTask(new FetchMoviesTask.FetchMoviesCompletionListener() {
            @Override
            public void fetchMoviesCompleted(MovieInfo[] movies) {
                mAdapter.reset(movies);
            }
        }).execute(sort_pref);
    }

    private class MovieArrayAdapter extends BaseAdapter {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final Context mContext;
        private final ArrayList<MovieInfo> mItemsArrayList;

        public MovieArrayAdapter(Context context) {
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
            TextView rowView;
            if (convertView != null)
            {
                rowView = (TextView) convertView;
            }
            else {
                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                rowView = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            rowView.setText(mItemsArrayList.get(position).getPopularity() + "");

            return rowView;
        }
    }
}
