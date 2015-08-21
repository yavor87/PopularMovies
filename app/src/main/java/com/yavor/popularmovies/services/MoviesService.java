package com.yavor.popularmovies.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.yavor.popularmovies.data.MoviesContract;
import com.yavor.popularmovies.utils.MovieDBUtils;

import java.util.Date;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Reviews;
import info.movito.themoviedbapi.model.Video;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.tools.MovieDbException;

public class MoviesService extends IntentService {
    public MoviesService() {
        super("Movies");
    }

    public static final String QUERY_SORT_ORDER = "qso";
    private static final String LOG_TAG = MoviesService.class.getSimpleName();

    @Override
    protected void onHandleIntent(Intent intent) {
        String sortBy = intent.getStringExtra(QUERY_SORT_ORDER);
        fetchPopularMovies(sortBy);
        Log.e(LOG_TAG, "Movie fetching successful");
    }

    private void fetchPopularMovies(String sortBy) {
        Log.v(LOG_TAG, "fetching movies sorted by " + sortBy);
        try {
            TmdbApi api = new TmdbApi(MovieDBUtils.API_KEY);
            Discover d = new Discover();
            d.sortBy(sortBy);

            MovieResultsPage page;
            try {
                page = api.getDiscover().getDiscover(d);
            } catch (MovieDbException e) {
                Log.e(LOG_TAG, "Unable to fetch movies", e);
                return;
            }

            for (MovieDb movie : page) {
                fetchFullMovie(api, movie.getId());
            }
            // TODO: store current popular movies
        } catch (MovieDbException e) {
            Log.e(LOG_TAG, "Unable to get movie data", e);
        }
    }

    private void fetchFullMovie(TmdbApi api, int movieId) {
        Log.v(LOG_TAG, "fetchFullMovie " + movieId);
        MovieDb movie;
        try {
            movie = api.getMovies().getMovie(movieId, null,
                    TmdbMovies.MovieMethod.reviews, TmdbMovies.MovieMethod.videos);
        } catch (MovieDbException e) {
            // TODO: Find out why the service is crashing
            Log.e(LOG_TAG, "Unable to fetch movie details", e);
            return;
        }

        ContentValues movieValues = buildMovieContentValues(movie);
        Uri uri = getContentResolver().insert(MoviesContract.Movie.CONTENT_URI, movieValues);
        if (uri == null) {
            Log.e(LOG_TAG, "Movie insertion failed");
            return;
        }
        ContentValues[] reviewValues = buildReviewsContentValues(movieId, movie.getReviews());
        getContentResolver().bulkInsert(MoviesContract.Review.CONTENT_URI, reviewValues);
        ContentValues[] trailerValues = buildTrailersContentValues(movieId, movie.getVideos());
        getContentResolver().bulkInsert(MoviesContract.Trailer.CONTENT_URI, trailerValues);
    }

    private static ContentValues buildMovieContentValues(MovieDb movie) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.Movie._ID, movie.getId());
        values.put(MoviesContract.Movie.TITLE, movie.getTitle());
        values.put(MoviesContract.Movie.OVERVIEW, movie.getOverview());
        values.put(MoviesContract.Movie.POSTERPATH, movie.getPosterPath());
        values.put(MoviesContract.Movie.RUNTIME, movie.getRuntime());
        Date releaseDate = MovieDBUtils.parseDate(movie.getReleaseDate());
        if (releaseDate != null) {
            values.put(MoviesContract.Movie.RELEASEDATE, releaseDate.getTime());
        }
        values.put(MoviesContract.Movie.VOTEAVERAGE, movie.getVoteAverage());
        values.put(MoviesContract.Movie.POPULARITY, movie.getPopularity());
        return values;
    }

    private static ContentValues[] buildReviewsContentValues(int movieId, List<Reviews> reviews) {
        ContentValues[] values = new ContentValues[reviews.size()];
        for (int i = 0; i < reviews.size(); i++) {
            Reviews review = reviews.get(i);
            ContentValues value = new ContentValues();
            value.put(MoviesContract.Review.MOVIE_ID, movieId);
            value.put(MoviesContract.Review.MOVIEDB_ID, review.getId());
            value.put(MoviesContract.Review.AUTHOR, review.getAuthor());
            value.put(MoviesContract.Review.CONTENT, review.getContent());
            values[i] = value;
        }
        return values;
    }

    private static ContentValues[] buildTrailersContentValues(int movieId, List<Video> videos) {
        ContentValues[] values = new ContentValues[videos.size()];
        for (int i = 0; i < videos.size(); i++) {
            Video video = videos.get(i);
            ContentValues value = new ContentValues();
            value.put(MoviesContract.Trailer.MOVIE_ID, movieId);
            value.put(MoviesContract.Trailer.MOVIEDB_ID, video.getId());
            value.put(MoviesContract.Trailer.KEY, video.getKey());
            value.put(MoviesContract.Trailer.SITE, video.getSite());
            value.put(MoviesContract.Trailer.NAME, video.getName());
            values[i] = value;
        }
        return values;
    }
}
