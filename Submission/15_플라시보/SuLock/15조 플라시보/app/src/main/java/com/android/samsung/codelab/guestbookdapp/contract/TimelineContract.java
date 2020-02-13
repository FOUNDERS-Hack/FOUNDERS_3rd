package com.android.samsung.codelab.guestbookdapp.contract;

import android.databinding.ObservableArrayList;

import com.android.samsung.codelab.guestbookdapp.model.Feed;
import com.android.samsung.codelab.guestbookdapp.remote.FeedLoader;

public interface TimelineContract {

    interface View {
        void setLoading(boolean isLoading);

        void showWriteFeedActivity();

        void toast(String message);
    }

    interface Presenter {
        void loadFeed(FeedLoader feedLoader, ObservableArrayList<Feed> feeds);

        void loadBalance();
    }


}
