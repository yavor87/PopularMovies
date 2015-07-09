package com.yavor.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
    public MoviesFragment() {
    }

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    GridLayout mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        mGridView = (GridLayout) view.findViewById(R.id.grid_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_pref = prefs.getString(getResources().getString(R.string.pref_sort_key),
                getResources().getStringArray(R.array.sort_prefs)[0]);
        new FetchMoviesTask().execute(sort_pref);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, MovieInfo[]> {
        private final String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3/";
        private final String POPULAR_MOVIES_PATH = "discover/movie";
        private final String SORT_QUERY = "sort_by";
        private final String APIKEY_QUERY = "api_key";
        private final String API_KEY = "699ff746b7269c719d2eb60eb957aca1";
        private URL mFetchUrl;

        @Override
        protected MovieInfo[] doInBackground(String... params) {
            buildUrl(params[0]);

            String jsonStr = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                urlConnection = (HttpURLConnection) mFetchUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer buffer = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        jsonStr = null;
                    }
                    jsonStr = buffer.toString();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                jsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return parseMoviesJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieInfo[] movieInfo) {
            super.onPostExecute(movieInfo);
        }

        private MovieInfo[] parseMoviesJson(String json)
                throws JSONException {
            final String RESULTS_LIST = "results";

            JSONObject moviesJson = new JSONObject(json);
            JSONArray moviesArray = moviesJson.getJSONArray(RESULTS_LIST);

            MovieInfo[] movies = new MovieInfo[moviesArray.length()];
            for (int i = 0; i < moviesArray.length(); i++) {
                // TODO: Add parsing
            }
            return movies;
        }

        private void buildUrl(String sort_pref) {
            Uri buildUri = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon()
                    .appendEncodedPath(POPULAR_MOVIES_PATH)
                    .appendQueryParameter(SORT_QUERY, sort_pref)
                    .appendQueryParameter(APIKEY_QUERY, API_KEY)
                    .build();
            String finalUrl = buildUri.toString();
            Log.v(LOG_TAG, "Computed url: " + finalUrl);
            try {
                mFetchUrl = new URL(finalUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private class MoviesAdapter extends BaseAdapter {
        MoviesAdapter(Context context) {
            mContext = context;
        }

        private Context mContext;

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
