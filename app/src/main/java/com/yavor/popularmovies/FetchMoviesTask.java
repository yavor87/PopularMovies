package com.yavor.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class FetchMoviesTask extends AsyncTask<String, Void, MovieInfo[]> {
    private final static String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private final static String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3/";
    private final static String POPULAR_MOVIES_PATH = "discover/movie";
    private final static String SORT_QUERY = "sort_by";
    private final static String APIKEY_QUERY = "api_key";
    private final static String API_KEY = "699ff746b7269c719d2eb60eb957aca1";
    private final String RESULTS_LIST_KEY = "results";
    private static final String ADULT_KEY = "adult";
    private static final String BACKDROP_PATH_KEY = "backdrop_path";
    private static final String GENRE_IDS_KEY = "genre_ids";
    private static final String ID_KEY = "id";
    private static final String ORIGINAL_LANGUAGE_KEY = "original_language";
    private static final String ORIGINAL_TITLE_KEY = "original_title";
    private static final String OVERVIEW_KEY = "overview";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String POSTER_PATH_KEY = "poster_path";
    private static final String POPULARITY_KEY = "popularity";
    private static final String TITLE_KEY = "title";
    private static final String VIDEO_KEY = "video";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String VOTE_COUNT_KEY = "vote_count";
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

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
        JSONObject moviesJson = new JSONObject(json);
        JSONArray moviesArray = moviesJson.getJSONArray(RESULTS_LIST_KEY);

        MovieInfo[] movies = new MovieInfo[moviesArray.length()];
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieJson = moviesArray.getJSONObject(i);
            MovieInfo movieInfo = new MovieInfo();

            // Adult
            movieInfo.setAdult(movieJson.optBoolean(ADULT_KEY));

            // Backdrop path
            movieInfo.setBackdropPath(movieJson.optString(BACKDROP_PATH_KEY));

            // Genre ids
            JSONArray genreIds = movieJson.optJSONArray(GENRE_IDS_KEY);
            if (genreIds != null && genreIds.length() > 0) {
                int[] genres = new int[genreIds.length()];
                for (int j = 0; j < genreIds.length(); j++) {
                    genres[j] = genreIds.getInt(j);
                }
                movieInfo.setGenreIds(genres);
            }

            // Id
            movieInfo.setId(movieJson.optInt(ID_KEY));

            // Original language
            movieInfo.setOriginalLanguage(movieJson.optString(ORIGINAL_LANGUAGE_KEY));

            // Original title
            movieInfo.setOriginalTitle(movieJson.optString(ORIGINAL_TITLE_KEY));

            // Overview
            movieInfo.setOverview(movieJson.optString(OVERVIEW_KEY));

            // Release date
            String releaseDateStr = movieJson.optString(RELEASE_DATE_KEY);
            if (releaseDateStr.length() > 0) {
                try {
                    Date parsedDate = DATE_FORMAT.parse(releaseDateStr);
                    movieInfo.setReleaseDate(parsedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Poster path
            movieInfo.setPosterPath(movieJson.optString(POSTER_PATH_KEY));

            // Popularity
            movieInfo.setPopularity(movieJson.optInt(POPULARITY_KEY));

            // Title
            movieInfo.setTitle(movieJson.optString(TITLE_KEY));

            // Video
            movieInfo.setVideo(movieJson.optBoolean(VIDEO_KEY));

            // Vote average
            movieInfo.setVoteAverage(movieJson.optInt(VOTE_AVERAGE_KEY));

            // Vote count
            movieInfo.setVoteCount(movieJson.optInt(VOTE_COUNT_KEY));

            movies[i] = movieInfo;
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
