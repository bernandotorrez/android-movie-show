package com.dicoding.movieandtvshow.tvshows;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dicoding.movieandtvshow.BuildConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TVShowViewModel extends ViewModel {

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<ArrayList<TVShowItems>> listTVShow = new MutableLiveData<>();

    LiveData<ArrayList<TVShowItems>> getTVShow() {
        return listTVShow;
    }

    void setTVShow(String search) {
        String search_tvshow = search.toString();

        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TVShowItems> listItems = new ArrayList<>();

        String url;
        if(search_tvshow == "null" || search_tvshow == null) {
            url = "https://api.themoviedb.org/3/discover/tv?api_key="+API_KEY+"&language=en-US";

        } else {
            url = "https://api.themoviedb.org/3/search/tv?api_key="+API_KEY+"&language=en-US&query="+search_tvshow;
        }

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tvShow = list.getJSONObject(i);
                        TVShowItems tvShowItems = new TVShowItems(tvShow);
                        listItems.add(tvShowItems);
                    }
                    listTVShow.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    void setDetailTVShow(Integer id) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TVShowItems> listItems = new ArrayList<>();

        String url = "https://api.themoviedb.org/3/tv/"+id+"?api_key="+API_KEY+"&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    TVShowItems tvShowItems = new TVShowItems(responseObject);
                    listItems.add(tvShowItems);
                    listTVShow.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

}
