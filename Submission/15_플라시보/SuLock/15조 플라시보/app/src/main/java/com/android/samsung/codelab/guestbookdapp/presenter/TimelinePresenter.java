package com.android.samsung.codelab.guestbookdapp.presenter;

import android.databinding.ObservableArrayList;

import com.android.samsung.codelab.guestbookdapp.contract.TimelineContract;
import com.android.samsung.codelab.guestbookdapp.model.Feed;
import com.android.samsung.codelab.guestbookdapp.model.UserInfo;
import com.android.samsung.codelab.guestbookdapp.remote.FeedLoader;
import com.android.samsung.codelab.guestbookdapp.remote.RemoteManager;
import com.android.samsung.codelab.guestbookdapp.util.AppExecutors;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TimelinePresenter implements TimelineContract.Presenter {

    private static final String TAG = TimelinePresenter.class.getSimpleName();
    private TimelineContract.View mContract;

    public TimelinePresenter(TimelineContract.View mContract) {
        this.mContract = mContract;
    }

    public void onClickWriteFeed() {
        UserInfo.getInstance().getFeedToWrite().clear();
        mContract.showWriteFeedActivity();
    }

    public void onClickRefreshBalance() {
        loadBalance();
    }

    @Override
    public void loadFeed(FeedLoader feedLoader, ObservableArrayList<Feed> feeds) {
        mContract.setLoading(true);
        feedLoader.loadFeeds(10, (success, feedList, message) -> {
            mContract.setLoading(false);
            if (success) {
                feeds.clear();
                feeds.addAll(feedList);
            } else {
                mContract.toast(message);
            }
        });
    }

    @Override
    public void loadBalance() {
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                String address = UserInfo.getInstance().getAddress();
                BigInteger balance = RemoteManager.getInstance().getBalance(address);
                String balanceString = getEthValueFromWei(balance, -1) + " ETH";
                UserInfo.getInstance().setBalance(balanceString);
            } catch (Exception e) {
                UserInfo.getInstance().setBalance("0.0 ETH");
            }
        });
    }

    private String getEthValueFromWei(BigInteger value, int minimumScale) {
        BigDecimal ethValue = (new BigDecimal(value)).multiply(BigDecimal.valueOf(1, 18));
        ethValue = ethValue.stripTrailingZeros();
        if (minimumScale > 0 && ethValue.scale() <= 0) {
            ethValue = ethValue.setScale(minimumScale, BigDecimal.ROUND_HALF_EVEN);
        }
        return ethValue.toPlainString();
    }
}
