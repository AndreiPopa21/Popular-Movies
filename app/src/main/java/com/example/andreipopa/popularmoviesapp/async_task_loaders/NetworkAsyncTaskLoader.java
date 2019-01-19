package com.example.andreipopa.popularmoviesapp.async_task_loaders;

import android.os.Bundle;
import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.andreipopa.popularmoviesapp.MainActivity;
import com.example.andreipopa.popularmoviesapp.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Andrei Popa on 3/26/2018.
 */

public class NetworkAsyncTaskLoader extends AsyncTaskLoader<String> {

    private String cachedData;

    private Bundle bundle;

    public NetworkAsyncTaskLoader(Context context,Bundle bundle) {
        super(context);
        this.bundle= bundle;

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
        //de implementat metoda pentru URL

        URL url = null;
        try {
            url = new URL(bundle.getString(MainActivity.LOADER_STRING_URL_KEY));
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
