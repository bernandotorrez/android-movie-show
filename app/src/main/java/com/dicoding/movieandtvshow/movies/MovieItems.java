package com.dicoding.movieandtvshow.movies;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dicoding.movieandtvshow.db.DatabaseContract;

import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static com.dicoding.movieandtvshow.db.DatabaseContract.getColumnInt;
import static com.dicoding.movieandtvshow.db.DatabaseContract.getColumnString;

public class MovieItems implements Parcelable {

    private int id;
    private String title, posterPath, voteAverage, overview, releaseDate, popularity;

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

    public MovieItems() {

    }

    MovieItems(JSONObject object) {

        try {
            int id = object.getInt("id");
            String title = object.getString("title");
            String posterPath = object.getString("poster_path");
            String voteAverage = object.getString("vote_average");
            String overview = object.getString("overview");
            String releaseDate = object.getString("release_date");
            String popularity = object.getString("popularity");

            posterPath = "https://image.tmdb.org/t/p/w342"+posterPath;
            this.id = id;
            this.title = title;
            this.posterPath = posterPath;
            this.voteAverage = voteAverage+" / 10";
            this.overview = overview;
            this.releaseDate = releaseDate;
            this.popularity = popularity;
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    protected MovieItems(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.voteAverage = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.popularity = in.readString();
    }

    public static final Creator<MovieItems> CREATOR = new Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel source) {
            return new MovieItems(source);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };
}
