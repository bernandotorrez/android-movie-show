package com.dicoding.movieandtvshow.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dicoding.movieandtvshow.db.DatabaseContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static String DATABASE_NAME = "dbmovieapp";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_MOVIE,
            MovieColumns._ID,
            MovieColumns.ID,
            MovieColumns.TITLE,
            MovieColumns.POSTER_PATH,
            MovieColumns.VOTE_AVERAGE,
            MovieColumns.OVERVIEW,
            MovieColumns.RELEASE_DATE,
            MovieColumns.POPULARITY
    );

    private static final String SQL_CREATE_TABLE_TVSHOW = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_TVSHOW,
            TVShowsColumns._ID,
            TVShowsColumns.ID,
            TVShowsColumns.TITLE,
            TVShowsColumns.POSTER_PATH,
            TVShowsColumns.VOTE_AVERAGE,
            TVShowsColumns.OVERVIEW,
            TVShowsColumns.RELEASE_DATE,
            TVShowsColumns.POPULARITY
    );

    private static final String SQL_CREATE_TABLE_REMINDER = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_REMINDER,
            ReminderColumns._ID,
            ReminderColumns.TYPE_REMINDER,
            ReminderColumns.TIME_REMINDER,
            ReminderColumns.IS_REMINDER
    );

    private static final String SQL_INSERT_TABLE_REMINDER = String.format("INSERT INTO %s"
                    + "(%s, %s, %s)" +
                    "VALUES ('release_reminder', '08:00', 'no'), ('daily_reminder', '07:00', 'no')",
            DatabaseContract.TABLE_REMINDER,
            ReminderColumns.TYPE_REMINDER,
            ReminderColumns.TIME_REMINDER,
            ReminderColumns.IS_REMINDER
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TVSHOW);
        db.execSQL(SQL_CREATE_TABLE_REMINDER);
        db.execSQL(SQL_INSERT_TABLE_REMINDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_TVSHOW);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_REMINDER);
        onCreate(db);

    }
}
