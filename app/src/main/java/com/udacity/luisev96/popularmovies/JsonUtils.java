package com.udacity.luisev96.popularmovies;

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
    private static final String KEY_GENRE_IDS = "genre_ids";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";

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
                List<Integer> genreIds = new ArrayList<>();
                JSONArray genreIdsObject = movie.getJSONArray(KEY_GENRE_IDS);
                for (int j = 0; j < genreIdsObject.length(); j++) {
                    genreIds.add(genreIdsObject.getInt(j));
                }
                String backdropPath = movie.getString(KEY_BACKDROP_PATH);
                boolean adult = movie.getBoolean(KEY_ADULT);
                String overview = movie.getString(KEY_OVERVIEW);
                String releaseDate = movie.getString(KEY_RELEASE_DATE);
                movies.add(new Movie(id, voteCount, video, voteAverage, title, popularity, posterPath, originalLanguage, originalTitle, genreIds, backdropPath, adult, overview, releaseDate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }
}