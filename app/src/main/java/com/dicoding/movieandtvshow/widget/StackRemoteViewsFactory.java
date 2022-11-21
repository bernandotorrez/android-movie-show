package com.dicoding.movieandtvshow.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.movieandtvshow.R;
import com.dicoding.movieandtvshow.db.DatabaseContract;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    //private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private final ArrayList<String> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    private Cursor cursorMovie;
    private Cursor cursorTV;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        long identityToken = Binder.clearCallingIdentity();
        try {
            cursorMovie = mContext.getContentResolver().query(DatabaseContract.MovieColumns.CONTENT_URI, null, null, null, null);
            cursorTV = mContext.getContentResolver().query(DatabaseContract.TVShowsColumns.CONTENT_URI, null, null, null, null);

            if (cursorMovie != null) {

                while (cursorMovie.moveToNext()) {
                    String poster_path = cursorMovie.getString(cursorMovie.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER_PATH));
                    mWidgetItems.add(poster_path);
                }

            }

            if (cursorTV != null) {
                while (cursorTV.moveToNext()) {
                    String poster_path = cursorTV.getString(cursorMovie.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.POSTER_PATH));
                    mWidgetItems.add(poster_path);
                }
            }
        } finally {
            Binder.restoreCallingIdentity(identityToken);
        }

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        String urldisplay = mWidgetItems.get(position);

        Bitmap bitmap = null;

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.image_loader)
                .fallback(R.drawable.image_broken)
                .error(R.drawable.image_broken)
                .priority(Priority.HIGH);

        try {
            bitmap = Glide
                    .with(mContext)
                    .asBitmap()
                    .load(urldisplay)
                    .apply(options)
                    .submit()
                    .get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rv.setImageViewBitmap(R.id.imageView, bitmap);

        Bundle extras = new Bundle();
        extras.putInt(FavouriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
