package com.samsung.open_crypto_wallet_app.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.samsung.open_crypto_wallet_app.BR;

@Entity
public class AccountModel extends BaseObservable {


    @PrimaryKey
    private int accountId;

    private String publicAddress;
    private String hdPath;
    private String accountName;

    @Ignore
    private String accountBalance;
    @Ignore
    private Bitmap qrCode;
    @Ignore
    private boolean isLoading;

    //Constructor for creating blank Account Model Object
    @Ignore
    public AccountModel() {
        isLoading = true;
    }

    public AccountModel(int accountId, String publicAddress, String hdPath, String accountName) {
        this.accountId = accountId;
        this.publicAddress = publicAddress;
        this.hdPath = hdPath;
        this.accountName = accountName;
    }

    // BindingAdapter Required for bitmap Image
    @BindingAdapter("bind:imageBitmap")
    public static void loadImage(ImageView iv, Bitmap bitmap) {
        iv.setImageBitmap(bitmap);
    }

    // Getter & Setter: publicAddress
    @Bindable
    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
        // BR._all is used temporarily instead of BR.publicAddress as UI does not seem to refresh without it
        notifyPropertyChanged(BR.publicAddress);
    }

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    // Getter & Setter: accountBalance
    @Bindable
    public String getAccountBalance() {
        return accountBalance;
    }


    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
        notifyPropertyChanged(BR.accountBalance);
    }

    // Getter & Setter: qrCode
    @Bindable
    public Bitmap getQrCode() {
        return qrCode;
    }


    public void setQrCode(Bitmap qrCode) {
        this.qrCode = qrCode;
        // BR._all is used temporarily instead of BR.qrCode as UI does not seem to refresh without it
        notifyPropertyChanged(BR.qrCode);
    }

    // Getter & Setter: hdPath
    @Bindable
    public String getHdPath() {
        return hdPath;
    }


    public void setHdPath(String hdPath) {
        this.hdPath = hdPath;
        notifyPropertyChanged(BR.hdPath);
    }

    @Bindable
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
        notifyPropertyChanged(BR.accountName);
    }

    @Bindable
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
        notifyPropertyChanged(BR.accountId);
    }

}