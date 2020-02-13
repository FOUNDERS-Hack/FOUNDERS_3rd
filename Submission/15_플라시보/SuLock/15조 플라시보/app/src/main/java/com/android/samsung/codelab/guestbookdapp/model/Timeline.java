package com.android.samsung.codelab.guestbookdapp.model;

import android.databinding.ObservableArrayList;

import java.util.ArrayList;

public class Timeline {

    private ObservableArrayList<Feed> feeds;

    public Timeline(ArrayList<Feed> feeds) {
        this.feeds = new ObservableArrayList<>();
        this.feeds.addAll(feeds);
    }

    public ObservableArrayList<Feed> getFeeds() {
        return feeds;
    }
}
