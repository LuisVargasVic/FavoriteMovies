package com.udacity.luisev96.popularmovies.remote;

import android.os.AsyncTask;

import com.udacity.luisev96.popularmovies.database.DatabaseVideo;
import com.udacity.luisev96.popularmovies.database.MoviesDatabase;
import com.udacity.luisev96.popularmovies.domain.Video;
import com.udacity.luisev96.popularmovies.utils.JsonUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class VideosTask extends AsyncTask<URL, Void, String> {

    private MoviesDatabase mMoviesDatabase;
    private RemoteListener mRemoteListener;
    private int mMovieId;

    public VideosTask(int movieId, MoviesDatabase moviesDatabase, RemoteListener remoteListener) {
        mMovieId = movieId;
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
                List<Video> videos = JsonUtils.parseVideoJson(result);
                for (int i = 0; i < videos.size(); i++) {
                    Video video = videos.get(i);
                    final DatabaseVideo databaseVideo = new DatabaseVideo(
                            video.getId(),
                            video.getKey(),
                            video.getName(),
                            video.getSite(),
                            video.getSize(),
                            video.getType(),
                            mMovieId
                    );
                    mMoviesDatabase.moviesDao().insertVideo(databaseVideo);
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
