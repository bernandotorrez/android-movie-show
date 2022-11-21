package com.dicoding.movieandtvshow.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.dicoding.movieandtvshow";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static String TABLE_MOVIE = "movie";
    public static final class MovieColumns implements BaseColumns {
        public static String ID = "id";
        public static String TITLE = "title";
        public static String POSTER_PATH = "poster_path";
        public static String VOTE_AVERAGE = "vote_average";
        public static String OVERVIEW = "overview";
        public static String RELEASE_DATE = "release_date";
        public static String POPULARITY = "popularity";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_MOVIE)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public static String TABLE_TVSHOW = "tvshow";
    public static final class TVShowsColumns implements BaseColumns {
        public static String ID = "id";
        public static String TITLE = "name";
        public static String POSTER_PATH = "poster_path";
        public static String VOTE_AVERAGE = "vote_average";
        public static String OVERVIEW = "overview";
        public static String RELEASE_DATE = "first_air_date";
        public static String POPULARITY = "popularity";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_TVSHOW)
                .build();
    }

    static String TABLE_REMINDER = "reminder";
    static final class ReminderColumns implements BaseColumns {
        static String TYPE_REMINDER = "type_reminder";
        static String TIME_REMINDER = "time_reminder";
        static String IS_REMINDER = "is_reminder";
    }

}
