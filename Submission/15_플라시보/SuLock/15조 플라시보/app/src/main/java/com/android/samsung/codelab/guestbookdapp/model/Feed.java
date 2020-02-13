package com.android.samsung.codelab.guestbookdapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.samsung.codelab.guestbookdapp.BR;

public class Feed extends BaseObservable {

    private String area, bname, ph, bod, cod, toc;

    public Feed(String area, String bname, String ph, String bod, String cod, String toc) {
        this.area = area;
        this.bname = bname;
        this.ph = ph;
        this.bod = bod;
        this.cod = cod;
        this.toc = toc;
    }


    @Bindable
    public String getArea() {
        return area;
    }

    public void setArea(String area) { this.area = area; }

    @Bindable
    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    @Bindable
    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    @Bindable
    public String getBod() {
        return bod;
    }

    public void setBod(String bod) {
        this.bod = bod;
    }

    @Bindable
    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    @Bindable
    public String getToc() {
        return toc;
    }

    public void setToc(String toc) {
        this.toc = toc;
    }



    public void clear() {
        this.area = "";
        this.bname = "";
        this.ph = "";
        this.bod = "";
        this.cod = "";
        this.toc = "";
    }
}
