package com.yavor.popularmovies;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.yavor.popularmovies.data.MoviesContract;
import com.yavor.popularmovies.services.MoviesService;

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public MoviesFragment() {
    }

    private MoviesListAdapter mAdapter;
    private OnMovieSelectedListener mListener;
    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private static final int MOVIES_LOADER = 1;

    private static String[] PROJECTION = new String[] {
            MoviesContract.Movie._ID,
            MoviesContract.Movie.POSTER_PATH,
            MoviesContract.Movie.TITLE
    };

    static int MOVIE_ID = 0;
    static int MOVIE_POSTER_PATH = 1;
    static int MOVIE_TITLE = 2;

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

        GridView gridView = (GridView)rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    Uri selectedMovie = ContentUris.withAppendedId(MoviesContract.Movie.CONTENT_URI, id);
                    mListener.movieSelected(selectedMovie);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
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

    private void updateMovies() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_pref = prefs.getString(getResources().getString(R.string.pref_sort_key),
                getResources().getString(R.string.pref_sort_popularity));

        Intent updateMovies = new Intent(getActivity(), MoviesService.class);
        updateMovies.putExtra(MoviesService.QUERY_SORT_ORDER, sort_pref);
        getActivity().startService(updateMovies);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MoviesContract.Movie.CONTENT_URI,
                PROJECTION, null, null, null); // TODO: Add sorting and filtering
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public interface OnMovieSelectedListener {
        void movieSelected(Uri movie);
    }
}
