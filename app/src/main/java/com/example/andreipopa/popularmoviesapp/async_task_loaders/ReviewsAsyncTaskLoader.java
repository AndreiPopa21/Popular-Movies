package com.example.andreipopa.popularmoviesapp.async_task_loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import com.example.andreipopa.popularmoviesapp.MovieDetailActivity;
import com.example.andreipopa.popularmoviesapp.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Andrei Popa on 3/26/2018.
 */

public class ReviewsAsyncTaskLoader extends AsyncTaskLoader<String>{

    private Bundle bundle;
    private String cachedData;

    public ReviewsAsyncTaskLoader(Context context, Bundle bundle) {
        super(context);
        this.bundle=bundle;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if(cachedData==null){
            forceLoad();
        }
        else{

            super.deliverResult(cachedData);
        }
    }

    @Override
    public String loadInBackground() {
        URL url = null;
        try {
            url = new URL(bundle.getString(MovieDetailActivity.LOADER_REVIEWS_URL_KEY));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            String response= NetworkUtils.getResponseFromHttp(url);
            return response;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(String data) {
        cachedData=data;
        super.deliverResult(data);
    }
}
