package com.example.andreipopa.popularmoviesapp;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MovieQueryTypeSelector{

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POPULAR,TOP_RATED,FAVORITES,TRAILER,REVIEWS,DOES_IT_EXIST,UPCOMING})
    @interface MovieQueryMode {}
    public static final int POPULAR = 0;
    public static final int TOP_RATED = 1;
    public static final int FAVORITES = 2;
    public static final int TRAILER=3;
    public static final int REVIEWS=4;
    public static final int DOES_IT_EXIST=5;
    public static final int UPCOMING=6;

    private int mode;

    public void setMode(@MovieQueryMode int mode){
        this.mode= mode;
    }

    @MovieQueryMode
    public int getMode(){
        return mode;
    }
}
