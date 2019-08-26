package com.udacity.luisev96.popularmovies.presentation.video;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.udacity.luisev96.popularmovies.R;
import com.udacity.luisev96.popularmovies.databinding.ActivityVideoBinding;

public class VideoActivity extends AppCompatActivity {

    YoutubeFragment youtubeFragment;
    ActivityVideoBinding activityVideoBinding;
    String url;
    public static final String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVideoBinding = DataBindingUtil.setContentView(this, R.layout.activity_video);

        if (savedInstanceState != null) url = savedInstanceState.getString(URL);
        else if (getIntent() != null)
            if (getIntent().hasExtra(URL)) url = getIntent().getStringExtra(URL);

        youtubeFragment = new YoutubeFragment(url);
        getSupportFragmentManager().beginTransaction().replace(R.id.youtube_player, youtubeFragment).commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(URL, url);
    }

    @Override
    public void onBackPressed() {
        if (youtubeFragment != null)
            if (youtubeFragment.isFullScreen) youtubeFragment.closeFullScreen();
            else super.onBackPressed();
        else super.onBackPressed();
    }
}
