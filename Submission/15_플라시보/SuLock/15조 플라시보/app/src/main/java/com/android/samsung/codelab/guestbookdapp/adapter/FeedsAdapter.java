package com.android.samsung.codelab.guestbookdapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView;

import com.android.databinding.library.baseAdapters.BR;
import com.android.samsung.codelab.guestbookdapp.databinding.ItemFeedListBinding;
import com.android.samsung.codelab.guestbookdapp.model.Feed;

import java.util.ArrayList;
import java.util.List;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> implements Filterable {

    private List<Feed> feedList;
    private List<Feed> feedListAll;

    public FeedsAdapter() {
        this.feedList = new ArrayList<>();
        this.feedListAll=new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Feed feed = feedList.get(i);
        viewHolder.bind(feed);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    @Override
    public Filter getFilter(){
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Feed> filteredList = new ArrayList<>();
            if(constraint ==null|| constraint.length()==0){
                filteredList.addAll(feedListAll);
            } else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Feed item: feedListAll){
                    if(item.getArea().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            feedList.clear();
            feedList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    void setItem(List<Feed> movie) {
        if (movie == null) {
            return;
        }
        this.feedList = movie;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemFeedListBinding binding = ItemFeedListBinding.inflate(
                LayoutInflater.from(viewGroup.getContext())
                , viewGroup
                , false
        );
        return new ViewHolder(binding);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ItemFeedListBinding binding;

        ViewHolder(ItemFeedListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Feed feed) {
            binding.setVariable(BR.feed, feed);
        }
    }
}
