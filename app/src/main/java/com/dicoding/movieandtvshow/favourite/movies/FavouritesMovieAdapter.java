package com.dicoding.movieandtvshow.favourite.movies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.movieandtvshow.CustomOnItemClickListener;
import com.dicoding.movieandtvshow.R;
import com.dicoding.movieandtvshow.db.FavMovieHelper;

import java.util.ArrayList;

import static com.dicoding.movieandtvshow.db.DatabaseContract.MovieColumns.CONTENT_URI;

public class FavouritesMovieAdapter extends RecyclerView.Adapter<FavouritesMovieAdapter.FavouritesMovieViewHolder> {

    private ArrayList<FavouritesMovieItems> listMovies = new ArrayList<>();
    private Activity activity;

    private Uri uriWithId;

    public FavouritesMovieAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<FavouritesMovieItems> getListMovies() {
        return listMovies;
    }

    public void removeItem(int position) {
        this.listMovies.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listMovies.size());
        //notifyDataSetChanged();
    }


    public void setListMovies(ArrayList<FavouritesMovieItems> listMovies) {
        if (listMovies.size() > 0) {
            this.listMovies.clear();
        }
        this.listMovies.addAll(listMovies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouritesMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_favourite_movie, parent, false);
        return new FavouritesMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavouritesMovieViewHolder holder, int position) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.image_loader)
                .fallback(R.drawable.image_broken)
                .error(R.drawable.image_broken)
                .override(125, 200)
                .priority(Priority.HIGH);

        Glide.with(holder.itemView.getContext())
                .load(listMovies.get(position).getPosterPath())
                .apply(options)
                .into(holder.imgPoster);
        holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tvName.setText(listMovies.get(position).getTitle());
        holder.tvReleaseDate.setText(listMovies.get(position).getReleaseDate());
        holder.tvRating.setText(listMovies.get(position).getVoteAverage());
        holder.tvPopularity.setText(listMovies.get(position).getPopularity());
        holder.tvSeeMore.setPaintFlags(holder.tvSeeMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tvSeeMore.setText(holder.itemView.getContext().getString(R.string.see_more));

        holder.btnFavorite.setEnabled(true);

        holder.btnFavorite.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {

                String success_delete_favourite = view.getContext().getString(R.string.success_delete_favourite);
                String fail_favourite =  view.getContext().getString(R.string.fail_favourite);

                uriWithId = Uri.parse(CONTENT_URI + "/" + listMovies.get(position).getId());

                int delete = activity.getContentResolver().delete(uriWithId, null, null);

                holder.btnFavorite.setEnabled(false);

                if (delete > 0) {
                    Toast.makeText(view.getContext(), success_delete_favourite, Toast.LENGTH_SHORT).show();
                    removeItem(position);


                } else {
                    holder.btnFavorite.setEnabled(true);
                    Toast.makeText(view.getContext(), fail_favourite, Toast.LENGTH_SHORT).show();

                }

            }
        }));

        holder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, DetailFavouritesMoviesActivity.class);
                intent.putExtra(DetailFavouritesMoviesActivity.EXTRA_POSITION, position);
                intent.putExtra(DetailFavouritesMoviesActivity.EXTRA_MOVIE, listMovies.get(position));
                activity.startActivity(intent);
            }
        }));

    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class FavouritesMovieViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvRating, tvReleaseDate, tvPopularity, tvSeeMore;
        CardView cvMovie;
        Button btnFavorite;
        ImageView imgPoster;

        public FavouritesMovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.img_item_photo);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvRating = itemView.findViewById(R.id.tv_item_rating);
            tvReleaseDate = itemView.findViewById(R.id.tv_item_releasedate);
            tvPopularity = itemView.findViewById(R.id.tv_item_popularity);
            tvSeeMore = itemView.findViewById(R.id.tv_see_more);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
            cvMovie = itemView.findViewById(R.id.card_view);
        }
    }
}
