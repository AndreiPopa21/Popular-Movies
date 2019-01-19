package com.example.andreipopa.popularmoviesapp.async_task_loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.andreipopa.popularmoviesapp.MainActivity;
import com.example.andreipopa.popularmoviesapp.MovieQueryTypeSelector;
import com.example.andreipopa.popularmoviesapp.data.MovieContract;

import static android.content.ContentValues.TAG;

/**
 * Created by Andrei Popa on 4/4/2018.
 */

public class DatabaseCursorLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mMovieData;
    private Bundle bundle;
    private int queryType;


    public DatabaseCursorLoader(Context context,Bundle bundle) {
        super(context);
        this.bundle=bundle;
        this.queryType= bundle.getInt(MainActivity.BUNDLE_DATABASE_QUERY_TYPE);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if(mMovieData==null){
            forceLoad();
        }else{
            super.deliverResult(mMovieData);
        }
    }

    @Override
    public Cursor loadInBackground() {
        try {

            switch (queryType){
                case MovieQueryTypeSelector.DOES_IT_EXIST:
                    return getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            String.valueOf(MovieQueryTypeSelector.DOES_IT_EXIST),
                            new String[]{bundle.getString(MainActivity.BUNDLE_DATABASE_QUERY_MOVIE_TITLE_SELECTION_ARGS)},
                            null);

                case MovieQueryTypeSelector.FAVORITES:
                    return getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            String.valueOf(MovieQueryTypeSelector.FAVORITES),
                            null,
                            null);

            }


        } catch (Exception e) {
            Log.e(TAG, "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    public void deliverResult(Cursor data) {
        mMovieData=data;
        super.deliverResult(data);
    }
}
