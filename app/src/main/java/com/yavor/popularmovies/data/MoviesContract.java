package com.yavor.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class MoviesContract {
    public static final String AUTHORITY = "com.yavor.popularmovies";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Movie implements BaseColumns {
        static final String TABLE_NAME = "movie";
        public static final Uri CONTENT_URI = Uri.parse(AUTHORITY_URI + "/" + TABLE_NAME);
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;

        /**
         * Primary key.
         */
        public static final String _ID = BaseColumns._ID;

        public static final String TITLE = "title";

        public static final String RELEASE_DATE = "release_date";

        public static final String RUNTIME = "runtime";

        public static final String POSTER_PATH = "poster_path";

        public static final String VOTE_AVERAGE = "vote_average";

        public static final String POPULARITY = "popularity";

        public static final String OVERVIEW = "overview";

        public static final String FAVOURITE = "is_favourite";
    }

    public static final class Review implements BaseColumns {
        static final String TABLE_NAME = "review";
        public static final Uri CONTENT_URI = Uri.parse(AUTHORITY_URI + "/" + TABLE_NAME);
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;

        /**
         * Primary key.
         */
        public static final String _ID = BaseColumns._ID;

        public static final String MOVIE_ID = "movie_id";

        public static final String MOVIEDB_ID = "moviedb_id";

        public static final String AUTHOR = "author";

        public static final String CONTENT = "content";
    }

    public static final class Trailer implements BaseColumns {
        static final String TABLE_NAME = "trailer";
        public static final Uri CONTENT_URI = Uri.parse(AUTHORITY_URI + "/" + TABLE_NAME);
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;

        /**
         * Primary key.
         */
        public static final String _ID = BaseColumns._ID;

        public static final String MOVIE_ID = "movie_id";

        public static final String MOVIEDB_ID = "moviedb_id";

        public static final String NAME = "name";

        public static final String SITE = "site";

        public static final String KEY = "key";
    }
}
