package com.example.mjd_final.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class UserInfo extends BaseObservable {

    private String seedHash;
    private String address;
    private String balance;
    private Review reviewToWrite;

    public static UserInfo getInstance() {
        return LazyHolder.INSTANCE;
    }

    public String getSeedHash() {
        return seedHash;
    }

    public void setSeedHash(String seedHash) {
        this.seedHash = seedHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Bindable
    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
        notifyPropertyChanged(com.example.mjd_final.BR.balance);
    }

    public Review getReviewToWrite() {
        return reviewToWrite;
    }

    private static class LazyHolder {
        private static final UserInfo INSTANCE = new UserInfo();
    }
}
