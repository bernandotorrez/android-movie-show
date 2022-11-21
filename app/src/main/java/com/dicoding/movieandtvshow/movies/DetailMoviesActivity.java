package com.dicoding.movieandtvshow.movies;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dicoding.movieandtvshow.R;
import com.dicoding.movieandtvshow.db.FavMovieHelper;
import com.dicoding.movieandtvshow.favourite.movies.FavouritesMovieItems;
import com.dicoding.movieandtvshow.helper.MappingHelper;

import java.util.ArrayList;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;
import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.ID;
import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.POPULARITY;
import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.TITLE;
import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.VOTE_AVERAGE;

public class DetailMoviesActivity extends AppCompatActivity {

    private TextView tvDetailMovieName, tvDetailMovieDesc, tvDetailMovieDuration,
    tvDetailMovieRating, tvDetailMoviePopularity;
    private ImageView ivDetailMoviePhoto;
    private Button btnBack, btnFavorite;
    private MovieViewModel movieViewModel;
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    public static final String EXTRA_MOVIE = "extra_movie";
    private String title = "Movie List";
    private final String STATE_TITLE = "state_string";

    private Uri uriWithId;
    private FavouritesMovieItems favouritesMovieItems;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movies);

        progressBar = findViewById(R.id.progressBar);
        tvDetailMovieName = findViewById(R.id.tv_detailmovies_name);
        tvDetailMovieDesc = findViewById(R.id.tv_detailmovies_desc);
        ivDetailMoviePhoto = findViewById(R.id.image_detailmovies);
        tvDetailMovieDuration = findViewById(R.id.tv_detailmovies_releasedate);
        tvDetailMovieRating = findViewById(R.id.tv_detailmovies_rating);
        tvDetailMoviePopularity = findViewById(R.id.tv_detailmovies_popularity);
        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);

        adapter = new MovieAdapter();
        adapter.notifyDataSetChanged();
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, getMovie);

        final MovieItems movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        Integer detailMovieID = movie.getId();
        movieViewModel.setDetailMovie(detailMovieID);

        final String detailMovieName = movie.getTitle();
        String detailMovieDesc = movie.getOverview();
        String img_url = movie.getPosterPath();
        String detailMovieDuration = movie.getReleaseDate();
        String detailMovieRating = movie.getVoteAverage();
        String detailMoviePopularity = movie.getPopularity();

        tvDetailMovieName.setPaintFlags(tvDetailMovieName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvDetailMovieName.setText(detailMovieName);
        tvDetailMovieDesc.setText(detailMovieDesc);
        tvDetailMovieDuration.setText(detailMovieDuration);
        tvDetailMovieRating.setText(detailMovieRating);
        tvDetailMoviePopularity.setText(detailMoviePopularity);
        tvDetailMovieDesc.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        showImage(img_url);

        title = detailMovieName;
        setActionBarTitle(title);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        uriWithId = Uri.parse(CONTENT_URI + "/" + detailMovieID);

        int cek_data = 0;

        if (uriWithId != null) {
            Cursor cursor = getContentResolver().query(uriWithId, null, null, null, null);
            if (cursor != null) {
                favouritesMovieItems = MappingHelper.mapCursorToObject(cursor);
                cek_data = favouritesMovieItems.getId();
                cursor.close();
            } else {
                cek_data = 0;
            }

        }

        if(cek_data > 0) {
            disableButtonFavourite();
        }

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                values.put(ID, movie.getId());
                values.put(TITLE, movie.getTitle());
                values.put(OVERVIEW, movie.getOverview());
                values.put(POPULARITY, movie.getPopularity());
                values.put(POSTER_PATH, movie.getPosterPath());
                values.put(RELEASE_DATE, movie.getReleaseDate());
                values.put(VOTE_AVERAGE, movie.getVoteAverage());

                String success_favourite = getString(R.string.success_favourite);

                int cek_data = 0;

                if (uriWithId != null) {
                    Cursor cursor = getContentResolver().query(uriWithId, null, null, null, null);
                    if (cursor != null) {
                        favouritesMovieItems = MappingHelper.mapCursorToObject(cursor);
                        cek_data = favouritesMovieItems.getId();
                        cursor.close();
                    } else {
                        cek_data = 0;
                    }

                }

                if(cek_data > 0) {
                    Toast.makeText(DetailMoviesActivity.this, getString(R.string.favourite), Toast.LENGTH_SHORT).show();
                } else {

                    Uri insert = getContentResolver().insert(CONTENT_URI, values);
                    int result = Integer.parseInt(insert.getLastPathSegment());

                    if (result > 0) {
                        disableButtonFavourite();
                        Toast.makeText(DetailMoviesActivity.this, success_favourite, Toast.LENGTH_SHORT).show();

                    } else {
                        String fail_favourite =  getString(R.string.fail_favourite);
                        Toast.makeText(DetailMoviesActivity.this, fail_favourite, Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void disableButtonFavourite() {
        String favourite = getString(R.string.favourite);
        btnFavorite.setText(favourite);
        btnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_white_24dp,0,0,0);
        btnFavorite.setEnabled(false);
        btnFavorite.setTextColor(Color.WHITE);
        btnFavorite.getBackground().setAlpha(128);
    }

    private Observer<ArrayList<MovieItems>> getMovie = new Observer<ArrayList<MovieItems>>() {

        @Override
        public void onChanged(ArrayList<MovieItems> movieItems) {

            if (movieItems != null) {
                adapter.setData(movieItems);

            }
        }


    };

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_TITLE, title);
    }

    private void showImage(String img_url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.image_loader)
                .fallback(R.drawable.image_broken)
                .error(R.drawable.image_broken)
                .override(125, 200)
                .priority(Priority.HIGH);

        Glide.with(this)
                .load(img_url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        showLoading(false);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        showLoading(false);
                        return false;
                    }
                })
                .apply(options)
                .into(ivDetailMoviePhoto);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
