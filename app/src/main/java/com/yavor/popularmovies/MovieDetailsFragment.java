package com.yavor.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yavor.popularmovies.utils.MovieDB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDetailsFragment extends Fragment {
    public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

    public MovieDetailsFragment() {
    }

    MovieInfo mMovie;

    public static MovieDetailsFragment createInstance(MovieInfo movie) {
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
            mMovie = (MovieInfo) args.getSerializable(MovieDetailsActivity.MOVIE_ARG);
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
        Date movieDate = mMovie.getReleaseDate();
        yearView.setText(yearFormat.format(movieDate));

        // Duration
        TextView durationView = (TextView) rootView.findViewById(R.id.movie_durationView);

        // Poster
        ImageView posterView = (ImageView) rootView.findViewById(R.id.movie_posterView);
        String posterPath = MovieDB.getFullPosterUrl(mMovie.getPosterPath());
        Picasso.with(getActivity())
                .load(posterPath)
                .into(posterView);

        // Rating
        TextView ratingView = (TextView) rootView.findViewById(R.id.ratingView);
        ratingView.setText(String.format("%.1f/10", mMovie.getVoteAverage()));

        // Overview
        TextView overviewView = (TextView) rootView.findViewById(R.id.overviewView);
        overviewView.setText(mMovie.getOverview());
    }
}
