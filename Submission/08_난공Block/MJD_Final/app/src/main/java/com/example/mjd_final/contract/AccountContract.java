package com.example.mjd_final.contract;

import androidx.databinding.ObservableArrayList;

import com.example.mjd_final.model.Review;
import com.example.mjd_final.remote.ReviewLoader;

public interface AccountContract {
    interface  View {
        void setLoading(boolean isLoading);

        void toast(String message);
    }

    interface  Presenter {
        void loadReview(ReviewLoader reviewLoader, ObservableArrayList<Review> reviews);

        void loadBalance();
    }
}
