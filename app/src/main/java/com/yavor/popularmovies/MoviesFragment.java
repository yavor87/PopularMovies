package com.yavor.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MoviesFragment extends Fragment {
    public MoviesFragment() {
    }

    private MovieAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        mAdapter = new MovieAdapter(this.getActivity());

        GridView gridView = (GridView)rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDb selectedMovie = (MovieDb) parent.getItemAtPosition(position);
                Intent showDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                showDetailsIntent.putExtra(MovieDetailsActivity.MOVIE_ARG, selectedMovie);
                startActivity(showDetailsIntent);
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
                getResources().getString(R.string.pref_sort_popularity));

        new FetchMoviesTask().execute(sort_pref);
    }

    class FetchMoviesTask extends AsyncTask<String, Void, List<MovieDb>> {
        private static final String API_KEY = ""; // TODO: Put in API_KEY

        @Override
        protected List<MovieDb> doInBackground(String... params) {
            String sortBy = params[0];

            TmdbApi api = new TmdbApi(API_KEY);
            Discover d = new Discover();
            d.sortBy(sortBy);
            MovieResultsPage page = api.getDiscover().getDiscover(d);
            return page.getResults();
        }

        @Override
        protected void onPostExecute(List<MovieDb> movies) {
            mAdapter.reset(movies);
        }
    }
}
