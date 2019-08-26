package com.udacity.luisev96.popularmovies.presentation.video;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YoutubeFragment extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener, YouTubePlayer.OnFullscreenListener {

    private YouTubePlayer youTubePlayer;
    Boolean isFullScreen = false;
    private static final String URL = "url";
    private static final String API_KEY = "YOUR_YOUTUBE_API_KEY";

    YoutubeFragment(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        setArguments(bundle);
        init();

    }

    private void init() {
        initialize(API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            this.youTubePlayer = youTubePlayer;
            this.youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            this.youTubePlayer.setOnFullscreenListener(this);
            this.youTubePlayer.loadVideo(getArguments().getString(URL), 0);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onFullscreen(boolean b) {
        isFullScreen = b;
    }

    void closeFullScreen() {
        youTubePlayer.setFullscreen(false);
    }
}
