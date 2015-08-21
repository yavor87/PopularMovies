package com.yavor.popularmovies.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.yavor.popularmovies.BuildConfig;

public class MoviesDBHelper extends SQLiteOpenHelper {
    static final String DATABASE_FILE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = "CREATE TABLE IF NOT EXISTS "
            + MoviesContract.Movie.TABLE_NAME + " ( "
            + MoviesContract.Movie._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, "
            + MoviesContract.Movie.TITLE + " TEXT NOT NULL, "
            + MoviesContract.Movie.RELEASEDATE + " INTEGER, "
            + MoviesContract.Movie.RUNTIME + " INTEGER, "
            + MoviesContract.Movie.POSTERPATH + " TEXT, "
            + MoviesContract.Movie.VOTEAVERAGE + " REAL, "
            + MoviesContract.Movie.POPULARITY + " REAL, "
            + MoviesContract.Movie.OVERVIEW + " TEXT "
            + " );";

    private static final String SQL_CREATE_TABLE_MOVIEREVIEW = "CREATE TABLE IF NOT EXISTS "
            + MoviesContract.Review.TABLE_NAME + " ( "
            + MoviesContract.Review._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MoviesContract.Review.MOVIE_ID + " INTEGER NOT NULL, "
            + MoviesContract.Review.MOVIEDB_ID + " TEXT NOT NULL, "
            + MoviesContract.Review.AUTHOR + " TEXT NOT NULL, "
            + MoviesContract.Review.CONTENT + " TEXT NOT NULL "
            + ", CONSTRAINT fk_movie_id FOREIGN KEY (" + MoviesContract.Review.MOVIE_ID
            + ") REFERENCES movie ("+ MoviesContract.Movie._ID + ") ON DELETE RESTRICT"
            + ", UNIQUE (" + MoviesContract.Review.MOVIEDB_ID
            + ") ON CONFLICT REPLACE"
            + ");";

    private static final String SQL_CREATE_TABLE_MOVIETRAILER = "CREATE TABLE IF NOT EXISTS "
            + MoviesContract.Trailer.TABLE_NAME + " ( "
            + MoviesContract.Trailer._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MoviesContract.Trailer.MOVIE_ID + " INTEGER NOT NULL, "
            + MoviesContract.Review.MOVIEDB_ID + " TEXT NOT NULL, "
            + MoviesContract.Trailer.NAME + " TEXT NOT NULL, "
            + MoviesContract.Trailer.SITE + " TEXT NOT NULL, "
            + MoviesContract.Trailer.KEY + " TEXT NOT NULL "
            + ", CONSTRAINT fk_movie_id FOREIGN KEY (" + MoviesContract.Trailer.MOVIE_ID
            + ") REFERENCES movie ("+ MoviesContract.Movie._ID + ") ON DELETE RESTRICT"
            + ", UNIQUE (" + MoviesContract.Trailer.MOVIEDB_ID
            + ") ON CONFLICT REPLACE"
            + ");";

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_MOVIEREVIEW);
        db.execSQL(SQL_CREATE_TABLE_MOVIETRAILER);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.Movie.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.Review.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.Trailer.TABLE_NAME);
        onCreate(db);
    }
}
