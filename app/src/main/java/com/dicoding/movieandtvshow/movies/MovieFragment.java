package com.dicoding.movieandtvshow.movies;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dicoding.movieandtvshow.R;

import java.util.ArrayList;

public class MovieFragment extends Fragment {

    private MovieAdapter adapter;
    private MovieViewModel movieViewModel;
    public static final String EXTRA_SEARCH = "extra_movie";

    private ProgressBar progressBar;


    public MovieFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_movie, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        adapter = new MovieAdapter();
        adapter.notifyDataSetChanged();
        RecyclerView recyclerView = view.findViewById(R.id.rv_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        String movie;
        if (getArguments() != null) {
            movie = getArguments().getString("params");
        } else {
            movie = "null";
        }

        movieViewModel.setMovie(movie);
        movieViewModel.getMovies().observe(this, getMovie);

        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(MovieItems data) {

                data.setId(data.getId());
                Intent detailMovies = new Intent(getActivity(), DetailMoviesActivity.class);
                detailMovies.putExtra(DetailMoviesActivity.EXTRA_MOVIE, data);
                startActivity(detailMovies);
            }
        });


    }

    private Observer<ArrayList<MovieItems>> getMovie = new Observer<ArrayList<MovieItems>>() {

        @Override
        public void onChanged(ArrayList<MovieItems> movieItems) {

            if (movieItems != null) {
                adapter.setData(movieItems);
                showLoading(false);
            } else {
                Toast.makeText(getActivity(), R.string.data_not_found, Toast.LENGTH_SHORT).show();
                showLoading(false);
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
