package com.dicoding.movieandtvshow.favourite.movies;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dicoding.movieandtvshow.R;

public class DetailFavouritesMoviesActivity extends AppCompatActivity {

    private TextView tvDetailMovieName, tvDetailMovieDesc, tvDetailMovieDuration,
            tvDetailMovieRating, tvDetailMoviePopularity;
    private ImageView ivDetailMoviePhoto;
    private Button btnBack;
    private ProgressBar progressBar;
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_POSITION = "extra_position";
    private String title = "Movie List";
    private final String STATE_TITLE = "state_string";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_favourites_movies);

        progressBar = findViewById(R.id.progressBar);
        tvDetailMovieName = findViewById(R.id.tv_detailmovies_name);
        tvDetailMovieDesc = findViewById(R.id.tv_detailmovies_desc);
        ivDetailMoviePhoto = findViewById(R.id.image_detailmovies);
        tvDetailMovieDuration = findViewById(R.id.tv_detailmovies_releasedate);
        tvDetailMovieRating = findViewById(R.id.tv_detailmovies_rating);
        tvDetailMoviePopularity = findViewById(R.id.tv_detailmovies_popularity);
        btnBack = findViewById(R.id.btnBack);

        final FavouritesMovieItems movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
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
        tvDetailMovieDesc.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        showImage(img_url);

        title = detailMovieName;
        setActionBarTitle(title);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
