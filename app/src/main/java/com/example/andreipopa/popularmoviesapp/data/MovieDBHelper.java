package com.example.andreipopa.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Andrei Popa on 4/1/2018.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="movie.db";

    private static final int DATABASE_VERSION=1;

    public MovieDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

       // String clearDBQuery = "DELETE FROM "+ MovieContract.MovieEntry.TABLE_NAME;
        //sqLiteDatabase.execSQL(clearDBQuery);

        final String SQL_CREATE_MOVIE_TABLE=
                "CREATE TABLE "+ MovieContract.MovieEntry.TABLE_NAME+ " ("+
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_TITLE+" TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE+ " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE+ " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW+ " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID_IN_ONLINE_DATABSE+" TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_POSTER_BLOB+ " BLOB, "+
                        MovieContract.MovieEntry.COLUMN_POSTER_FULL_LINK+" TEXT_NOT_NULL,"+
                        MovieContract.MovieEntry.COLUMN_REVIEWS_LINK+" TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_TRAILERS_LINK+" TEXT NOT NULL"+");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
