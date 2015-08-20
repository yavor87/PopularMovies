package com.yavor.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yavor.popularmovies.utils.MovieDBUtils;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
    }

    public static MovieDetailsFragment createInstance(int movieId) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(MovieDetailsActivity.MOVIE_ID_ARG, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null)
        {
            int movieId = args.getInt(MovieDetailsActivity.MOVIE_ID_ARG);
            new FetchMovieInfoTask().execute(movieId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    private void bindView(View rootView, MovieDb movie) {
        // Title
        TextView nameView = (TextView) rootView.findViewById(R.id.movie_nameView);
        nameView.setText(movie.getTitle());

        // Year
        TextView yearView = (TextView) rootView.findViewById(R.id.movie_yearView);
        String releaseYear = MovieDBUtils.getReleaseYear(movie.getReleaseDate());
        if (releaseYear != null) {
            yearView.setText(releaseYear);
        }

        // Poster
        ImageView posterView = (ImageView) rootView.findViewById(R.id.movie_posterView);
        String posterPath = MovieDBUtils.getFullPosterUrl(movie.getPosterPath());
        if (posterPath != null && posterPath.length() > 0) {
            Picasso.with(getActivity())
                    .load(posterPath)
                    .into(posterView);
        }

        // Rating
        TextView ratingView = (TextView) rootView.findViewById(R.id.ratingView);
        ratingView.setText(String.format("%.1f/10", movie.getVoteAverage()));

        // Overview
        TextView overviewView = (TextView) rootView.findViewById(R.id.overviewView);
        String overview = movie.getOverview();
        if (overview != null && overview.length() > 0) {
            overviewView.setText(overview);
        } else {
            overviewView.setText(getString(R.string.no_overview_found));
        }
    }

    private class FetchMovieInfoTask extends AsyncTask<Integer, Void, MovieDb> {
        @Override
        protected MovieDb doInBackground(Integer... params) {
            TmdbApi api = new TmdbApi(MovieDBUtils.API_KEY);
            return api.getMovies().getMovie(params[0], null,
                    TmdbMovies.MovieMethod.reviews, TmdbMovies.MovieMethod.videos);
        }

        @Override
        protected void onPostExecute(MovieDb movieDb) {
            if (movieDb != null) {
                bindView(getView(), movieDb);
            }
        }
    }
}
