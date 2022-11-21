package com.dicoding.movieandtvshow.tvshows;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.movieandtvshow.R;

import java.util.ArrayList;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder> {

    private ArrayList<TVShowItems> mData = new ArrayList<>();
    public void setData(ArrayList<TVShowItems> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    private TVShowAdapter.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(TVShowAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(TVShowItems data);
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_tvshow, viewGroup, false);
        return new TVShowViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.bind(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class TVShowViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPoster;
        TextView tvName, tvRating, tvReleaseDate, tvPopularity, tvSeeMore;

        public TVShowViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_item_photo);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvRating = itemView.findViewById(R.id.tv_item_rating);
            tvReleaseDate = itemView.findViewById(R.id.tv_item_releasedate);
            tvPopularity = itemView.findViewById(R.id.tv_item_popularity);
            tvSeeMore = itemView.findViewById(R.id.tv_see_more);
        }

        void bind(final TVShowItems tvShowItem) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.image_loader)
                    .fallback(R.drawable.image_broken)
                    .error(R.drawable.image_broken)
                    .override(125, 200)
                    .priority(Priority.HIGH);

            Glide.with(itemView.getContext())
                    .load(tvShowItem.getPosterPath())
                    .apply(options)
                    .into(imgPoster);
            tvName.setPaintFlags(tvName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvName.setText(tvShowItem.getTitle());
            tvReleaseDate.setText(tvShowItem.getReleaseDate());
            tvRating.setText(tvShowItem.getVoteAverage());
            tvPopularity.setText(tvShowItem.getPopularity());
            tvSeeMore.setPaintFlags(tvSeeMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvSeeMore.setText(itemView.getContext().getString(R.string.see_more));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(mData.get(getAdapterPosition()));

                }
            });
        }
    }
}
