package com.dicoding.movieandtvshow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;

import android.content.Intent;


import com.dicoding.movieandtvshow.favourite.movies.FavouritesMovieAdapter;
import com.dicoding.movieandtvshow.favourite.movies.FavouritesMovieItems;
import com.dicoding.movieandtvshow.favourite.movies.FavouritesMoviesFragment;
import com.dicoding.movieandtvshow.favourite.tvshows.FavouritesTVShowsFragment;

import com.dicoding.movieandtvshow.reminder.ReminderSettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class FavouriteListActivity extends AppCompatActivity implements LoadMoviesCallback{

    private Fragment fragment;
    private String title = "Favourited Movie";
    private final String STATE_TITLE = "state_string";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    fragment = new FavouritesMoviesFragment();
                    String title_menu_movie = String.format(getResources().getString(R.string.title_fav_movie));
                    title = title_menu_movie;
                    setActionBarTitle(title);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_tvshow:
                    fragment = new FavouritesTVShowsFragment();
                    String title_menu_tvshow = String.format(getResources().getString(R.string.title_fav_tvshow));
                    title = title_menu_tvshow;
                    setActionBarTitle(title);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_setting:
                    fragment = new ReminderSettingFragment();
                    String title_menu_reminder = String.format(getResources().getString(R.string.reminder_setting));
                    title = title_menu_reminder;
                    setActionBarTitle(title);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setActionBarTitle(title);

        if (savedInstanceState == null){
            navView.setSelectedItemId(R.id.navigation_movie);
        } else {
            title = savedInstanceState.getString(STATE_TITLE);
            setActionBarTitle(title);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_TITLE, title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.favourite_list);
        item.setVisible(false);

        MenuItem item1 = menu.findItem(R.id.back_to_list);
        item1.setVisible(false);

        menu.findItem(R.id.search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.action_language:
                Intent language = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(language);
                break;

        }
    }

    public void preExecute() {

    }

    @Override
    public void postExecute(ArrayList<FavouritesMovieItems> movies) {
    }
}