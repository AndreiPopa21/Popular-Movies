package com.example.andreipopa.popularmoviesapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.app.LoaderManager;
import android.content.Loader;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.andreipopa.popularmoviesapp.async_task_loaders.DatabaseCursorLoader;
import com.example.andreipopa.popularmoviesapp.data.MovieContract;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.json.JSONException;

import java.net.URL;

import com.example.andreipopa.popularmoviesapp.Adapters.MoviesAdapter;
import com.example.andreipopa.popularmoviesapp.async_task_loaders.NetworkAsyncTaskLoader;
import com.example.andreipopa.popularmoviesapp.Objects.Movie;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.MovieClickItemListener{


    private RecyclerView movies_rv;
    private TextView error_tv;
    private ProgressBar network_loading_bar;
    private BottomNavigationView bottomNavigationView;

    private Context activity_context;
    private MoviesAdapter.MovieClickItemListener activity_click_listener;
    private MoviesAdapter moviesAdapter;



    private int currentQueryType=MovieQueryTypeSelector.POPULAR;

    private static final String ON_SAVED_INSTANCE_QUERY_TYPE="query";
    public static final String MOVIE_PARCELABLE_QUERY_STRING="MovieClass";
    public static final String MOVIE_PARCELABLE_WAS_ONLINE_QUERY_BOOLEAN="wasOnlineQuery";

    private static final int ID_MOVIE_LOADER=44;
    private static final int OFFLINE_DATABASE_LOADER=99;

    public static final String LOADER_INT_MOVIE_QUERY_KEY="movieQueryType";
    public static final String LOADER_STRING_URL_KEY="url";
    public static final String LOADER_BOOLEAN_SEARCHNEW_KEY="searchNew";

    public static final String BUNDLE_DATABASE_QUERY_TYPE="database_query_type";
    public static final String BUNDLE_DATABASE_QUERY_MOVIE_TITLE_SELECTION_ARGS="movie_title_selection_args";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sthethoSetUp();


        activity_context=this;
        activity_click_listener=this;

        movies_rv=(RecyclerView)findViewById(R.id.movies_rv);
        error_tv=(TextView)findViewById(R.id.error_tv);
        network_loading_bar=(ProgressBar)findViewById(R.id.network_progress_bar);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.navigation);

        //to be added the personal action category

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                         
                            case R.id.popular:
                                loadData(MovieQueryTypeSelector.POPULAR,true);
                                break;
                            case R.id.personal:
                                loadData(MovieQueryTypeSelector.FAVORITES,true);
                                break;
                            case R.id.upcoming:
                                loadData(MovieQueryTypeSelector.UPCOMING,true);
                                break;
                        }
                        return true;
                    }
                });


        setGridLayoutSpanCount();

        movies_rv.setHasFixedSize(false);

        if(savedInstanceState!=null && savedInstanceState.containsKey(ON_SAVED_INSTANCE_QUERY_TYPE)){
            currentQueryType =(int) savedInstanceState.getSerializable(ON_SAVED_INSTANCE_QUERY_TYPE);
            loadData(currentQueryType,false);
        }else{
            loadData(currentQueryType,true);

        }
    }

    private void sthethoSetUp(){

        Stetho.initializeWithDefaults(this);

        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    private void loadData(int queryTypes, boolean searchNewData){

        /*Vibrator vibe = (Vibrator) getSystemService( VIBRATOR_SERVICE );
        vibe.vibrate( 100 );*/

        switch (queryTypes){

            case MovieQueryTypeSelector.TOP_RATED:
                showLoadingProgressBar();
                currentQueryType=MovieQueryTypeSelector.TOP_RATED;
                performOnlineDatabaseQuery(MovieQueryTypeSelector.TOP_RATED,searchNewData);
                break;

            case MovieQueryTypeSelector.POPULAR:
                showLoadingProgressBar();
                currentQueryType=MovieQueryTypeSelector.POPULAR;
                performOnlineDatabaseQuery(MovieQueryTypeSelector.POPULAR,searchNewData);
                break;

            case MovieQueryTypeSelector.FAVORITES:
                showLoadingProgressBar();
                currentQueryType=MovieQueryTypeSelector.FAVORITES;
                performOfflineDatabaseQuery(MovieQueryTypeSelector.FAVORITES,searchNewData);
                break;
            case MovieQueryTypeSelector.UPCOMING:
                showLoadingProgressBar();
                currentQueryType=MovieQueryTypeSelector.UPCOMING;
                performOnlineDatabaseQuery(currentQueryType,searchNewData);
                break;
        }

    }

    private void performOnlineDatabaseQuery(int queryTypes,boolean searchNewData){

        URL movieUrl= NetworkUtils.buildMovieListUrl(queryTypes);

        Bundle bundle= new Bundle();
        bundle.putInt(LOADER_INT_MOVIE_QUERY_KEY,queryTypes);
        bundle.putString(LOADER_STRING_URL_KEY,movieUrl.toString());
        bundle.putBoolean(LOADER_BOOLEAN_SEARCHNEW_KEY,searchNewData);

        if(searchNewData){
            movies_rv.setAdapter(new MoviesAdapter(this,new Movie[]{},this,true));
            getLoaderManager().restartLoader(ID_MOVIE_LOADER,bundle,networkLoaderCallbacks);

        }else{
            getLoaderManager().initLoader(ID_MOVIE_LOADER,bundle,networkLoaderCallbacks);

        }
    }

    private void performOfflineDatabaseQuery(int queryTypes,boolean searchNewData){

        Bundle bundle= new Bundle();
        bundle.putInt(BUNDLE_DATABASE_QUERY_TYPE,queryTypes);
        if(searchNewData){
            movies_rv.setAdapter(new MoviesAdapter(this,new Movie[]{},this,true));
            getLoaderManager().restartLoader(OFFLINE_DATABASE_LOADER,bundle,offlineDatabaseFullQuery);
        }else{
            getLoaderManager().initLoader(OFFLINE_DATABASE_LOADER,bundle,offlineDatabaseFullQuery);
        }
    }

    private void showErrorMessage(){
        error_tv.setVisibility(View.VISIBLE);
        network_loading_bar.setVisibility(View.INVISIBLE);

    }
    private void hideErrorMessage(){
        error_tv.setVisibility(View.INVISIBLE);
        network_loading_bar.setVisibility(View.INVISIBLE);

    }
    private void showLoadingProgressBar(){
        error_tv.setVisibility(View.INVISIBLE);
        network_loading_bar.setVisibility(View.VISIBLE);
    }
    private void hideLoadingProgressBar(){
        error_tv.setVisibility(View.INVISIBLE);
        network_loading_bar.setVisibility(View.INVISIBLE);

    }

    private void setGridLayoutSpanCount(){
        int spanCount;
        if(this.getResources().getBoolean(R.bool.is_landscape)){
            spanCount=3;
        }else{
            spanCount=2;
        }
        movies_rv.setLayoutManager(new GridLayoutManager(this,spanCount));
    }

    @Override
    public void onMovieClickItem(MoviesAdapter.MovieViewHolder movieViewHolder, boolean wasOnlineQuery,Bitmap posterBitmap) {

        if(wasOnlineQuery==true){
            byte[] posterByteArray= NetworkUtils.convertBitmapToByteArray(posterBitmap);
            movieViewHolder.movieClass.setPosterByteArray(posterByteArray);
        }

        launchMovieDetailActivity(movieViewHolder.movieClass,wasOnlineQuery);
    }

    private void launchMovieDetailActivity(Movie holderMovieClass,boolean wasOnlineQuery){

        Intent intent= new Intent(this,MovieDetailActivity.class);
        intent.putExtra(MOVIE_PARCELABLE_QUERY_STRING,holderMovieClass);
        intent.putExtra(MOVIE_PARCELABLE_WAS_ONLINE_QUERY_BOOLEAN,wasOnlineQuery);
        startActivity(intent);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putSerializable(ON_SAVED_INSTANCE_QUERY_TYPE,currentQueryType);
        super.onSaveInstanceState(outState);
    }

    private LoaderManager.LoaderCallbacks<String> networkLoaderCallbacks=new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int i, Bundle bundle) {

            return new NetworkAsyncTaskLoader(getApplicationContext(),bundle);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String s) {

            hideLoadingProgressBar();
            try {
                Movie[] list = NetworkUtils.createOnlineMovieList(s);

                if(list!=null)
                {
                    hideErrorMessage();

                    moviesAdapter= new MoviesAdapter(activity_context,list,activity_click_listener,true);
                    movies_rv.setAdapter(moviesAdapter);
                }else{
                    showErrorMessage();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> offlineDatabaseFullQuery= new LoaderManager.LoaderCallbacks<Cursor>() {


        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return new DatabaseCursorLoader(getApplicationContext(),args);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

            hideLoadingProgressBar();
            if(data==null){
                showErrorMessage();
                return;
            }



            Movie[] moviesList=new Movie[data.getCount()];

            if(data.getCount()==0){
                showErrorMessage();
            }else{
                data.moveToFirst();
                int i = 0;
                do {
                    int titleIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
                    int releaseDateIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
                    int voteAverageIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE);
                    int overviewIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW);
                    int onlineIdIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID_IN_ONLINE_DATABSE);
                    int fullPosterIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_FULL_LINK);
                    int posterBlobIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_BLOB);
                    int trailersLinkIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TRAILERS_LINK);
                    int reviewsLinkIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_REVIEWS_LINK);


                    String movie_title = data.getString(titleIndex);
                    String movie_releaseDate = data.getString(releaseDateIndex);
                    String movie_voteAverage = data.getString(voteAverageIndex);
                    String movie_overview = data.getString(overviewIndex);
                    String movie_idInOnlineDatabse = data.getString(onlineIdIndex);
                    String movie_fullPosterLink = data.getString(fullPosterIndex);
                    String movie_trailersLink = data.getString(trailersLinkIndex);
                    String movie_reviewsLink = data.getString(reviewsLinkIndex);
                    byte[] movie_posterByteArray = data.getBlob(posterBlobIndex);


                    moviesList[i] = new Movie(movie_title,
                            movie_releaseDate,
                            movie_voteAverage,
                            movie_overview,
                            movie_idInOnlineDatabse,
                            movie_fullPosterLink,
                            movie_trailersLink,
                            movie_reviewsLink,
                            movie_posterByteArray);
                    i++;

                } while (data.moveToNext());
            }


            moviesAdapter=new MoviesAdapter(getApplicationContext(),
                    moviesList,
                    activity_click_listener,
                    false);
            movies_rv.setAdapter(moviesAdapter);

        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };




}
