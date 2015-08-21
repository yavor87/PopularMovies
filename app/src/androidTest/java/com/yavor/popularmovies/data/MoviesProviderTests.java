package com.yavor.popularmovies.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.yavor.popularmovies.ApplicationTest;

public class MoviesProviderTests extends ApplicationTest {

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mContext.deleteDatabase(MoviesDBHelper.DATABASE_FILE_NAME);
    }

    public void testInsert_Movie() {
        // Arrange
        ContentValues values = getTestMovie();

        // Act
        Uri uri = getContext().getContentResolver().insert(MoviesContract.Movie.CONTENT_URI, values);

        // Assert
        assertNotNull(uri);
        assertEquals(1, ContentUris.parseId(uri));
    }

    public void testInsert_Review() {
        // Arrange
        ContentValues values = new ContentValues();
        values.put(MoviesContract.Review.AUTHOR, "me");
        values.put(MoviesContract.Review.CONTENT, "test review");
        values.put(MoviesContract.Review.MOVIE_ID, 1);
        values.put(MoviesContract.Review.MOVIEDB_ID, "sfndsl2kamafla");

        // Act
        getContext().getContentResolver().insert(MoviesContract.Movie.CONTENT_URI, getTestMovie());

        // Act
        Uri uri = getContext().getContentResolver().insert(MoviesContract.Review.CONTENT_URI, values);

        // Assert
        assertNotNull(uri);
        assertEquals(1, ContentUris.parseId(uri));
    }

    public void testInsert_Trailer() {
        // Arrange
        ContentValues values = new ContentValues();
        values.put(MoviesContract.Trailer.KEY, "sfswsfs");
        values.put(MoviesContract.Trailer.SITE, "test");
        values.put(MoviesContract.Trailer.NAME, "test trailer");
        values.put(MoviesContract.Trailer.MOVIE_ID, 1);
        values.put(MoviesContract.Trailer.MOVIEDB_ID, "sfndsl2kamafla");

        // Act
        getContext().getContentResolver().insert(MoviesContract.Movie.CONTENT_URI, getTestMovie());

        // Act
        Uri uri = getContext().getContentResolver().insert(MoviesContract.Trailer.CONTENT_URI, values);

        // Assert
        assertNotNull(uri);
        assertEquals(1, ContentUris.parseId(uri));
    }

    private ContentValues getTestMovie() {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.Movie._ID, 1);
        values.put(MoviesContract.Movie.TITLE, "Test movie");
        values.put(MoviesContract.Movie.OVERVIEW, "Test movie");
        values.put(MoviesContract.Movie.POSTERPATH, "tt");
        values.put(MoviesContract.Movie.RELEASEDATE, System.currentTimeMillis());
        values.put(MoviesContract.Movie.RUNTIME, 120);
        values.put(MoviesContract.Movie.VOTEAVERAGE, 4.4);
        return values;
    }
}
