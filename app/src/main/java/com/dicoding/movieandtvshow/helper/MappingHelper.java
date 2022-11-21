package com.dicoding.movieandtvshow.helper;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.dicoding.movieandtvshow.db.DatabaseContract;
import com.dicoding.movieandtvshow.favourite.movies.FavouritesMovieItems;
import com.dicoding.movieandtvshow.favourite.tvshows.FavouritesTVShowsItems;

import java.util.ArrayList;

import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.*;

public class MappingHelper {
    public static ArrayList<FavouritesMovieItems> mapCursorToArrayList(Cursor favMovieCursor) {
        ArrayList<FavouritesMovieItems> favMovieList = new ArrayList<>();
        while (favMovieCursor.moveToNext()) {
            int id = favMovieCursor.getInt(favMovieCursor.getColumnIndexOrThrow(ID));
            String title = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(TITLE));
            String posterPath = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(POSTER_PATH));
            String voteAverage = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(VOTE_AVERAGE));
            String overview = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(OVERVIEW));
            String releaseDate = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(RELEASE_DATE));
            String popularity = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(POPULARITY));

            favMovieList.add(new FavouritesMovieItems(id, title, posterPath, voteAverage, overview, releaseDate, popularity));
        }
        return favMovieList;
    }

    public static FavouritesMovieItems mapCursorToObject(Cursor favMovieCursor) {
        if(favMovieCursor.getCount() > 0) {
            int id = favMovieCursor.getInt(favMovieCursor.getColumnIndexOrThrow(ID));
            String title = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(TITLE));
            String posterPath = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(POSTER_PATH));
            String voteAverage = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(VOTE_AVERAGE));
            String overview = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(OVERVIEW));
            String releaseDate = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(RELEASE_DATE));
            String popularity = favMovieCursor.getString(favMovieCursor.getColumnIndexOrThrow(POPULARITY));
            return new FavouritesMovieItems(id, title, posterPath, voteAverage, overview, releaseDate, popularity);
        } else {
            int id = 0;
            String title = null;
            String posterPath = null;
            String voteAverage = null;
            String overview = null;
            String releaseDate = null;
            String popularity = null;
            return new FavouritesMovieItems(id, title, posterPath, voteAverage, overview, releaseDate, popularity);
        }

    }

    public static ArrayList<FavouritesTVShowsItems> mapCursorToArrayListTVShow(Cursor favTVShowCursor) {
        ArrayList<FavouritesTVShowsItems> favTVShowList = new ArrayList<>();
        while (favTVShowCursor.moveToNext()) {
            int id = favTVShowCursor.getInt(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.ID));
            String title = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.TITLE));
            String posterPath = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.POSTER_PATH));
            String voteAverage = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.VOTE_AVERAGE));
            String overview = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.OVERVIEW));
            String releaseDate = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.RELEASE_DATE));
            String popularity = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.POPULARITY));

            favTVShowList.add(new FavouritesTVShowsItems(id, title, posterPath, voteAverage, overview, releaseDate, popularity));
        }
        return favTVShowList;
    }

    public static FavouritesTVShowsItems mapCursorToObjectTVShow(Cursor favTVShowCursor) {
        if(favTVShowCursor.getCount() > 0) {
            int id = favTVShowCursor.getInt(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.ID));
            String title = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.TITLE));
            String posterPath = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.POSTER_PATH));
            String voteAverage = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.VOTE_AVERAGE));
            String overview = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.OVERVIEW));
            String releaseDate = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.RELEASE_DATE));
            String popularity = favTVShowCursor.getString(favTVShowCursor.getColumnIndexOrThrow(DatabaseContract.TVShowsColumns.POPULARITY));
            return new FavouritesTVShowsItems(id, title, posterPath, voteAverage, overview, releaseDate, popularity);
        } else {
            int id = 0;
            String title = null;
            String posterPath = null;
            String voteAverage = null;
            String overview = null;
            String releaseDate = null;
            String popularity = null;
            return new FavouritesTVShowsItems(id, title, posterPath, voteAverage, overview, releaseDate, popularity);
        }

    }
}
