package com.yavor.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yavor.popularmovies.utils.MovieDBUtils;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Reviews;
import info.movito.themoviedbapi.model.Video;
import info.movito.themoviedbapi.tools.MovieDbException;

public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
    }

    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();

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
        Context context = getActivity();

        ImageView posterView = (ImageView) rootView.findViewById(R.id.movie_posterView);

        // Title
        TextView nameView = (TextView) rootView.findViewById(R.id.movie_name);
        String title = movie.getTitle();
        if (title != null) {
            nameView.setText(title);
            posterView.setContentDescription(title);
        }

        // Year
        TextView yearView = (TextView) rootView.findViewById(R.id.movie_year);
        String releaseYear = MovieDBUtils.getReleaseYear(movie.getReleaseDate());
        if (releaseYear != null) {
            yearView.setText(releaseYear);
        }

        // Duration
        TextView durationView = (TextView) rootView.findViewById(R.id.movie_duration);
        int runtime = movie.getRuntime();
        if (runtime > 0) {
            durationView.setText(String.format(getString(R.string.runtime_text_format), runtime));
        }

        // Poster
        String posterPath = MovieDBUtils.getFullPosterUrl(movie.getPosterPath());
        if (posterPath != null && posterPath.length() > 0) {
            Picasso.with(context)
                    .load(posterPath)
                    .into(posterView);
        }

        // Rating
        TextView ratingView = (TextView) rootView.findViewById(R.id.movie_rating);
        ratingView.setText(String.format("%.1f/10", movie.getVoteAverage()));

        // Overview
        TextView overviewView = (TextView) rootView.findViewById(R.id.movie_overview);
        String overview = movie.getOverview();
        if (overview != null && overview.length() > 0) {
            overviewView.setText(overview);
        } else {
            overviewView.setText(getString(R.string.no_overview_found));
        }

        // Trailers
        LinearLayout trailersView = (LinearLayout) rootView.findViewById(R.id.trailers_list);
        if (movie.getVideos() != null) {
            for (Video trailer : movie.getVideos()) {
                TextView trailerView = new TextView(context);
                trailerView.setText(trailer.getName());
                trailersView.addView(trailerView);
            }
        }

        // Reviews
        LinearLayout reviewsView = (LinearLayout) rootView.findViewById(R.id.reviews_list);
        if (movie.getReviews() != null) {
            for (Reviews review : movie.getReviews()) {
                TextView reviewView = new TextView(context);
                reviewView.setText(review.getContent());
                reviewsView.addView(reviewView);
            }
        }
    }

    private class FetchMovieInfoTask extends AsyncTask<Integer, Void, MovieDb> {
        @Override
        protected MovieDb doInBackground(Integer... params) {
            if (MovieDBUtils.API_KEY == null || MovieDBUtils.API_KEY == "") {
                Log.wtf(LOG_TAG, "Did you forget to set api key?");
                return null;
            }
            try {
                TmdbApi api = new TmdbApi(MovieDBUtils.API_KEY);
                return api.getMovies().getMovie(params[0], null,
                        TmdbMovies.MovieMethod.reviews, TmdbMovies.MovieMethod.videos);
            } catch (MovieDbException e) {
                Log.e(LOG_TAG, "Unable to get movie details", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieDb movieDb) {
            if (movieDb != null) {
                bindView(getView(), movieDb);
            }
        }
    }
}
