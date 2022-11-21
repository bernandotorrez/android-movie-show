package com.dicoding.movieandtvshow.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dicoding.movieandtvshow.favourite.movies.FavouritesMovieItems;
import com.dicoding.movieandtvshow.movies.MovieItems;

import static android.provider.BaseColumns._ID;
import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.*;
import static com.dicoding.movieandtvshow.db.DatabaseContract.TABLE_MOVIE;

import java.util.ArrayList;

public class FavMovieHelper {

    private static final String DATABASE_TABLE = TABLE_MOVIE;
    private static DatabaseHelper dataBaseHelper;
    private static FavMovieHelper INSTANCE;

    private static SQLiteDatabase database;

    public FavMovieHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public FavMovieHelper() {

    }

    public static FavMovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavMovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public ArrayList<FavouritesMovieItems> getAllMovie() {
        ArrayList<FavouritesMovieItems> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " DESC",
                null);
        cursor.moveToFirst();
        FavouritesMovieItems movie;

        if (cursor.getCount() > 0) {
            do {
                movie = new FavouritesMovieItems();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                movie.setVoteAverage(cursor.getString(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movie.setPopularity(cursor.getString(cursor.getColumnIndexOrThrow(POPULARITY)));

                arrayList.add(movie);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }


        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }

        return arrayList;
    }

    public static int getDataMovie(Integer id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_MOVIE + " WHERE "  +ID + " = " +id,null);
        cursor.moveToFirst();

        return cursor.getCount();
    }

    public static Cursor getDataMovieWidget() {
        Cursor cursor = database.rawQuery("SELECT " + POSTER_PATH + " FROM " + TABLE_MOVIE,null);
        cursor.moveToFirst();
        return cursor;
    }

    public static long insertMovie(MovieItems movie) {
        ContentValues args = new ContentValues();
        args.put(ID, movie.getId());
        args.put(TITLE, movie.getTitle());
        args.put(POSTER_PATH, movie.getPosterPath());
        args.put(VOTE_AVERAGE, movie.getVoteAverage());
        args.put(OVERVIEW, movie.getOverview());
        args.put(RELEASE_DATE, movie.getReleaseDate());
        args.put(POPULARITY, movie.getPopularity());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public static int deleteMovie(int id) {
        return database.delete(TABLE_MOVIE, ID + " = '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_MOVIE + " WHERE "  +ID + " = " +id,null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, ID + " = ?", new String[]{id});
    }
}
