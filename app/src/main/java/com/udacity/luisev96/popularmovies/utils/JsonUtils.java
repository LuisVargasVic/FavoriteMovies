package com.udacity.luisev96.popularmovies.utils;

import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.domain.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String KEY_RESULTS = "results";
    private static final String KEY_ID = "id";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_VIDEO = "video";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_TITLE = "title";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_ORIGINAL_LANGUAGE = "original_language";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_KEY = "key";
    private static final String KEY_NAME = "name";
    private static final String KEY_SITE = "site";
    private static final String KEY_SIZE = "size";
    private static final String KEY_TYPE = "type";

    public static  List<Movie> parseMovieJson(String json) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray results = object.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                int id = movie.getInt(KEY_ID);
                int voteCount = movie.getInt(KEY_VOTE_COUNT);
                boolean video = movie.getBoolean(KEY_VIDEO);
                double voteAverage = movie.getDouble(KEY_VOTE_AVERAGE);
                String title = movie.getString(KEY_TITLE);
                double popularity = movie.getDouble(KEY_POPULARITY);
                String posterPath = movie.getString(KEY_POSTER_PATH);
                String originalLanguage = movie.getString(KEY_ORIGINAL_LANGUAGE);
                String originalTitle = movie.getString(KEY_ORIGINAL_TITLE);
                String backdropPath = movie.getString(KEY_BACKDROP_PATH);
                boolean adult = movie.getBoolean(KEY_ADULT);
                String overview = movie.getString(KEY_OVERVIEW);
                String releaseDate = movie.getString(KEY_RELEASE_DATE);
                movies.add(new Movie(id, voteCount, video, voteAverage, title, popularity, posterPath, originalLanguage, originalTitle, backdropPath, adult, overview, releaseDate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static  List<Video> parseVideoJson(String json) {
        List<Video> videos = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray results = object.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject video = results.getJSONObject(i);
                String id = video.getString(KEY_ID);
                String key = video.getString(KEY_KEY);
                String name = video.getString(KEY_NAME);
                String site = video.getString(KEY_SITE);
                int size = video.getInt(KEY_SIZE);
                String type = video.getString(KEY_TYPE);
                videos.add(new Video(id, key, name, site, size, type));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videos;
    }
}