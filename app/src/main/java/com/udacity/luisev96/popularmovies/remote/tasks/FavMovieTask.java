package com.udacity.luisev96.popularmovies.remote.tasks;

import android.os.AsyncTask;

import com.udacity.luisev96.popularmovies.database.DatabaseFavMovie;
import com.udacity.luisev96.popularmovies.database.MoviesDatabase;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.remote.listeners.RemoteListener;

/**
 * Created by Luis Vargas on 2019-08-25.
 */

public class FavMovieTask extends AsyncTask<String, Void, String> {

    private static final String INSERT = "insert";
    private static final String DELETE = "delete";
    private MoviesDatabase mMoviesDatabase;
    private RemoteListener mRemoteListener;
    private Movie mMovie;
    private String mType;

    public FavMovieTask(MoviesDatabase moviesDatabase, RemoteListener remoteListener, Movie movie, String type) {
        mMoviesDatabase = moviesDatabase;
        mRemoteListener = remoteListener;
        mMovie = movie;
        mType = type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mRemoteListener.preExecute();
    }

    @Override
    public String doInBackground(String... params) {
        if (mType.equals(INSERT)) {
            Movie movie = mMovie;
            final DatabaseFavMovie databaseFavMovie = new DatabaseFavMovie(
                    movie.getId(),
                    movie.getVoteCount(),
                    movie.isVideo(),
                    movie.getVoteAverage(),
                    movie.getTitle(),
                    movie.getPopularity(),
                    movie.getPosterPath(),
                    movie.getOriginalLanguage(),
                    movie.getOriginalTitle(),
                    movie.getBackdropPath(),
                    movie.isAdult(),
                    movie.getOverview(),
                    movie.getReleaseDate()
            );
            mMoviesDatabase.moviesDao().insertFavMovie(databaseFavMovie);
        } if (mType.equals(DELETE)) {
            Movie movie = mMovie;
            final DatabaseFavMovie databaseFavMovie = new DatabaseFavMovie(
                    movie.getId(),
                    movie.getVoteCount(),
                    movie.isVideo(),
                    movie.getVoteAverage(),
                    movie.getTitle(),
                    movie.getPopularity(),
                    movie.getPosterPath(),
                    movie.getOriginalLanguage(),
                    movie.getOriginalTitle(),
                    movie.getBackdropPath(),
                    movie.isAdult(),
                    movie.getOverview(),
                    movie.getReleaseDate()
            );
            mMoviesDatabase.moviesDao().deleteFavMovie(databaseFavMovie);
        }

        return mType;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals(INSERT)) {
            mRemoteListener.postExecute(true);
        } else if (result.equals(DELETE)) {
            mRemoteListener.postExecute(false);
        }
    }
}
