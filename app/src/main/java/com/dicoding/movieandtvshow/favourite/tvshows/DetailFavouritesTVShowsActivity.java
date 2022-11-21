package com.dicoding.movieandtvshow.favourite.tvshows;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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

public class DetailFavouritesTVShowsActivity extends AppCompatActivity {

    private TextView tvDetailTVShowName, tvDetailTVShowDesc, tvDetailTVShowDuration,
            tvDetailTVShowRating, tvDetailTVShowPopularity;
    private ImageView ivDetailTVShowPhoto;
    private Button btnBack;
    private FavouritesTVShowAdapter adapter;
    private ProgressBar progressBar;
    public static final String EXTRA_TVSHOW = "extra_tvshow";
    public static final String EXTRA_POSITION = "extra_position";
    private String title = "TV Show List";
    private final String STATE_TITLE = "state_string";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_favourites_tvshows);

        progressBar = findViewById(R.id.progressBar);
        tvDetailTVShowName = findViewById(R.id.tv_tvshow_name);
        tvDetailTVShowDesc = findViewById(R.id.tv_tvshow_desc);
        ivDetailTVShowPhoto = findViewById(R.id.image_tvshow);
        tvDetailTVShowDuration = findViewById(R.id.tv_tvshow_releasedate);
        tvDetailTVShowRating = findViewById(R.id.tv_tvshow_rating);
        tvDetailTVShowPopularity = findViewById(R.id.tv_tvshow_popularity);
        btnBack = findViewById(R.id.btnBack);

        final FavouritesTVShowsItems tvShow = getIntent().getParcelableExtra(EXTRA_TVSHOW);
        final String detailTVShowName = tvShow.getTitle();
        String detailTVShowDesc = tvShow.getOverview();
        String img_url = tvShow.getPosterPath();
        String detailTVShowDuration = tvShow.getReleaseDate();
        String detailTVShowRating = tvShow.getVoteAverage();
        String detailTVShowPopularity = tvShow.getPopularity();

        tvDetailTVShowName.setPaintFlags(tvDetailTVShowName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvDetailTVShowName.setText(detailTVShowName);
        tvDetailTVShowDesc.setText(detailTVShowDesc);
        tvDetailTVShowDuration.setText(detailTVShowDuration);
        tvDetailTVShowRating.setText(detailTVShowRating);
        tvDetailTVShowPopularity.setText(detailTVShowPopularity);
        tvDetailTVShowDesc.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        showImage(img_url);

        title = detailTVShowName;
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
                .into(ivDetailTVShowPhoto);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
