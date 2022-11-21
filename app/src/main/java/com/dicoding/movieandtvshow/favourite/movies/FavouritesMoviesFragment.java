package com.dicoding.movieandtvshow.favourite.movies;


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


import com.dicoding.movieandtvshow.LoadMoviesCallback;
import com.dicoding.movieandtvshow.R;
import com.dicoding.movieandtvshow.db.DatabaseContract;
import com.dicoding.movieandtvshow.db.FavMovieHelper;
import com.dicoding.movieandtvshow.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.dicoding.movieandtvshow.helper.MappingHelper.mapCursorToArrayList;


public class FavouritesMoviesFragment extends Fragment implements LoadMoviesCallback {

    private RecyclerView rvMovies;
    private ProgressBar progressBar;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private FavouritesMovieAdapter adapter;

    public FavouritesMoviesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favourites_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);

        adapter = new FavouritesMovieAdapter(getActivity());
        rvMovies = view.findViewById(R.id.rv_favourites);
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvMovies.setAdapter(adapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getActivity());
        getActivity().getContentResolver().registerContentObserver(DatabaseContract.MovieColumns.CONTENT_URI, true, myObserver);

        if (savedInstanceState == null) {
            new LoadMoviesAsync(getActivity(), this).execute();
        } else {
            ArrayList<FavouritesMovieItems> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListMovies(list);
            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListMovies());
    }


    public void preExecute() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<FavouritesMovieItems> movies) {
        progressBar.setVisibility(View.INVISIBLE);
        if (movies.size() > 0) {
            adapter.setListMovies(movies);
        } else {
            adapter.setListMovies(new ArrayList<FavouritesMovieItems>());
        }
    }

    private static class LoadMoviesAsync extends AsyncTask<Void, Void, ArrayList<FavouritesMovieItems>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMoviesCallback> weakCallback;

        private LoadMoviesAsync(Context context, LoadMoviesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<FavouritesMovieItems> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.MovieColumns.CONTENT_URI, null, null, null, null);
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<FavouritesMovieItems> movie) {
            super.onPostExecute(movie);
            weakCallback.get().postExecute(movie);
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
            new LoadMoviesAsync(context, (LoadMoviesCallback) context).execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
