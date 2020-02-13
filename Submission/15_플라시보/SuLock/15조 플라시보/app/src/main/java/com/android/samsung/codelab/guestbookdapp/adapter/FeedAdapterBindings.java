package com.android.samsung.codelab.guestbookdapp.adapter;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;

import com.android.samsung.codelab.guestbookdapp.model.Feed;

public class FeedAdapterBindings {

    @BindingAdapter("bind:item")
    public static void bindItem(RecyclerView recyclerView, ObservableArrayList<Feed> feed) {
        FeedsAdapter adapter = (FeedsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItem(feed);
        }
    }
}
