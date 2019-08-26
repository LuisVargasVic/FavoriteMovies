package com.udacity.luisev96.popularmovies.remote;

import android.os.AsyncTask;

import com.udacity.luisev96.popularmovies.database.DatabaseMovie;
import com.udacity.luisev96.popularmovies.database.MoviesDatabase;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.utils.JsonUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MoviesTask extends AsyncTask<URL, Void, String> {

    private MoviesDatabase mMoviesDatabase;
    private RemoteListener mRemoteListener;

    public MoviesTask(MoviesDatabase moviesDatabase, RemoteListener remoteListener) {
        mMoviesDatabase = moviesDatabase;
        mRemoteListener = remoteListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mRemoteListener.preExecute();
    }

    @Override
    public String doInBackground(URL... params) {
        URL url = params[0];
        String result = null;
        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                result = reader.readLine();
                List<Movie> movies = JsonUtils.parseMovieJson(result);
                for (int i = 0; i < movies.size(); i++) {
                    Movie movie = movies.get(i);
                    final DatabaseMovie databaseMovie = new DatabaseMovie(
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
                    mMoviesDatabase.moviesDao().insertMovie(databaseMovie);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            mRemoteListener.postExecute(true);
        } else {
            mRemoteListener.postExecute(false);
        }
    }
}