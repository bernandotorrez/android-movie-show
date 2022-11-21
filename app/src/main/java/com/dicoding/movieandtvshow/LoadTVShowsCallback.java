package com.dicoding.movieandtvshow;

import com.dicoding.movieandtvshow.favourite.tvshows.FavouritesTVShowsItems;

import java.util.ArrayList;

public interface LoadTVShowsCallback {
    void preExecute();
    void postExecute(ArrayList<FavouritesTVShowsItems> tvshow);
}
