package com.dicoding.movieandtvshow;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dicoding.movieandtvshow.alarm.AlarmReceiver;
import com.dicoding.movieandtvshow.db.ReminderHelper;
import com.dicoding.movieandtvshow.movies.MovieFragment;
import com.dicoding.movieandtvshow.reminder.ReminderSettingFragment;
import com.dicoding.movieandtvshow.tvshows.TVShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private String title = "Movie List";
    private String position = "movie";
    private final String STATE_TITLE = "state_string";

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    private AlarmReceiver alarmReceiver;
    private ReminderHelper reminderHelper;

    final ArrayList<String> todayReminderMovie = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    fragment = new MovieFragment();
                    String title_menu_movie = String.format(getResources().getString(R.string.title_menu_movie));
                    title = title_menu_movie;
                    setActionBarTitle(title);
                    position = "movie";
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_tvshow:
                    fragment = new TVShowFragment();
                    String title_menu_tvshow = String.format(getResources().getString(R.string.title_menu_tvshow));
                    title = title_menu_tvshow;
                    setActionBarTitle(title);
                    position = "tvshow";
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_setting:
                    fragment = new ReminderSettingFragment();
                    String title_menu_reminder = String.format(getResources().getString(R.string.reminder_setting));
                    title = title_menu_reminder;
                    setActionBarTitle(title);
                    position = "setting";

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

        reminderHelper = ReminderHelper.getInstance(this);
        reminderHelper.open();

        if (savedInstanceState == null){
            navView.setSelectedItemId(R.id.navigation_movie);
        } else {
            title = savedInstanceState.getString(STATE_TITLE);
            setActionBarTitle(title);
        }
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item1 = menu.findItem(R.id.back_to_list);
        item1.setVisible(false);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            final SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    Boolean isEmptyFields = true;
                    Boolean isMinimalSearch = true;
                    String inputQuery = query.trim();

                    if(inputQuery == null || inputQuery.length() == 0) {
                        Toast.makeText(MainActivity.this, "Please fill the Form", Toast.LENGTH_SHORT).show();
                    } else {
                        isEmptyFields = false;
                    }

                    if(inputQuery.length() < 3) {
                        Toast.makeText(MainActivity.this, "Fill with Minimal 3 Characters", Toast.LENGTH_SHORT).show();
                    } else {
                        isMinimalSearch = false;
                    }

                    if(position == "movie" && isEmptyFields == false && isMinimalSearch == false) {
                        fragment = new MovieFragment();
                        String title_menu_movie = String.format(getResources().getString(R.string.title_menu_movie));
                        title = title_menu_movie;
                        setActionBarTitle(title);

                        Bundle bundle = new Bundle();
                        bundle.putString("params", query);
                        fragment.setArguments(bundle);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                                .commit();
                        return true;
                    } else if(position == "tvshow" && isEmptyFields == false && isMinimalSearch == false) {
                        fragment = new TVShowFragment();
                        String title_menu_tvshow = String.format(getResources().getString(R.string.title_menu_tvshow));
                        title = title_menu_tvshow;
                        setActionBarTitle(title);

                        Bundle bundle = new Bundle();
                        bundle.putString("params", query);
                        fragment.setArguments(bundle);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                                .commit();
                        return true;
                    } else {
                        return false;
                    }


                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });


        }

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

            case R.id.favourite_list:
                Intent favourite_list = new Intent(MainActivity.this, FavouriteListActivity.class);
                startActivity(favourite_list);
                break;

        }
    }


}
