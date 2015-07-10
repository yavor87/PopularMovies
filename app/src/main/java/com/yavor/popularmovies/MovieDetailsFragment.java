package com.yavor.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailsFragment extends Fragment {

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
        TextView nameView = (TextView) rootView.findViewById(R.id.movie_nameView);
        nameView.setText(mMovie.getTitle());

        TextView yearView = (TextView) rootView.findViewById(R.id.movie_yearView);

        TextView durationView = (TextView) rootView.findViewById(R.id.movie_durationView);

        ImageView posterView = (ImageView) rootView.findViewById(R.id.movie_posterView);
    }
}
