package com.udacity.luisev96.popularmovies.presentation.movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.luisev96.popularmovies.R;
import com.udacity.luisev96.popularmovies.databinding.MovieItemBinding;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.presentation.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.udacity.luisev96.popularmovies.presentation.movies.MoviesActivity.MOVIE;

/**
 * Created by Luis Vargas on 2019-08-20.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> mMoviesList = new ArrayList<>();
    private Context mContext;
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w185";

    MoviesAdapter(Context context) {
        mContext = context;
    }

    public void setMovies(List<Movie> mMoviesList) {
        this.mMoviesList = mMoviesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieItemBinding movieItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.movie_item, parent, false);
        return new MoviesViewHolder(movieItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        holder.bind(mMoviesList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MovieItemBinding mMovieItemBinding;
        Movie mMovie;

        MoviesViewHolder(@NonNull MovieItemBinding movieItemBinding) {
            super(movieItemBinding.getRoot());
            mMovieItemBinding = movieItemBinding;
            itemView.setOnClickListener(this);
        }

        void bind(Movie movie) {
            mMovie = movie;
            Picasso
                    .get()
                    .load(Uri.parse(BASE_URL + movie.getPosterPath()))
                    .into(mMovieItemBinding.ivMovieImage);

            mMovieItemBinding.tvMovieAverage.setText(String.valueOf(movie.getVoteAverage()));
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(MOVIE, mMovie);
            mContext.startActivity(intent);
        }
    }
}
