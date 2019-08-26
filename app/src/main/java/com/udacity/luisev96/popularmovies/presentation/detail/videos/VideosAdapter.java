package com.udacity.luisev96.popularmovies.presentation.detail.videos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.luisev96.popularmovies.R;
import com.udacity.luisev96.popularmovies.databinding.VideoItemBinding;
import com.udacity.luisev96.popularmovies.domain.Video;
import com.udacity.luisev96.popularmovies.presentation.video.VideoActivity;

import java.util.ArrayList;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private List<Video> mVideosList = new ArrayList<>();
    private Context mContext;
    private static final String URL = "url";

    VideosAdapter(Context context) {
        mContext = context;
    }

    void setVideos(List<Video> mVideosList) {
        this.mVideosList = mVideosList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VideoItemBinding movieItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.video_item, parent, false);
        return new VideosViewHolder(movieItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder holder, int position) {
        holder.bind(mVideosList.get(position));
    }

    @Override
    public int getItemCount() {
        return mVideosList.size();
    }

    class VideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        VideoItemBinding mVideoItemBinding;
        Video mVideo;

        VideosViewHolder(@NonNull VideoItemBinding videoItemBinding) {
            super(videoItemBinding.getRoot());
            mVideoItemBinding = videoItemBinding;
        }

        void bind(Video video) {
            mVideo = video;
            String videoThumbUrl = "https://img.youtube.com/vi/" + video.getKey() + "/0.jpg";
            Picasso
                    .get()
                    .load(videoThumbUrl)
                    .error(R.drawable.ic_panorama)
                    .into(mVideoItemBinding.video);

            mVideoItemBinding.video.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // 1st choice
            inYTApp();
            // 2nd choice
            // inApp();
        }

        void inApp() {
            Intent intent = new Intent(mContext, VideoActivity.class);
            intent.putExtra(URL, mVideo.getKey());
            mContext.startActivity(intent);
        }

        void inYTApp() {
            try {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + mVideo.getKey()));
                mContext.startActivity(appIntent);
            } catch (Exception ex) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mVideo.getKey()));
                mContext.startActivity(webIntent);
            }
        }
    }
}
