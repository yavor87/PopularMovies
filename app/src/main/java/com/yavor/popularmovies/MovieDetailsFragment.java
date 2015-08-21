package com.yavor.popularmovies;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.yavor.popularmovies.data.MoviesContract;
import com.yavor.popularmovies.utils.MovieDBUtils;
import com.yavor.popularmovies.views.YoutubePlayView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public MovieDetailsFragment() {
    }

    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    public static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    private static final int DETAILS_LOADER = 2;
    private static final int REVIEWS_LOADER = 3;
    private static final int TRAILERS_LOADER = 4;
    private Uri mMovieUri;

    public static MovieDetailsFragment createInstance(Uri movieUri) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(MovieDetailsActivity.MOVIE_ARG, movieUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null)
        {
            mMovieUri = args.getParcelable(MovieDetailsActivity.MOVIE_ARG);
            //new FetchMovieInfoTask().execute(movieId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAILS_LOADER, null, this);
        getLoaderManager().initLoader(TRAILERS_LOADER, null, this);
        getLoaderManager().initLoader(REVIEWS_LOADER, null, this);
    }

    private void bindView(Cursor data) {
        if (!data.moveToFirst()) { return; }

        Context context = getActivity();
        View rootView = getView();

        // Favourite
        boolean isFavourite = false; // TODO: Get favourite value
        ToggleButton favouriteButton = (ToggleButton) rootView.findViewById(R.id.movie_favourite);
        favouriteButton.setChecked(isFavourite);
        favouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onToggleFavourite(isChecked);
            }
        });

        ImageView posterView = (ImageView) rootView.findViewById(R.id.movie_posterView);

        // Title
        TextView nameView = (TextView) rootView.findViewById(R.id.movie_name);
        String title = data.getString(data.getColumnIndex(MoviesContract.Movie.TITLE));
        if (title != null) {
            nameView.setText(title);
            posterView.setContentDescription(title);
        }

        // Year
        TextView yearView = (TextView) rootView.findViewById(R.id.movie_year);
        Date releaseDate = new Date(data.getLong(data.getColumnIndex(MoviesContract.Movie.RELEASEDATE)));
        yearView.setText(YEAR_FORMAT.format(releaseDate));

        // Duration
        TextView durationView = (TextView) rootView.findViewById(R.id.movie_duration);
        int runtime = data.getInt(data.getColumnIndex(MoviesContract.Movie.RUNTIME));
        if (runtime > 0) {
            durationView.setText(String.format(getString(R.string.runtime_text_format), runtime));
        }

        // PosterposterView
        String posterPath = data.getString(data.getColumnIndex(MoviesContract.Movie.POSTERPATH));
        if (posterPath != null && posterPath.length() > 0) {
            String fullPosterPath = MovieDBUtils.getFullPosterUrl(posterPath);
            Picasso.with(context)
                    .load(fullPosterPath)
                    .into(posterView);
        }

        // Rating
        TextView ratingView = (TextView) rootView.findViewById(R.id.movie_rating);
        ratingView.setText(String.format("%.1f/10", data.getFloat(data.getColumnIndex(MoviesContract.Movie.VOTEAVERAGE))));

        // Overview
        TextView overviewView = (TextView) rootView.findViewById(R.id.movie_overview);
        String overview = data.getString(data.getColumnIndex(MoviesContract.Movie.OVERVIEW));
        if (overview != null && overview.length() > 0) {
            overviewView.setText(overview);
        } else {
            overviewView.setText(getString(R.string.no_overview_found));
        }
    }

    private void bindTrailers(Cursor data) {
        View rootView = getView();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View trailersContainer = rootView.findViewById(R.id.trailers_container);
        LinearLayout trailersList = (LinearLayout) trailersContainer.findViewById(R.id.trailers_list);
        View emptyTrailers = trailersContainer.findViewById(R.id.trailers_empty);
        if (!data.isAfterLast()) {
            while (data.moveToNext()) {
                View view = inflater.inflate(R.layout.list_item_trailer, trailersList, false);

                // Trailer video
                String site = data.getString(data.getColumnIndex(MoviesContract.Trailer.SITE));
                String key = data.getString(data.getColumnIndex(MoviesContract.Trailer.KEY));
                YoutubePlayView playView = (YoutubePlayView) view.findViewById(R.id.item_trailer_play);
                Uri uri = YoutubePlayView.createVideoPath(site, key);
                if (uri != null) {
                    playView.setVideoPath(uri);
                }

                // Trailer name
                String name = data.getString(data.getColumnIndex(MoviesContract.Trailer.NAME));
                TextView trailerView = (TextView) view.findViewById(R.id.item_trailer_name);
                trailerView.setText(name);

                trailersList.addView(view);
            }
            emptyTrailers.setVisibility(View.GONE);
            trailersList.setVisibility(View.VISIBLE);
        } else {
            emptyTrailers.setVisibility(View.VISIBLE);
            trailersList.setVisibility(View.GONE);
        }
    }

    private void bindReviews(Cursor data) {
        View rootView = getView();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View reviewsContainer = rootView.findViewById(R.id.reviews_container);
        LinearLayout reviewsList = (LinearLayout) reviewsContainer.findViewById(R.id.reviews_list);
        View emptyReviews = reviewsContainer.findViewById(R.id.reviews_empty);
        if (!data.isAfterLast()) {
            while (data.moveToNext()) {
                View view = inflater.inflate(R.layout.list_item_review, reviewsList, false);

                // Author
                TextView authorView = (TextView) view.findViewById(R.id.item_review_author);
                String author = data.getString(data.getColumnIndex(MoviesContract.Review.AUTHOR));
                authorView.setText(author);

                // Text
                TextView reviewView = (TextView) view.findViewById(R.id.item_review_text);
                String content = data.getString(data.getColumnIndex(MoviesContract.Review.CONTENT));
                reviewView.setText(content);

                reviewsList.addView(view);
            }
            emptyReviews.setVisibility(View.GONE);
            reviewsList.setVisibility(View.VISIBLE);
        } else {
            emptyReviews.setVisibility(View.VISIBLE);
            reviewsList.setVisibility(View.GONE);
        }
    }

    private void onToggleFavourite(boolean isFavourite) {
        Log.v(LOG_TAG, "Toggle favourite");
        // TODO: Implement favourite
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mMovieUri != null) {
            // TODO: Add projections
            switch (id) {
                case DETAILS_LOADER:
                    return new CursorLoader(getActivity(), mMovieUri, null, null, null, null);
                case REVIEWS_LOADER:
                    // TODO: Think of uri
                    return new CursorLoader(getActivity(), MoviesContract.Review.CONTENT_URI,
                            null, MoviesContract.Review.MOVIE_ID + "=?",
                            new String[] {String.valueOf(ContentUris.parseId(mMovieUri))}, null);
                case TRAILERS_LOADER:
                    // TODO: Think of uri
                    return new CursorLoader(getActivity(), MoviesContract.Trailer.CONTENT_URI,
                            null, MoviesContract.Review.MOVIE_ID + "=?",
                            new String[] {String.valueOf(ContentUris.parseId(mMovieUri))}, null);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case DETAILS_LOADER:
                bindView(data);
                break;
            case REVIEWS_LOADER:
                bindReviews(data);
                break;
            case TRAILERS_LOADER:
                bindTrailers(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
