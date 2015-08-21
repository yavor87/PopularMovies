package com.yavor.popularmovies.data;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class MoviesDbTests extends AndroidTestCase {
    private MoviesDBHelper dbHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dbHelper = new MoviesDBHelper(mContext);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mContext.deleteDatabase(MoviesDBHelper.DATABASE_FILE_NAME);
    }

    public void testCreateDb() {
        // Act
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Assert
        assertTrue(db.isOpen());

        db.close();
    }

    public void testInsert_Movie() {
        // Arrange
        ContentValues values = getTestMovie();

        // Act
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, values);

        // Assert
        assertEquals(1, id);
    }

    public void testInsert_Movie_Twice() {
        // Arrange
        ContentValues values = getTestMovie();

        // Act
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id1 = db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, values);
        long id2 = db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, values);

        // Assert
        assertEquals(1, id1);
        assertEquals(id1, id2);
    }

    public void testInsert_Movie_Twice_WithReviews() {
        // Arrange
        ContentValues values = getTestMovie();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id1 = db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, values);
        db.insertOrThrow(MoviesContract.Review.TABLE_NAME, null, getTestReview());

        // Act
        long id2 = db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, values);

        // Assert
        assertEquals(1, id1);
        assertEquals(id1, id2);
    }

    public void testInsert_Movie_Twice_WithTrailers() {
        // Arrange
        ContentValues values = getTestMovie();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id1 = db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, values);
        db.insertOrThrow(MoviesContract.Trailer.TABLE_NAME, null, getTestTrailer());

        // Act
        long id2 = db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, values);

        // Assert
        assertEquals(1, id1);
        assertEquals(id1, id2);
    }

    public void testInsert_Movie_Twice_WithReviewsAndTrailers() {
        // Arrange
        ContentValues values = getTestMovie();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id1 = db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, values);
        db.insertOrThrow(MoviesContract.Review.TABLE_NAME, null, getTestReview());
        db.insertOrThrow(MoviesContract.Trailer.TABLE_NAME, null, getTestTrailer());

        // Act
        long id2 = db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, values);

        // Assert
        assertEquals(1, id1);
        assertEquals(id1, id2);
    }

    public void testInsert_Review() {
        // Arrange
        ContentValues values = getTestReview();

        // Act
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, getTestMovie());

        // Act
        long id = db.insertOrThrow(MoviesContract.Review.TABLE_NAME, null, values);

        // Assert
        assertEquals(1, id);
    }

    public void testInsert_Review_Twice() {
        // Arrange
        ContentValues values = getTestReview();

        // Act
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, getTestMovie());

        // Act
        long id1 = db.insertOrThrow(MoviesContract.Review.TABLE_NAME, null, values);
        long id2 = db.insertOrThrow(MoviesContract.Review.TABLE_NAME, null, values);
        long count = DatabaseUtils.queryNumEntries(db, MoviesContract.Review.TABLE_NAME);

        // Assert
        assertEquals(1, id1);
        assertEquals(2, id2);
        assertEquals(1, count);
    }

    public void testInsert_Trailer() {
        // Arrange
        ContentValues values = getTestTrailer();

        // Act
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, getTestMovie());

        // Act
        long id = db.insertOrThrow(MoviesContract.Trailer.TABLE_NAME, null, values);

        // Assert
        assertEquals(1, id);
    }

    public void testInsert_Trailer_Twice() {
        // Arrange
        ContentValues values = getTestTrailer();

        // Act
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertOrThrow(MoviesContract.Movie.TABLE_NAME, null, getTestMovie());

        // Act
        long id1 = db.insertOrThrow(MoviesContract.Trailer.TABLE_NAME, null, values);
        long id2 = db.insertOrThrow(MoviesContract.Trailer.TABLE_NAME, null, values);
        long count = DatabaseUtils.queryNumEntries(db, MoviesContract.Trailer.TABLE_NAME);

        // Assert
        assertEquals(1, id1);
        assertEquals(2, id2);
        assertEquals(1, count);
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

    private ContentValues getTestReview() {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.Review.AUTHOR, "me");
        values.put(MoviesContract.Review.CONTENT, "test review");
        values.put(MoviesContract.Review.MOVIE_ID, 1);
        values.put(MoviesContract.Review.MOVIEDB_ID, "sfndsl2kamafla");
        return values;
    }

    private ContentValues getTestTrailer() {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.Trailer.KEY, "sfswsfs");
        values.put(MoviesContract.Trailer.SITE, "test");
        values.put(MoviesContract.Trailer.NAME, "test trailer");
        values.put(MoviesContract.Trailer.MOVIE_ID, 1);
        values.put(MoviesContract.Trailer.MOVIEDB_ID, "sfndsl2kamafla");
        return values;
    }
}
