package com.udacity.luisev96.popularmovies;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.luisev96.popularmovies.databinding.MovieItemBinding;

import java.util.List;

/**
 * Created by Luis Vargas on 2019-08-20.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> mMoviesList;
    private MovieClickListener mMovieClickListener;
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w185";

    public interface MovieClickListener{
        void movieClicked(Movie movie);
    }

    public MoviesAdapter(List<Movie> moviesList, MovieClickListener movieClickListener) {
        mMoviesList = moviesList;
        mMovieClickListener = movieClickListener;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieItemBinding movieItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.movie_item, parent, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(movieItemBinding);
        return viewHolder;
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

        public MoviesViewHolder(@NonNull MovieItemBinding movieItemBinding) {
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
            mMovieClickListener.movieClicked(mMovie);
        }
    }
}
