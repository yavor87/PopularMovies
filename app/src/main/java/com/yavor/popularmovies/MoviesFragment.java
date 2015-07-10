package com.yavor.popularmovies;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class MoviesFragment extends Fragment {
    public MoviesFragment() {
    }

    private ArrayAdapter<String> moviesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        this.moviesAdapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        GridView gridView = (GridView)rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(moviesAdapter);
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
                moviesAdapter.clear();
                for (MovieInfo m : movies) {
                    moviesAdapter.add(m.getTitle());

                }
//            mAdapter.addAdd(movieInfo);
            }
        }).execute(sort_pref);
    }
}
