package com.dicoding.movieandtvshow.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dicoding.movieandtvshow.FavouriteListActivity;
import com.dicoding.movieandtvshow.db.FavMovieHelper;
import com.dicoding.movieandtvshow.db.FavTVShowHelper;

import static com.dicoding.movieandtvshow.db.DatabaseContract.AUTHORITY;
import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.dicoding.movieandtvshow.db.DatabaseContract.TABLE_MOVIE;
import static com.dicoding.movieandtvshow.db.DatabaseContract.TABLE_TVSHOW;

public class FavMovieProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TVSHOW = 3;
    private static final int TVSHOW_ID = 4;
    private static final int WIDGET_MOVIE = 5;
    private static final int WIDGET_TVSHOW = 6;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavMovieHelper favMovieHelper;
    private FavTVShowHelper favTVShowHelper;

    static {

        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE + "/#", MOVIE_ID);

        sUriMatcher.addURI(AUTHORITY, TABLE_TVSHOW, TVSHOW);
        sUriMatcher.addURI(AUTHORITY, TABLE_TVSHOW + "/#", TVSHOW_ID);
    }

    @Override
    public boolean onCreate() {
        favMovieHelper = FavMovieHelper.getInstance(getContext());
        favMovieHelper.open();

        favTVShowHelper = favTVShowHelper.getInstance(getContext());
        favTVShowHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = favMovieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = favMovieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TVSHOW:
                cursor = favTVShowHelper.queryProvider();
                break;
            case TVSHOW_ID:
                cursor = favTVShowHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case WIDGET_MOVIE:
                cursor = favMovieHelper.getDataMovieWidget();
                break;
            case WIDGET_TVSHOW:
                cursor = favTVShowHelper.getDataTVShowWidget();
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = favMovieHelper.insertProvider(contentValues);
                break;
            case TVSHOW:
                added = favTVShowHelper.insertProvider(contentValues);
                break;
            default:
                added = 0;
                break;
        }


        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = favMovieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            case TVSHOW_ID:
                deleted = favTVShowHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }


        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                updated = favMovieHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            case TVSHOW_ID:
                updated = favTVShowHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;
            default:
                updated = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }
}
