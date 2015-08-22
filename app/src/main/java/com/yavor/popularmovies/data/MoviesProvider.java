package com.yavor.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.yavor.popularmovies.BuildConfig;

import java.util.Arrays;

public class MoviesProvider extends ContentProvider {
    private static final String TAG = MoviesProvider.class.getSimpleName();

    private static final int URI_TYPE_MOVIE = 0;
    private static final int URI_TYPE_MOVIE_ID = 1;

    private static final int URI_TYPE_MOVIE_REVIEW = 2;
    private static final int URI_TYPE_MOVIE_REVIEW_ID = 3;

    private static final int URI_TYPE_MOVIE_TRAILER = 4;
    private static final int URI_TYPE_MOVIE_TRAILER_ID = 5;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    protected MoviesDBHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case URI_TYPE_MOVIE:
                return MoviesContract.Movie.CONTENT_TYPE;
            case URI_TYPE_MOVIE_ID:
                return MoviesContract.Movie.CONTENT_ITEM_TYPE;

            case URI_TYPE_MOVIE_REVIEW:
                return MoviesContract.Review.CONTENT_TYPE;
            case URI_TYPE_MOVIE_REVIEW_ID:
                return MoviesContract.Review.CONTENT_ITEM_TYPE;

            case URI_TYPE_MOVIE_TRAILER:
                return MoviesContract.Trailer.CONTENT_TYPE;
            case URI_TYPE_MOVIE_TRAILER_ID:
                return MoviesContract.Trailer.CONTENT_ITEM_TYPE;

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (BuildConfig.DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);

        String table;
        switch (sUriMatcher.match(uri)) {
            case URI_TYPE_MOVIE:
                table = MoviesContract.Movie.TABLE_NAME;
                break;
            case URI_TYPE_MOVIE_REVIEW:
                table = MoviesContract.Review.TABLE_NAME;
                break;
            case URI_TYPE_MOVIE_TRAILER:
                table = MoviesContract.Trailer.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowId = db.insertOrThrow(table, null, values);
        if (rowId == -1)
            throw new SQLException("Failed to insert row into " + uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, rowId);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);

        String table;
        switch (sUriMatcher.match(uri)) {
            case URI_TYPE_MOVIE:
                table = MoviesContract.Movie.TABLE_NAME;
                break;
            case URI_TYPE_MOVIE_REVIEW:
                table = MoviesContract.Review.TABLE_NAME;
                break;
            case URI_TYPE_MOVIE_TRAILER:
                table = MoviesContract.Trailer.TABLE_NAME;
                break;
            default:
                return super.bulkInsert(uri, values);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(table, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));

        String table;
        switch (sUriMatcher.match(uri)) {
            case URI_TYPE_MOVIE_ID: {
                long id = ContentUris.parseId(uri);
                selection = MoviesContract.Movie._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
            }
            case URI_TYPE_MOVIE:
                table = MoviesContract.Movie.TABLE_NAME;
                break;
            case URI_TYPE_MOVIE_REVIEW_ID: {
                long id = ContentUris.parseId(uri);
                selection = MoviesContract.Review._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
            }
            case URI_TYPE_MOVIE_REVIEW:
                table = MoviesContract.Review.TABLE_NAME;
                break;
            case URI_TYPE_MOVIE_TRAILER_ID: {
                long id = ContentUris.parseId(uri);
                selection = MoviesContract.Trailer._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
            }
            case URI_TYPE_MOVIE_TRAILER:
                table = MoviesContract.Trailer.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated = db.update(table, values, selection, selectionArgs);
        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));

        String table;
        switch (sUriMatcher.match(uri)) {
            case URI_TYPE_MOVIE_ID: {
                long id = ContentUris.parseId(uri);
                selection = MoviesContract.Movie._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
            }
            case URI_TYPE_MOVIE:
                table = MoviesContract.Movie.TABLE_NAME;
                break;
            case URI_TYPE_MOVIE_REVIEW_ID: {
                long id = ContentUris.parseId(uri);
                selection = MoviesContract.Review._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
            }
            case URI_TYPE_MOVIE_REVIEW:
                table = MoviesContract.Review.TABLE_NAME;
                break;
            case URI_TYPE_MOVIE_TRAILER_ID: {
                long id = ContentUris.parseId(uri);
                selection = MoviesContract.Trailer._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
            }
            case URI_TYPE_MOVIE_TRAILER:
                table = MoviesContract.Trailer.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(table, selection, selectionArgs);
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder);

        String table;
        switch (sUriMatcher.match(uri)) {
            case URI_TYPE_MOVIE_ID: {
                long id = ContentUris.parseId(uri);
                selection = MoviesContract.Movie._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
            }
            case URI_TYPE_MOVIE:
                table = MoviesContract.Movie.TABLE_NAME;
                break;
            case URI_TYPE_MOVIE_REVIEW_ID: {
                long id = ContentUris.parseId(uri);
                selection = MoviesContract.Review._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
            }
            case URI_TYPE_MOVIE_REVIEW:
                table = MoviesContract.Review.TABLE_NAME;
                break;
            case URI_TYPE_MOVIE_TRAILER_ID: {
                long id = ContentUris.parseId(uri);
                selection = MoviesContract.Trailer._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
            }
            case URI_TYPE_MOVIE_TRAILER:
                table = MoviesContract.Trailer.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.Movie.TABLE_NAME, URI_TYPE_MOVIE);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.Movie.TABLE_NAME + "/#", URI_TYPE_MOVIE_ID);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.Review.TABLE_NAME, URI_TYPE_MOVIE_REVIEW);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.Review.TABLE_NAME + "/#", URI_TYPE_MOVIE_REVIEW_ID);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.Trailer.TABLE_NAME, URI_TYPE_MOVIE_TRAILER);
        matcher.addURI(MoviesContract.AUTHORITY, MoviesContract.Trailer.TABLE_NAME + "/#", URI_TYPE_MOVIE_TRAILER_ID);
        return matcher;
    }
}
