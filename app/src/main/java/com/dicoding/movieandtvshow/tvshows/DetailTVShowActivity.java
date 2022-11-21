package com.dicoding.movieandtvshow.tvshows;

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
import com.dicoding.movieandtvshow.db.FavTVShowHelper;
import com.dicoding.movieandtvshow.favourite.tvshows.FavouritesTVShowsItems;
import com.dicoding.movieandtvshow.helper.MappingHelper;

import java.util.ArrayList;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;
import static com.dicoding.movieandtvshow.db.DatabaseContract.TVShowsColumns.*;

public class DetailTVShowActivity extends AppCompatActivity {

    private TextView tvDetailTVShowName, tvDetailTVShowDesc, tvDetailTVShowDuration,
            tvDetailTVShowRating, tvDetailTVShowPopularity;
    private ImageView ivDetailTVShowPhoto;
    private Button btnBack, btnFavorite;
    private TVShowViewModel tvShowViewModel;
    private TVShowAdapter adapter;
    private ProgressBar progressBar;
    public static final String EXTRA_TVSHOW = "extra_tvshow";
    private String title = "TV Show List";
    private final String STATE_TITLE = "state_string";

    private Uri uriWithId;
    private FavouritesTVShowsItems favouritesTVShowsItems;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tvshow);

        progressBar = findViewById(R.id.progressBar);
        tvDetailTVShowName = findViewById(R.id.tv_tvshow_name);
        tvDetailTVShowDesc = findViewById(R.id.tv_tvshow_desc);
        ivDetailTVShowPhoto = findViewById(R.id.image_tvshow);
        tvDetailTVShowDuration = findViewById(R.id.tv_tvshow_releasedate);
        tvDetailTVShowRating = findViewById(R.id.tv_tvshow_rating);
        tvDetailTVShowPopularity = findViewById(R.id.tv_tvshow_popularity);
        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);

        adapter = new TVShowAdapter();
        adapter.notifyDataSetChanged();
        tvShowViewModel = ViewModelProviders.of(this).get(TVShowViewModel.class);
        tvShowViewModel.getTVShow().observe(this, getTVShow);

        final TVShowItems tvShow = getIntent().getParcelableExtra(EXTRA_TVSHOW);
        Integer detailMovieID = tvShow.getId();
        tvShowViewModel.setDetailTVShow(detailMovieID);

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
        tvDetailTVShowDesc.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        showImage(img_url);

        title = detailTVShowName;
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
                favouritesTVShowsItems = MappingHelper.mapCursorToObjectTVShow(cursor);
                cek_data = favouritesTVShowsItems.getId();
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
                values.put(ID, tvShow.getId());
                values.put(TITLE, tvShow.getTitle());
                values.put(OVERVIEW, tvShow.getOverview());
                values.put(POPULARITY, tvShow.getPopularity());
                values.put(POSTER_PATH, tvShow.getPosterPath());
                values.put(RELEASE_DATE, tvShow.getReleaseDate());
                values.put(VOTE_AVERAGE, tvShow.getVoteAverage());


                String success_favourite = getString(R.string.success_favourite);

                int cek_data = 0;

                if (uriWithId != null) {
                    Cursor cursor = getContentResolver().query(uriWithId, null, null, null, null);
                    if (cursor != null) {
                        favouritesTVShowsItems = MappingHelper.mapCursorToObjectTVShow(cursor);
                        cek_data = favouritesTVShowsItems.getId();
                        cursor.close();
                    } else {
                        cek_data = 0;
                    }

                }

                if(cek_data > 0) {
                    Toast.makeText(DetailTVShowActivity.this, getString(R.string.favourite), Toast.LENGTH_SHORT).show();
                } else {
                    Uri insert = getContentResolver().insert(CONTENT_URI, values);
                    int result = Integer.parseInt(insert.getLastPathSegment());
                    if (result > 0) {
                        disableButtonFavourite();
                        Toast.makeText(DetailTVShowActivity.this, success_favourite, Toast.LENGTH_SHORT).show();

                    } else {
                        String fail_favourite =  getString(R.string.fail_favourite);
                        Toast.makeText(DetailTVShowActivity.this, fail_favourite, Toast.LENGTH_SHORT).show();
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

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void disableButtonFavourite() {
        String favourite = getString(R.string.favourite);
        btnFavorite.setText(favourite);
        btnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_white_24dp,0,0,0);
        btnFavorite.setEnabled(false);
        btnFavorite.setTextColor(Color.WHITE);
        btnFavorite.getBackground().setAlpha(128);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_TITLE, title);
    }

    private Observer<ArrayList<TVShowItems>> getTVShow = new Observer<ArrayList<TVShowItems>>() {

        @Override
        public void onChanged(ArrayList<TVShowItems> tvShowItems) {

            if (tvShowItems != null) {
                adapter.setData(tvShowItems);

            }
        }


    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
