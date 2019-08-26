package com.udacity.luisev96.popularmovies.remote.tasks;

import android.os.AsyncTask;

import com.udacity.luisev96.popularmovies.database.DatabaseReview;
import com.udacity.luisev96.popularmovies.database.MoviesDatabase;
import com.udacity.luisev96.popularmovies.domain.Review;
import com.udacity.luisev96.popularmovies.remote.listeners.ConnectionListener;
import com.udacity.luisev96.popularmovies.remote.listeners.RemoteListener;
import com.udacity.luisev96.popularmovies.utils.JsonUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ReviewsTask extends AsyncTask<URL, Void, String> implements ConnectionListener {

    private MoviesDatabase mMoviesDatabase;
    private RemoteListener mRemoteListener;
    private int mMovieId;
    private static ConnectionListener connectionListener;

    public ReviewsTask(int movieId, MoviesDatabase moviesDatabase, RemoteListener remoteListener) {
        mMovieId = movieId;
        mMoviesDatabase = moviesDatabase;
        mRemoteListener = remoteListener;
        connectionListener = this;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mRemoteListener.preExecute();
    }

    public static void setConnectionReviews(Boolean connection) {
        connectionListener.connection(connection);
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
                List<Review> reviews = JsonUtils.parseReviewJson(result);
                for (int i = 0; i < reviews.size(); i++) {
                    Review review = reviews.get(i);
                    final DatabaseReview databaseReview = new DatabaseReview(
                            review.getId(),
                            review.getAuthor(),
                            review.getContent(),
                            review.getUrl(),
                            mMovieId
                    );
                    mMoviesDatabase.moviesDao().insertReview(databaseReview);
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
        mRemoteListener.postExecute(true);
    }

    @Override
    public void connection(Boolean connection) {
        if (!connection) {
            this.cancel(true);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mRemoteListener.postExecute(true);
    }
}
