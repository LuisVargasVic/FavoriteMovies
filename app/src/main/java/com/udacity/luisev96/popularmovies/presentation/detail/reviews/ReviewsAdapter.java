package com.udacity.luisev96.popularmovies.presentation.detail.reviews;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.luisev96.popularmovies.R;
import com.udacity.luisev96.popularmovies.databinding.ReviewItemBinding;
import com.udacity.luisev96.popularmovies.domain.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private List<Review> mReviewsList = new ArrayList<>();

    ReviewsAdapter() {
    }

    void setReviews(List<Review> mReviewsList) {
        this.mReviewsList = mReviewsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReviewItemBinding reviewItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.review_item, parent, false);
        return new ReviewsViewHolder(reviewItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        holder.bind(mReviewsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mReviewsList.size();
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        ReviewItemBinding mReviewItemBinding;
        Review mReview;

        ReviewsViewHolder(@NonNull ReviewItemBinding reviewItemBinding) {
            super(reviewItemBinding.getRoot());
            mReviewItemBinding = reviewItemBinding;
        }

        void bind(Review review) {
            mReview = review;
            mReviewItemBinding.tvReviewAuthor.setText(mReview.getAuthor());
            mReviewItemBinding.tvReviewContent.setText(mReview.getContent());
        }
    }
}
