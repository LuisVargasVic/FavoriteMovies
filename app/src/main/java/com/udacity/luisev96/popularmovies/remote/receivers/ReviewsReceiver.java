package com.udacity.luisev96.popularmovies.remote.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static com.udacity.luisev96.popularmovies.presentation.detail.reviews.ReviewsFragment.setReviewsConnection;
import static com.udacity.luisev96.popularmovies.remote.tasks.ReviewsTask.setConnectionReviews;

/**
 * Created by Luis Vargas on 2019-08-26.
 */
public class ReviewsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        Boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        setConnectionReviews(isConnected);
        setReviewsConnection(isConnected);
    }
}
