package com.udacity.luisev96.popularmovies.presentation.detail.videos;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.luisev96.popularmovies.R;
import com.udacity.luisev96.popularmovies.databinding.FragmentVideosBinding;
import com.udacity.luisev96.popularmovies.domain.Movie;
import com.udacity.luisev96.popularmovies.domain.Video;
import com.udacity.luisev96.popularmovies.remote.listeners.ConnectionListener;
import com.udacity.luisev96.popularmovies.remote.listeners.RemoteListener;
import com.udacity.luisev96.popularmovies.remote.receivers.VideosReceiver;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideosFragment extends Fragment implements RemoteListener, ConnectionListener {

    private FragmentVideosBinding fragmentVideosBinding;
    private VideosViewModel viewModel;
    private VideosAdapter mAdapter;
    private Movie mMovie;
    private BroadcastReceiver mReceiver;
    private static ConnectionListener connectionListener;
    private static final String MOVIE_KEY = "movie";
    private static final String TAG = VideosFragment.class.getSimpleName();

    public VideosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentVideosBinding = FragmentVideosBinding.inflate(inflater);
        return fragmentVideosBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connectionListener = this;
        assert getArguments() != null;
        mMovie = (Movie) getArguments().getSerializable(MOVIE_KEY);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentVideosBinding.rvVideos.setLayoutManager(layoutManager);
        fragmentVideosBinding.rvVideos.setHasFixedSize(true);
        mAdapter = new VideosAdapter(getContext());
        fragmentVideosBinding.rvVideos.setAdapter(mAdapter);
        viewModel = ViewModelProviders.of(this).get(VideosViewModel.class);
        populateUI();
    }

    private void populateUI() {
        viewModel.videos(this, mMovie.getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        mReceiver = new VideosReceiver();
        Objects.requireNonNull(getActivity()).registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).unregisterReceiver(mReceiver);
    }

    @Override
    public void preExecute() {
        fragmentVideosBinding.pb.setVisibility(View.VISIBLE);
        fragmentVideosBinding.message.setVisibility(View.GONE);
        fragmentVideosBinding.rvVideos.setVisibility(View.GONE);
    }

    @Override
    public void postExecute(Boolean isData) {
        fragmentVideosBinding.pb.setVisibility(View.GONE);
        viewModel.getVideos().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(@Nullable List<Video> videos) {
                Log.d(TAG, "Updating list of videos from LiveData in ViewModel");
                if (videos == null || videos.isEmpty()) {
                    fragmentVideosBinding.message.setVisibility(View.VISIBLE);
                    fragmentVideosBinding.rvVideos.setVisibility(View.GONE);
                } else {
                    fragmentVideosBinding.message.setVisibility(View.GONE);
                    fragmentVideosBinding.rvVideos.setVisibility(View.VISIBLE);
                    mAdapter.setVideos(videos);
                }
            }
        });
    }

    public static void setVideosConnection(Boolean connection) {
        connectionListener.connection(connection);
    }

    @Override
    public void connection(Boolean connection) {
        if (connection) {
            fragmentVideosBinding.message.setText(R.string.videos_empty);
            populateUI();
        } else {
            fragmentVideosBinding.message.setText(R.string.videos_connection);
        }
    }
}
