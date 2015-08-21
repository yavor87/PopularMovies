package com.yavor.popularmovies.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
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

    public void testQuery_Movies() {
        // Arrange
        ContentValues values = getTestMovie();
        getContext().getContentResolver().insert(MoviesContract.Movie.CONTENT_URI, values);

        // Act
        Cursor c = getContext().getContentResolver().query(MoviesContract.Movie.CONTENT_URI, null, null, null, null);

        // Assert
        assertTrue(c.moveToFirst());
        assertEquals(1, c.getInt(c.getColumnIndex(MoviesContract.Movie._ID)));
        assertEquals(values.get(MoviesContract.Movie.TITLE), c.getString(c.getColumnIndex(MoviesContract.Movie.TITLE)));
        assertEquals(values.get(MoviesContract.Movie.OVERVIEW), c.getString(c.getColumnIndex(MoviesContract.Movie.OVERVIEW)));
        assertEquals(values.get(MoviesContract.Movie.POSTER_PATH), c.getString(c.getColumnIndex(MoviesContract.Movie.POSTER_PATH)));
        assertEquals(values.get(MoviesContract.Movie.RELEASE_DATE), c.getLong(c.getColumnIndex(MoviesContract.Movie.RELEASE_DATE)));
        assertEquals(values.get(MoviesContract.Movie.RUNTIME), c.getInt(c.getColumnIndex(MoviesContract.Movie.RUNTIME)));
        assertEquals(values.getAsFloat(MoviesContract.Movie.VOTE_AVERAGE), c.getFloat(c.getColumnIndex(MoviesContract.Movie.VOTE_AVERAGE)));

        c.close();
    }

    public void testQuery_SingleMovie() {
        // Arrange
        ContentValues values = getTestMovie();
        getContext().getContentResolver().insert(MoviesContract.Movie.CONTENT_URI, values);

        // Act
        Cursor c = getContext().getContentResolver()
                .query(ContentUris.withAppendedId(MoviesContract.Movie.CONTENT_URI, 1), null, null, null, null);

        // Assert
        assertTrue(c.moveToFirst());
        assertEquals(1, c.getInt(c.getColumnIndex(MoviesContract.Movie._ID)));
        assertEquals(values.get(MoviesContract.Movie.TITLE), c.getString(c.getColumnIndex(MoviesContract.Movie.TITLE)));
        assertEquals(values.get(MoviesContract.Movie.OVERVIEW), c.getString(c.getColumnIndex(MoviesContract.Movie.OVERVIEW)));
        assertEquals(values.get(MoviesContract.Movie.POSTER_PATH), c.getString(c.getColumnIndex(MoviesContract.Movie.POSTER_PATH)));
        assertEquals(values.get(MoviesContract.Movie.RELEASE_DATE), c.getLong(c.getColumnIndex(MoviesContract.Movie.RELEASE_DATE)));
        assertEquals(values.get(MoviesContract.Movie.RUNTIME), c.getInt(c.getColumnIndex(MoviesContract.Movie.RUNTIME)));
        assertEquals(values.getAsFloat(MoviesContract.Movie.VOTE_AVERAGE), c.getFloat(c.getColumnIndex(MoviesContract.Movie.VOTE_AVERAGE)));
        assertEquals(values.getAsFloat(MoviesContract.Movie.POPULARITY), c.getFloat(c.getColumnIndex(MoviesContract.Movie.POPULARITY)));

        c.close();
    }

    private ContentValues getTestMovie() {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.Movie._ID, 1);
        values.put(MoviesContract.Movie.TITLE, "Test movie");
        values.put(MoviesContract.Movie.OVERVIEW, "Test movie");
        values.put(MoviesContract.Movie.POSTER_PATH, "tt");
        values.put(MoviesContract.Movie.RELEASE_DATE, System.currentTimeMillis());
        values.put(MoviesContract.Movie.RUNTIME, 120);
        values.put(MoviesContract.Movie.VOTE_AVERAGE, 4.4);
        values.put(MoviesContract.Movie.POPULARITY, 14.4);
        return values;
    }
}
