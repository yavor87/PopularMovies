package com.yavor.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yavor.popularmovies.utils.MovieDBUtils;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
    }

    MovieDb mMovie;

    public static MovieDetailsFragment createInstance(MovieDb movie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(MovieDetailsActivity.MOVIE_ARG, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null)
        {
            mMovie = (MovieDb) args.getSerializable(MovieDetailsActivity.MOVIE_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        bindView(rootView);
        return rootView;
    }

    private void bindView(View rootView) {
        // Title
        TextView nameView = (TextView) rootView.findViewById(R.id.movie_nameView);
        nameView.setText(mMovie.getTitle());

        // Year
        TextView yearView = (TextView) rootView.findViewById(R.id.movie_yearView);
        String releaseYear = MovieDBUtils.getReleaseYear(mMovie.getReleaseDate());
        if (releaseYear != null) {
            yearView.setText(releaseYear);
        }

        // Poster
        ImageView posterView = (ImageView) rootView.findViewById(R.id.movie_posterView);
        String posterPath = MovieDBUtils.getFullPosterUrl(mMovie.getPosterPath());
        if (posterPath != null && posterPath.length() > 0) {
            Picasso.with(getActivity())
                    .load(posterPath)
                    .into(posterView);
        }

        // Rating
        TextView ratingView = (TextView) rootView.findViewById(R.id.ratingView);
        ratingView.setText(String.format("%.1f/10", mMovie.getVoteAverage()));

        // Overview
        TextView overviewView = (TextView) rootView.findViewById(R.id.overviewView);
        String overview = mMovie.getOverview();
        if (overview != null && overview.length() > 0) {
            overviewView.setText(overview);
        } else {
            overviewView.setText(getString(R.string.no_overview_found));
        }
    }
}
