package com.android.samsung.codelab.guestbookdapp;

import android.content.ClipData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.samsung.codelab.guestbookdapp.adapter.FeedsAdapter;
import com.android.samsung.codelab.guestbookdapp.contract.TimelineContract;
import com.android.samsung.codelab.guestbookdapp.databinding.ActivityTimelineBinding;
import com.android.samsung.codelab.guestbookdapp.model.Feed;
import com.android.samsung.codelab.guestbookdapp.model.UserInfo;
import com.android.samsung.codelab.guestbookdapp.presenter.TimelinePresenter;
import com.android.samsung.codelab.guestbookdapp.remote.FeedLoader;
import com.android.samsung.codelab.guestbookdapp.view.DividerItemDecoration;


public class TimelineActivity extends AppCompatActivity implements TimelineContract.View {

    private ObservableArrayList<Feed> feedList;
    private FeedsAdapter adapter;
    private TimelineContract.Presenter contract;
    public MenuItem Item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTimelineBinding binding = DataBindingUtil.setContentView(this
                , R.layout.activity_timeline);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        this.adapter = new FeedsAdapter();
        this.feedList = new ObservableArrayList<>();
        binding.recyclerView.setAdapter(adapter);
        binding.setFeedList(feedList);
        binding.setUserInfo(UserInfo.getInstance());

        binding.recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        this
                        , getColor(R.color.divider_color)
                        , 1.0f
                        , 30
                        , 30)
        );

        TimelinePresenter presenter = new TimelinePresenter(this);
        binding.setTimelinePresenter(presenter);
        contract = presenter;
        contract.loadFeed(new FeedLoader(UserInfo.getInstance().getAddress()), feedList);

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.canScrollVertically(-1)) {
                    myToolbar.setElevation(4);
                } else {
                    myToolbar.setElevation(0);
                }
            }
        });

        contract.loadBalance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_timeline, menu);
        Item = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curld = item.getItemId();
        switch(curld){
            case R.id.action_refresh:
                contract.loadFeed(new FeedLoader(UserInfo.getInstance().getAddress()), feedList);
                break;
            case R.id.action_search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);
                        return false;
                    }
                });

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void showWriteFeedActivity() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(getApplicationContext(), WriteFeedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }

    @Override
    public void setLoading(boolean isLoading) {
        runOnUiThread(() -> {
            ProgressBar bar = findViewById(R.id.progress_bar);
            bar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        });
    }

    @Override
    public void toast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

}
