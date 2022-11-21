package com.dicoding.movieandtvshow;

import android.database.Cursor;

import com.dicoding.movieandtvshow.favourite.movies.FavouritesMovieItems;

import java.util.ArrayList;

public interface LoadMoviesCallback {
    void preExecute();
    void postExecute(ArrayList<FavouritesMovieItems> movie);
    //void postExecute(Cursor favMovie);
}
