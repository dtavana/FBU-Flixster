package com.dtavana.flixter.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtavana.flixter.GlideApp;
import com.dtavana.flixter.activities.MovieDetailsActivity;
import com.dtavana.flixter.R;
import com.dtavana.flixter.databinding.ItemMovieBinding;
import com.dtavana.flixter.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public static final String TAG = "MovieAdapter";

    Context ctx;
    List<Movie> movies;

    public MovieAdapter(Context ctx, List<Movie> movies) {
        this.ctx = ctx;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        ItemMovieBinding binding = ItemMovieBinding.inflate(LayoutInflater.from(ctx), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    // Involves populating data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        // Bind the movie data into the VH
        holder.bind(movie);
    }

    // Return total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemMovieBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            binding = ItemMovieBinding.bind(itemView);
        }

        public void bind(Movie movie) {
            binding.tvTitle.setText(movie.getTitle());
            binding.tvOverview.setText(movie.getOverview());
            boolean landscape = ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            String imageUrl = landscape ? movie.getBackgroundPath() : movie.getPosterPath();
            int placeholder = landscape ? R.drawable.flicks_backdrop_placeholder : R.drawable.flicks_movie_placeholder;
            final int RADIUS = 30;
            final int MARGIN = 10;
            GlideApp.with(ctx)
                    .load(imageUrl)
                    .transform(new RoundedCornersTransformation(RADIUS, MARGIN))
                    .placeholder(placeholder)
                    .into(binding.ivPoster);
        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);
                Intent i = new Intent(ctx, MovieDetailsActivity.class);
                i.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                ctx.startActivity(i);
            }
        }
    }
}
