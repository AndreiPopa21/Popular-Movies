package com.example.andreipopa.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Andrei Popa on 4/1/2018.
 */

public class MovieContract {


    public static final String AUTHORITY="com.example.andreipopa.popularmoviesapp";

    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);

    public static final String PATH_TASKS="movie";

    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI= BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME="movie";

        public static final String COLUMN_MOVIE_ID="movie_id";
        public static final String COLUMN_MOVIE_TITLE="movie_title";
        public static final String COLUMN_MOVIE_RELEASE_DATE="movie_release_date";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE="movie_vote_average";
        public static final String COLUMN_MOVIE_OVERVIEW="movie_overview";
        public static final String COLUMN_MOVIE_ID_IN_ONLINE_DATABSE="movie_online_id";
        public static final String COLUMN_POSTER_BLOB="movie_poster_blob";
        public static final String COLUMN_POSTER_FULL_LINK="movie_poster_full_link";
        public static final String COLUMN_REVIEWS_LINK="movie_reviews_link";
        public static final String COLUMN_TRAILERS_LINK="movie_trailers_link";



    }

}
