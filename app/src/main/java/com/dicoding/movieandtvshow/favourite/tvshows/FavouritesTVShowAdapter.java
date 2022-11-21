package com.dicoding.movieandtvshow.favourite.tvshows;

import android.app.Activity;
import android.content.Intent;
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
import com.dicoding.movieandtvshow.db.FavTVShowHelper;

import java.util.ArrayList;

import static com.dicoding.movieandtvshow.db.DatabaseContract.TVShowsColumns.CONTENT_URI;

public class FavouritesTVShowAdapter extends RecyclerView.Adapter<FavouritesTVShowAdapter.FavouritesTVShowViewHolder> {

    private ArrayList<FavouritesTVShowsItems> listTVShows = new ArrayList<>();
    private Activity activity;

    private Uri uriWithId;

    public FavouritesTVShowAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<FavouritesTVShowsItems> getListTVShows() {
        return listTVShows;
    }

    public void removeItem(int position) {
        this.listTVShows.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listTVShows.size());
    }


    public void setListTVShows(ArrayList<FavouritesTVShowsItems> listMovies) {
        if (listMovies.size() > 0) {
            this.listTVShows.clear();
        }
        this.listTVShows.addAll(listMovies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouritesTVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_favourite_tvshow, parent, false);
        return new FavouritesTVShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavouritesTVShowViewHolder holder, int position) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.image_loader)
                .fallback(R.drawable.image_broken)
                .error(R.drawable.image_broken)
                .override(125, 200)
                .priority(Priority.HIGH);

        Glide.with(holder.itemView.getContext())
                .load(listTVShows.get(position).getPosterPath())
                .apply(options)
                .into(holder.imgPoster);
        holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tvName.setText(listTVShows.get(position).getTitle());
        holder.tvReleaseDate.setText(listTVShows.get(position).getReleaseDate());
        holder.tvRating.setText(listTVShows.get(position).getVoteAverage());
        holder.tvPopularity.setText(listTVShows.get(position).getPopularity());
        holder.tvSeeMore.setPaintFlags(holder.tvSeeMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tvSeeMore.setText(holder.itemView.getContext().getString(R.string.see_more));

        holder.btnFavorite.setEnabled(true);

        holder.btnFavorite.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {

                String success_delete_favourite = view.getContext().getString(R.string.success_delete_favourite);
                String fail_favourite =  view.getContext().getString(R.string.fail_favourite);

                holder.btnFavorite.setEnabled(false);

                uriWithId = Uri.parse(CONTENT_URI + "/" + listTVShows.get(position).getId());

                int delete = activity.getContentResolver().delete(uriWithId, null, null);

                Log.d("Delete TVShow", String.valueOf(delete));

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
                Intent intent = new Intent(activity, DetailFavouritesTVShowsActivity.class);
                intent.putExtra(DetailFavouritesTVShowsActivity.EXTRA_POSITION, position);
                intent.putExtra(DetailFavouritesTVShowsActivity.EXTRA_TVSHOW, listTVShows.get(position));
                activity.startActivity(intent);
            }
        }));



    }

    @Override
    public int getItemCount() {
        return listTVShows.size();
    }

    public class FavouritesTVShowViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvRating, tvReleaseDate, tvPopularity, tvSeeMore;
        CardView cvMovie;
        Button btnFavorite;
        ImageView imgPoster;

        public FavouritesTVShowViewHolder(@NonNull View itemView) {
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
