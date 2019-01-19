package com.example.andreipopa.popularmoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.andreipopa.popularmoviesapp.MovieQueryTypeSelector;

import java.util.regex.Matcher;

/**
 * Created by Andrei Popa on 4/2/2018.
 */

public class MovieContentProvider extends ContentProvider {

    public static final int MOVIES=100;
    public static final int MOVIE_WITH_DATABASE_ID=101;

    private static final UriMatcher sUriMatcher= buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_TASKS,MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_TASKS+"/#",MOVIE_WITH_DATABASE_ID);

        return uriMatcher;
    }


    private MovieDBHelper mMovieDBHelper;

    @Override
    public boolean onCreate() {

        Context context= getContext();

        mMovieDBHelper= new MovieDBHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String s1) {

        SQLiteDatabase db= mMovieDBHelper.getReadableDatabase();

        Cursor cursor=null;
        switch (Integer.valueOf(selection)){
            case MovieQueryTypeSelector.DOES_IT_EXIST:

                cursor=db.query(MovieContract.MovieEntry.TABLE_NAME,
                        columns,
                        "movie_title=?",
                        selectionArgs,
                        null,
                        null,
                        null
                );

                break;

            case MovieQueryTypeSelector.FAVORITES:

                cursor=db.query(MovieContract.MovieEntry.TABLE_NAME,
                        columns,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);

                break;
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int tasksDeleted=0;

        switch(match){
            case MOVIES:

                // String movie_database_code= uri.getPathSegments().get(1);
                tasksDeleted= db.delete(MovieContract.MovieEntry.TABLE_NAME,"movie_title=?",strings);
                // tasksDeleted=db.delete(MovieContract.MovieEntry.TABLE_NAME,"1",null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri+"//"+match);


        }

        if(tasksDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return tasksDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase dbHelper= mMovieDBHelper.getWritableDatabase();

        int match= sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case MOVIES:

                long id= dbHelper.insert(MovieContract.MovieEntry.TABLE_NAME,null,contentValues);

                if(id>0){

                    returnUri= ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI,id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
