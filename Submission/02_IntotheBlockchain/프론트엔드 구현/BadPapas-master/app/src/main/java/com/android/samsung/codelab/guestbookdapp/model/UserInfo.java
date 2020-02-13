package com.android.samsung.codelab.guestbookdapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;



public class UserInfo extends BaseObservable {

    private String seedHash;
    private String address;
    private String balance;
    private Report reportToWrite;

    public UserInfo() {
        reportToWrite = new Report("", "", "", "","");
    }

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



    public Report getReportToWrite() {
        return reportToWrite;
    }

    private static class LazyHolder {
        private static final UserInfo INSTANCE = new UserInfo();
    }
}
