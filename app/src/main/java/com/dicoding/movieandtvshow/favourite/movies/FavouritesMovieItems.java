package com.dicoding.movieandtvshow.favourite.movies;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dicoding.movieandtvshow.db.DatabaseContract;

import static com.dicoding.movieandtvshow.db.DatabaseContract.getColumnInt;
import static com.dicoding.movieandtvshow.db.DatabaseContract.getColumnString;

public class FavouritesMovieItems implements Parcelable {

    private int id;
    private String title, posterPath, voteAverage, overview, releaseDate, popularity;

    public FavouritesMovieItems() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.voteAverage);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.popularity);
    }

    public FavouritesMovieItems(int id, String title, String posterPath, String voteAverage, String overview, String releaseDate, String popularity) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
    }
    public FavouritesMovieItems(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.MovieColumns.ID);
        this.title = getColumnString(cursor, DatabaseContract.MovieColumns.TITLE);
        this.posterPath = getColumnString(cursor, DatabaseContract.MovieColumns.POSTER_PATH);
        this.voteAverage = getColumnString(cursor, DatabaseContract.MovieColumns.VOTE_AVERAGE);
        this.overview = getColumnString(cursor, DatabaseContract.MovieColumns.OVERVIEW);
        this.releaseDate = getColumnString(cursor, DatabaseContract.MovieColumns.RELEASE_DATE);
        this.popularity = getColumnString(cursor, DatabaseContract.MovieColumns.POPULARITY);
    }

    protected FavouritesMovieItems(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.voteAverage = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.popularity = in.readString();
    }

    public static final Parcelable.Creator<FavouritesMovieItems> CREATOR = new Parcelable.Creator<FavouritesMovieItems>() {
        @Override
        public FavouritesMovieItems createFromParcel(Parcel source) {
            return new FavouritesMovieItems(source);
        }

        @Override
        public FavouritesMovieItems[] newArray(int size) {
            return new FavouritesMovieItems[size];
        }
    };
}
