package com.yavor.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.yavor.popularmovies.adapters.ReviewsAdapter;
import com.yavor.popularmovies.adapters.SimpleAdapter;
import com.yavor.popularmovies.adapters.TrailersAdapter;
import com.yavor.popularmovies.data.MoviesContract;
import com.yavor.popularmovies.utils.MovieDBUtils;
import com.yavor.popularmovies.views.YoutubePlayView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public MovieDetailsFragment() {
        setHasOptionsMenu(true);
    }

    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    public static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    private static final int DETAILS_LOADER = 2;
    private static final int REVIEWS_LOADER = 3;
    private static final int TRAILERS_LOADER = 4;
    private static final String SHARE_HASHTAG = "#PopularMovies";
    private Uri mMovieUri;
    private ReviewsAdapter mReviewAdapter;
    private TrailersAdapter mTrailerAdapter;
    private ShareActionProvider mShareActionProvider;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        mReviewAdapter = new ReviewsAdapter(getActivity(), (ViewGroup) rootView.findViewById(R.id.reviews_list),
                rootView.findViewById(R.id.reviews_empty));
        mTrailerAdapter = new TrailersAdapter(getActivity(), (ViewGroup) rootView.findViewById(R.id.trailers_list),
                rootView.findViewById(R.id.trailers_empty));
        mTrailerAdapter.setCallback(new SimpleAdapter.ViewBoundCallback() {
            @Override
            public void onViewBound(View view, Cursor data) {
                if (data.getPosition() == 0) {
                    String site = data.getString(data.getColumnIndex(MoviesContract.Trailer.SITE));
                    String key = data.getString(data.getColumnIndex(MoviesContract.Trailer.KEY));
                    Uri uri = YoutubePlayView.createVideoPath(site, key);
                    mShareActionProvider.setShareIntent(createShareTrailerIntent(uri.toString()));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
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
        boolean isFavourite = data.getShort(data.getColumnIndex(MoviesContract.Movie.FAVOURITE)) > 0;
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
        Date releaseDate = new Date(data.getLong(data.getColumnIndex(MoviesContract.Movie.RELEASE_DATE)));
        yearView.setText(YEAR_FORMAT.format(releaseDate));

        // Duration
        TextView durationView = (TextView) rootView.findViewById(R.id.movie_duration);
        int runtime = data.getInt(data.getColumnIndex(MoviesContract.Movie.RUNTIME));
        if (runtime > 0) {
            durationView.setText(String.format(getString(R.string.runtime_text_format), runtime));
        }

        // Poster
        String posterPath = data.getString(data.getColumnIndex(MoviesContract.Movie.POSTER_PATH));
        if (posterPath != null && posterPath.length() > 0) {
            String fullPosterPath = MovieDBUtils.getFullPosterUrl(posterPath);
            Picasso.with(context)
                    .load(fullPosterPath)
                    .into(posterView);
        }

        // Rating
        TextView ratingView = (TextView) rootView.findViewById(R.id.movie_rating);
        ratingView.setText(String.format("%.1f/10", data.getFloat(data.getColumnIndex(MoviesContract.Movie.VOTE_AVERAGE))));

        // Overview
        TextView overviewView = (TextView) rootView.findViewById(R.id.movie_overview);
        String overview = data.getString(data.getColumnIndex(MoviesContract.Movie.OVERVIEW));
        if (overview != null && overview.length() > 0) {
            overviewView.setText(overview);
        } else {
            overviewView.setText(getString(R.string.no_overview_found));
        }
    }

    private Intent createShareTrailerIntent(String trailer) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String content = String.format("Check out %s %s", trailer, SHARE_HASHTAG);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        return shareIntent;
    }

    private void onToggleFavourite(boolean isFavourite) {
        Log.v(LOG_TAG, "Toggle favourite");
        ContentValues values = new ContentValues();
        values.put(MoviesContract.Movie.FAVOURITE, isFavourite ? 1 : 0);
        getActivity().getContentResolver().update(mMovieUri, values, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mMovieUri != null) {
            switch (id) {
                case DETAILS_LOADER:
                    return new CursorLoader(getActivity(), mMovieUri, null, null, null, null);
                case REVIEWS_LOADER:
                    return new CursorLoader(getActivity(), MoviesContract.Review.CONTENT_URI,
                            null, MoviesContract.Review.MOVIE_ID + "=?",
                            new String[] {String.valueOf(ContentUris.parseId(mMovieUri))}, null);
                case TRAILERS_LOADER:
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
                mReviewAdapter.swapCursor(data);
                break;
            case TRAILERS_LOADER:
                mTrailerAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case REVIEWS_LOADER:
                mReviewAdapter.swapCursor(null);
                break;
            case TRAILERS_LOADER:
                mTrailerAdapter.swapCursor(null);
                break;
        }
    }
}
