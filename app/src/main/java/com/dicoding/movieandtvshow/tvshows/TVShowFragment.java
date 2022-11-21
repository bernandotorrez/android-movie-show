package com.dicoding.movieandtvshow.tvshows;


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

public class TVShowFragment extends Fragment {

    private TVShowAdapter adapter;
    private TVShowViewModel tvShowViewModel;

    private ProgressBar progressBar;


    public TVShowFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        adapter = new TVShowAdapter();
        adapter.notifyDataSetChanged();
        RecyclerView recyclerView = view.findViewById(R.id.rv_tvshow);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        tvShowViewModel = ViewModelProviders.of(this).get(TVShowViewModel.class);

        String tvshow;
        if (getArguments() != null) {
            tvshow = getArguments().getString("params");
        } else {
            tvshow = "null";
        }

        tvShowViewModel.setTVShow(tvshow);
        tvShowViewModel.getTVShow().observe(this, getTVShow);

        adapter.setOnItemClickCallback(new TVShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TVShowItems data) {

                data.setId(data.getId());
                Intent detailTVShow = new Intent(getActivity(), DetailTVShowActivity.class);
                detailTVShow.putExtra(DetailTVShowActivity.EXTRA_TVSHOW, data);
                startActivity(detailTVShow);
            }
        });
    }

    private Observer<ArrayList<TVShowItems>> getTVShow = new Observer<ArrayList<TVShowItems>>() {

        @Override
        public void onChanged(ArrayList<TVShowItems> tvShowItems) {

            if (tvShowItems != null) {
                adapter.setData(tvShowItems);
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
