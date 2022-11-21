package com.dicoding.movieandtvshow.favourite.tvshows;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dicoding.movieandtvshow.LoadTVShowsCallback;
import com.dicoding.movieandtvshow.R;
import com.dicoding.movieandtvshow.db.DatabaseContract;
import com.dicoding.movieandtvshow.db.FavTVShowHelper;
import com.dicoding.movieandtvshow.favourite.movies.FavouritesMovieItems;
import com.dicoding.movieandtvshow.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FavouritesTVShowsFragment extends Fragment implements LoadTVShowsCallback{

    private RecyclerView rvTVShows;
    private ProgressBar progressBar;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private FavouritesTVShowAdapter adapter;

    public FavouritesTVShowsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        adapter = new FavouritesTVShowAdapter(getActivity());
        rvTVShows = view.findViewById(R.id.rv_favourites);
        rvTVShows.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvTVShows.setAdapter(adapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getActivity());
        getActivity().getContentResolver().registerContentObserver(DatabaseContract.TVShowsColumns.CONTENT_URI, true, myObserver);

        if (savedInstanceState == null) {
            new LoadTVShowAsync(getActivity(), this).execute();
        } else {
            ArrayList<FavouritesTVShowsItems> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListTVShows(list);
            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListTVShows());
    }


    public void preExecute() {

      progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void postExecute(ArrayList<FavouritesTVShowsItems> tvshow) {
        progressBar.setVisibility(View.INVISIBLE);
        if (tvshow.size() > 0) {
            adapter.setListTVShows(tvshow);
        } else {
            adapter.setListTVShows(new ArrayList<FavouritesTVShowsItems>());
        }
    }

    private static class LoadTVShowAsync extends AsyncTask<Void, Void, ArrayList<FavouritesTVShowsItems>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadTVShowsCallback> weakCallback;

        private LoadTVShowAsync(Context context, LoadTVShowsCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<FavouritesTVShowsItems> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.TVShowsColumns.CONTENT_URI, null, null, null, null);
            return MappingHelper.mapCursorToArrayListTVShow(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<FavouritesTVShowsItems> tvshow) {
            super.onPostExecute(tvshow);
            weakCallback.get().postExecute(tvshow);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadTVShowAsync(context, (LoadTVShowsCallback) context).execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
