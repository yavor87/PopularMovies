package com.yavor.popularmovies;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.yavor.popularmovies.adapters.MoviesListAdapter;
import com.yavor.popularmovies.data.MoviesContract;
import com.yavor.popularmovies.services.MoviesService;
import com.yavor.popularmovies.utils.MovieDBUtils;
import com.yavor.popularmovies.utils.Utility;

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public MoviesFragment() {
    }

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private static final int MOVIES_LOADER = 1;
    private static String[] PROJECTION = new String[] {
            MoviesContract.Movie._ID,
            MoviesContract.Movie.POSTER_PATH,
            MoviesContract.Movie.TITLE,
            MoviesContract.Movie.POPULARITY,
            MoviesContract.Movie.VOTE_AVERAGE
    };

    public static final int MOVIE_ID = 0;
    public static final int MOVIE_POSTER_PATH = 1;
    public static final int MOVIE_TITLE = 2;

    private static final String SELECTED_POSITION_ARG = "selected_position";

    private MoviesListAdapter mAdapter;
    private OnMovieSelectedListener mListener;
    private String mSortOrder;
    private int mSelectedPosition = -1;
    private GridView mGridView;
    private boolean mShouldSelectFirst;

    public boolean getShouldSelectFirst() {
        return mShouldSelectFirst;
    }

    public void setShouldSelectFirst(boolean shouldSelectFirst) {
        this.mShouldSelectFirst = shouldSelectFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSortOrder = Utility.getPreferredSortOrder(getActivity());
        if (savedInstanceState != null) {
            mSelectedPosition = savedInstanceState.getInt(SELECTED_POSITION_ARG);
        } else {
            updateMovies();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        mAdapter = new MoviesListAdapter(this.getActivity(), null, 0);

        mGridView = (GridView)rootView.findViewById(R.id.grid_view);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPosition = position;
                if (mListener != null) {
                    Uri selectedMovie = ContentUris.withAppendedId(MoviesContract.Movie.CONTENT_URI, id);
                    mListener.movieSelected(selectedMovie);
                }
            }
        });
        if (mSelectedPosition != -1) {
            mGridView.smoothScrollToPosition(mSelectedPosition);
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MoviesFragment.OnMovieSelectedListener) {
            mListener = (MoviesFragment.OnMovieSelectedListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_POSITION_ARG, mSelectedPosition);
    }

    private void updateMovies() {
        Intent updateMovies = new Intent(getActivity(), MoviesService.class);
        updateMovies.putExtra(MoviesService.QUERY_SORT_ORDER, mSortOrder);
        getActivity().startService(updateMovies);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sorting = null, filtering = null;
        if (!mSortOrder.equals(Utility.FAVOURITES)) {
            sorting = MovieDBUtils.createSortOrder(mSortOrder);
        } else {
            filtering = MoviesContract.Movie.FAVOURITE + "=1";
        }
        return new CursorLoader(getActivity(), MoviesContract.Movie.CONTENT_URI,
                PROJECTION, filtering, null, sorting);
    }

    public void onSortOrderChanged(String newSortOrder) {
        mSortOrder = newSortOrder;
        if (!newSortOrder.equals(Utility.FAVOURITES)) {
            updateMovies();
        }
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        if (mShouldSelectFirst && mSelectedPosition == -1) {
            final Uri selectedMovie = ContentUris.withAppendedId(MoviesContract.Movie.CONTENT_URI,
                    mAdapter.getItemId(0));
            mGridView.post(new Runnable() {
                @Override
                public void run() {
                    mListener.movieSelected(selectedMovie);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public interface OnMovieSelectedListener {
        void movieSelected(Uri movie);
    }
}
