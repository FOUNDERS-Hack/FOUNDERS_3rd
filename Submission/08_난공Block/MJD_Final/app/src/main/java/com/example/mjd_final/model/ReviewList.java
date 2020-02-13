package com.example.mjd_final.model;

import androidx.databinding.ObservableArrayList;

import java.util.ArrayList;

public class ReviewList {

    private ObservableArrayList<Review> reviews;

    public ReviewList(ArrayList<Review> reviews) {
        this.reviews = new ObservableArrayList<>();
        this.reviews.addAll(reviews);
    }

    public ObservableArrayList<Review> getReviews() {
        return reviews;
    }
}
