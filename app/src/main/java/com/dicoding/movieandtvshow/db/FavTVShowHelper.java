package com.dicoding.movieandtvshow.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dicoding.movieandtvshow.favourite.tvshows.FavouritesTVShowsItems;
import com.dicoding.movieandtvshow.tvshows.TVShowItems;

import static android.provider.BaseColumns._ID;
import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.ID;
import static com.dicoding.movieandtvshow.db.DatabaseContract.TVShowsColumns.*;
import static com.dicoding.movieandtvshow.db.DatabaseContract.TABLE_TVSHOW;

import java.util.ArrayList;

public class FavTVShowHelper {

    private static final String DATABASE_TABLE = TABLE_TVSHOW;
    private static com.dicoding.movieandtvshow.db.DatabaseHelper dataBaseHelper;
    private static FavTVShowHelper INSTANCE;

    private static SQLiteDatabase database;

    public FavTVShowHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public FavTVShowHelper() {

    }

    public static FavTVShowHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavTVShowHelper(context);
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

    public ArrayList<FavouritesTVShowsItems> getAllTVShow() {
        ArrayList<FavouritesTVShowsItems> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " DESC",
                null);
        cursor.moveToFirst();
        FavouritesTVShowsItems tvshow;

        if (cursor.getCount() > 0) {
            do {
                tvshow = new FavouritesTVShowsItems();
                tvshow.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                tvshow.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                tvshow.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                tvshow.setVoteAverage(cursor.getString(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                tvshow.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                tvshow.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                tvshow.setPopularity(cursor.getString(cursor.getColumnIndexOrThrow(POPULARITY)));

                arrayList.add(tvshow);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }

        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }

        return arrayList;
    }

    public static int getDataTVShow(Integer id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_TVSHOW + " WHERE "  +ID + " = " +id,null);
        cursor.moveToFirst();

        return cursor.getCount();
    }

    public static long insertTVShow(TVShowItems tvshow) {
        ContentValues args = new ContentValues();
        args.put(ID, tvshow.getId());
        args.put(TITLE, tvshow.getTitle());
        args.put(POSTER_PATH, tvshow.getPosterPath());
        args.put(VOTE_AVERAGE, tvshow.getVoteAverage());
        args.put(OVERVIEW, tvshow.getOverview());
        args.put(RELEASE_DATE, tvshow.getReleaseDate());
        args.put(POPULARITY, tvshow.getPopularity());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public static int deleteTVShow(int id) {
        return database.delete(TABLE_TVSHOW, ID + " = '" + id + "'", null);
    }

    public static Cursor getDataTVShowWidget() {
        Cursor cursor = database.rawQuery("SELECT " + POSTER_PATH + " FROM " + TABLE_TVSHOW,null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor queryByIdProvider(String id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_TVSHOW + " WHERE "  +ID + " = " +id,null);
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
