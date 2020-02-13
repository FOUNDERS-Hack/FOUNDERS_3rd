package com.android.samsung.codelab.guestbookdapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class Feed extends BaseObservable {

    private String emoji, name, comment, date;

    public Feed(String emoji, String name, String comment, String date) {
        this.emoji = emoji;
        this.name = name;
        this.comment = comment;
        this.date = date;
    }

    @Bindable
    public String getEmoji() {
        return emoji;
    }



    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getComment() {
        return comment;
    }



    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void clear() {
        this.name = "";
        this.comment = "";
        this.date = "";
        this.emoji = "\uD83D\uDE00";
    }
}
