package com.example.mjd_final.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Review extends BaseObservable{
    private int reviewId;
    private String userId, storeName, comments, date;
    private boolean isTrust;
    private Vote vote;

    public Review(int reviewId, String userId, String storeName, String comments, String date, Vote vote) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.storeName = storeName;
        this.comments = comments;
        this.date = date;
        this.vote = vote;
    }

    @Bindable
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    @Bindable
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Bindable
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Bindable
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Bindable
    public boolean isTrust() {
        return isTrust;
    }

    public void setTrust(boolean trust) {
        isTrust = trust;
    }

    @Bindable
    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }
}