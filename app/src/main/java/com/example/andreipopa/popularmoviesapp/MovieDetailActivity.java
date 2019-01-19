package com.example.andreipopa.popularmoviesapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.CursorLoader;
//import android.support.v4.content.Loader;
//import android.support.v4.widget.CursorAdapter;

import com.example.andreipopa.popularmoviesapp.async_task_loaders.DatabaseCursorLoader;
import com.example.andreipopa.popularmoviesapp.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;

import com.example.andreipopa.popularmoviesapp.Adapters.TrailersAdapter;
import com.example.andreipopa.popularmoviesapp.async_task_loaders.ReviewsAsyncTaskLoader;
import com.example.andreipopa.popularmoviesapp.async_task_loaders.TrailerLinksAsyncTaskLoader;
import com.example.andreipopa.popularmoviesapp.Objects.Movie;
import com.example.andreipopa.popularmoviesapp.Objects.Review;
import com.example.andreipopa.popularmoviesapp.Objects.Trailer;
import com.squareup.picasso.Target;

import static com.example.andreipopa.popularmoviesapp.Objects.DetailFragmentObjects.*;

/**
 * Created by Andrei Popa on 2/27/2018.
 */

public class MovieDetailActivity extends AppCompatActivity
        implements TrailersAdapter.TrailerItemClickListener
{
    private ImageView posterImage;
    private TextView titleText;
    private TextView voteAverageText;
    private TextView releaseDateText;
    private TextView overviewText;

    private FrameLayout reviewFragmentHead;

    private ImageView firstStar;
    private ImageView secondStar;
    private ImageView thirdStar;
    private ImageView forthStar;
    private ImageView fifthStar;

    private Button addToFavoritesButton;

    //////!!!!!!!!!!!!!!!!!!!!!!!!!!
    private Movie detailMovieObject;
    private boolean wasOnlineQuery;
    private boolean movieHasBeenAddedToTheList;
/////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    private static final int TRAILER_LINKS_LOADER_ID=55;
    private static final int REVIEWS_LOADER_ID= 77;
    private static final int QUERY_FOR_EXISTENCE_LOADER_ID=88;

    public final static String LOADER_REVIEWS_URL_KEY="reviews-key";
    public final static String LOADER_TRAILER_URL_KEY="trailer-key";

    public final static String FRAGMENT_REVIEWS_RECYCLERVIEW_TAG="REVIEWS_RECYCLER_VIEW_TAG";
    public final static String FRAGMENT_TRAILERS_RECYCLERVIEW_TAG="TRAILERS_RECYCLER_VIEW_TAG";

    private boolean alraedyDataLoaded=false;

    private boolean configuration=false;

    private TrailersAdapter.TrailerItemClickListener mTrailerClickListener;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);


        Intent intent= getIntent();
        if(intent==null){
            closeOnError();
        }

        if(savedInstanceState!=null && savedInstanceState.containsKey("CONFIGURATION")){
           movieHasBeenAddedToTheList=savedInstanceState.getBoolean("CONFIGURATION");

        }

       // Toast.makeText(this,String.valueOf(movieHasBeenAddedToTheList),Toast.LENGTH_SHORT).show();

        this.detailMovieObject= intent.getExtras().getParcelable(MainActivity.MOVIE_PARCELABLE_QUERY_STRING);
        this.wasOnlineQuery= intent.getExtras().getBoolean(MainActivity.MOVIE_PARCELABLE_WAS_ONLINE_QUERY_BOOLEAN);


        populateDetailLayout();

    }

    private void populateDetailLayout(){

        setTitle(detailMovieObject.getMovieTitle());

        posterImage=(ImageView)findViewById(R.id.poster_image);
        //titleText=(TextView)findViewById(R.id.title_text);

        releaseDateText=(TextView)findViewById(R.id.release_date_text);
        overviewText=(TextView)findViewById(R.id.overview_text);
        firstStar=(ImageView)findViewById(R.id.first_star);
        secondStar=(ImageView)findViewById(R.id.second_star);
        thirdStar=(ImageView)findViewById(R.id.third_star);
        forthStar=(ImageView)findViewById(R.id.forth_star);
        fifthStar=(ImageView)findViewById(R.id.fifth_star);
        voteAverageText=(TextView)findViewById(R.id.vote_average_textView);


        addToFavoritesButtonSetUp();

        setDetailActivityPoster();
        setDetailActivityVoteAverage();
        setDetailActivityReleaseDate();
        setDetailActivityOverview();
        getTheReviews();
        getTheTrailers();
    }

    private void addToFavoritesButtonSetUp(){

        addToFavoritesButton=(Button)findViewById(R.id.favoritesButton);
        addToFavoritesButton.setHapticFeedbackEnabled(true);
        if(movieHasBeenAddedToTheList==true){
            addToFavoritesButton.setText("Remove");
        }
        else{
            addToFavoritesButton.setText("Add");
        }
        queryWhetherMovieExistsInDatabase();


    }

    private void setDetailActivityPoster(){

       /* if(wasOnlineQuery){

            Target target= new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    posterBitmap=bitmap;
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    posterBitmap=null;
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.with(this).load(full_poster_link).into(target);
            Picasso.with(this).load(full_poster_link).into(posterImage);

            detailMovieObject.setPosterBitmap(posterBitmap);
        }else{

        }*/

        Bitmap posterBitmap= NetworkUtils.convertByteArrayToBitmap(detailMovieObject.getPosterByteArray());
        posterImage.setImageBitmap(posterBitmap);

    }

    private void setDetailActivityVoteAverage(){

        double averageValue= Double.valueOf(detailMovieObject.getVoteAverage());
        int intAverageValue= (int)averageValue;

        if(intAverageValue<8){
            fifthStar.setImageDrawable(setStarColor());
        }
        if(intAverageValue<=6){
            forthStar.setImageDrawable(setStarColor());
        }
        if(intAverageValue<=4){
            thirdStar.setImageDrawable(setStarColor());
        }
        if(intAverageValue<=2){
            secondStar.setImageDrawable(setStarColor());
        }

        voteAverageText.setText("("+detailMovieObject.getVoteAverage()+"/10)");

    }

    private void setDetailActivityReleaseDate(){

        String year= detailMovieObject.getReleaseDate().substring(0,4);
        year= "("+year+")";
        releaseDateText.setText(year);
    }

    private void setDetailActivityOverview(){
        overviewText.setText(detailMovieObject.getOverview());
    }

    private void getTheReviews(){

        ReviewListRecyclerView reviewListRecyclerView= (ReviewListRecyclerView)getSupportFragmentManager().findFragmentByTag(FRAGMENT_REVIEWS_RECYCLERVIEW_TAG);

        if(reviewListRecyclerView!=null){
            return;
        }

        reviewFragmentHead= (FrameLayout)findViewById(R.id.review_fragment_head);

        // URL reviewUrl= NetworkUtils.buildMovieInfosUrl(MovieQueryTypeSelector.REVIEWS,detailMovieObject.getOnlineId());

        //  detailMovieObject.setReviewsLink(reviewUrl.toString());

        Bundle bundle = new Bundle();
        bundle.putString(LOADER_REVIEWS_URL_KEY,detailMovieObject.getReviewLink());

        showReviewProgressBar();
        getLoaderManager().initLoader(REVIEWS_LOADER_ID,bundle,reviewsLoader);

    }

    private void getTheTrailers(){

        TrailerListRecyclerView trailerListRecyclerView= (TrailerListRecyclerView)getSupportFragmentManager().findFragmentByTag(FRAGMENT_TRAILERS_RECYCLERVIEW_TAG);

        if(trailerListRecyclerView!=null){
            return;
        }

        this.mTrailerClickListener=this;

        showTrailerProgressBar();

        //URL trailersURL= NetworkUtils.buildMovieInfosUrl(MovieQueryTypeSelector.TRAILER,detailMovieObject.getOnlineId());
        //detailMovieObject.setTrailersLink(trailersURL.toString());

        Bundle bundle = new Bundle();
        bundle.putString(LOADER_TRAILER_URL_KEY,detailMovieObject.getTrailersLink());
        getLoaderManager().initLoader(TRAILER_LINKS_LOADER_ID,bundle,trailerLoader);
    }

    private void showReviewProgressBar(){

        ReviewLoadingProgressBar reviewLoadingProgressBar= new ReviewLoadingProgressBar();
        getSupportFragmentManager().beginTransaction().replace(R.id.review_fragment_head,reviewLoadingProgressBar).commit();
    }

    private void showTrailerProgressBar(){

        ReviewLoadingProgressBar reviewLoadingProgressBar= new ReviewLoadingProgressBar();
        getSupportFragmentManager().beginTransaction().replace(R.id.trailer_fragment_head,reviewLoadingProgressBar).commit();
    }

    private void showNoReviewTextView(){

        NoReviewTextViewFragment noReviewTextViewFragment= new NoReviewTextViewFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.review_fragment_head,noReviewTextViewFragment).commit();
    }

    private void showNoTrailerTextView(){

        NoTrailerTextViewFragment noTrailerTextViewFragment= new NoTrailerTextViewFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.trailer_fragment_head,noTrailerTextViewFragment).commit();
    }

    private void showReviewRecyclerView(Review[] reviews_list){

        ReviewListRecyclerView reviewListRecyclerView= new ReviewListRecyclerView(getApplicationContext(),reviews_list);

        getSupportFragmentManager().beginTransaction().replace(R.id.review_fragment_head,reviewListRecyclerView,FRAGMENT_REVIEWS_RECYCLERVIEW_TAG).commit();

    }

    private void showTrailerRecyclerView(Trailer[] trailers_list){

        TrailerListRecyclerView trailerListRecyclerView= new TrailerListRecyclerView(getApplicationContext(),trailers_list,mTrailerClickListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.trailer_fragment_head,trailerListRecyclerView,FRAGMENT_TRAILERS_RECYCLERVIEW_TAG).commit();
    }

    private void closeOnError(){
        finish();
        Log.d("MovieDetailActivity","There was an error whcih forced the detail activity to be close");
    }

    private void queryWhetherMovieExistsInDatabase(){
        Bundle bundle= new Bundle();
        bundle.putString(MainActivity.BUNDLE_DATABASE_QUERY_MOVIE_TITLE_SELECTION_ARGS,detailMovieObject.getMovieTitle());
        bundle.putInt(MainActivity.BUNDLE_DATABASE_QUERY_TYPE,MovieQueryTypeSelector.DOES_IT_EXIST);
        getLoaderManager().restartLoader(QUERY_FOR_EXISTENCE_LOADER_ID,bundle,queryForExistenceLoader);
    }

    private Drawable setStarColor(){
        Drawable myIcon = getResources().getDrawable( R.drawable.ic_grade_white_18px );
        ColorFilter filter = new LightingColorFilter( Color.WHITE, Color.WHITE );
        myIcon.setColorFilter(filter);
        return myIcon;
    }

    private LoaderManager.LoaderCallbacks<String> trailerLoader= new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int i, Bundle bundle) {

            return new TrailerLinksAsyncTaskLoader(getApplicationContext(),bundle);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String trailerText) {

            if(trailerText!=null){
                try {

                    Trailer[] trailers_list= NetworkUtils.getTrailersFromJson(trailerText);

                    if(trailers_list==null || trailers_list.length==0){
                        showNoTrailerTextView();
                    }else{

                        showTrailerRecyclerView(trailers_list);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<String> reviewsLoader= new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int i, Bundle bundle) {

            return new ReviewsAsyncTaskLoader(getApplicationContext(),bundle);
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String reviewText) {

            if(reviewText!=null){
                try {
                    Review[] reviews_list = NetworkUtils.getReviewsFromJson(reviewText);


                    if(reviews_list==null || reviews_list.length==0){
                        showNoReviewTextView();
                    }else{
                        showReviewRecyclerView(reviews_list);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> queryForExistenceLoader= new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new DatabaseCursorLoader(getApplicationContext(),bundle);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            if(cursor==null){
                addToFavoritesButton.setText("Add");
                movieHasBeenAddedToTheList=false;
            }else{

                if(cursor.getCount()<1){
                    Log.d("EXISTENTA FILMULUI","NU EXISTA "+cursor.getCount());
                    addToFavoritesButton.setText("Add");
                    movieHasBeenAddedToTheList=false;
                }
                else{
                    Log.d("EXISTENTA FILMULUI","EXISTA IN BAZA DE DATE "+cursor.getCount());
                    addToFavoritesButton.setText("Remove");
                    movieHasBeenAddedToTheList=true;
                }
            }

            /*if(cursor.moveToNext()){

            }else{

            }*/
           // addToFavoritesButton.setOnTouchListener(new CustomHapticListener(100));
            addToFavoritesButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Vibrator vibe = (Vibrator) getSystemService( VIBRATOR_SERVICE );
                    vibe.vibrate( 100 );
                    //view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    if(movieHasBeenAddedToTheList==false){
                        ContentValues cv= new ContentValues();
                        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,detailMovieObject.getMovieTitle());
                        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,detailMovieObject.getReleaseDate());
                        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,detailMovieObject.getVoteAverage());
                        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,detailMovieObject.getOverview());
                        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID_IN_ONLINE_DATABSE,detailMovieObject.getOnlineId());
                        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_BLOB,detailMovieObject.getPosterByteArray());
                        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_FULL_LINK,detailMovieObject.getFullPosterLink());
                        cv.put(MovieContract.MovieEntry.COLUMN_REVIEWS_LINK,detailMovieObject.getReviewLink());
                        cv.put(MovieContract.MovieEntry.COLUMN_TRAILERS_LINK,detailMovieObject.getTrailersLink());

                        Uri uri= getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,cv);

                        if(uri!=null){
                         //   Toast.makeText(getApplicationContext(),uri.toString(),Toast.LENGTH_SHORT).show();
                        }

                        addToFavoritesButton.setText("REMOVE");
                        movieHasBeenAddedToTheList=true;
                    }else{

                        //String movie_database_id= detailMovieObject.getMovieId();
                        // int i = Integer.getInteger(movie_database_id);
                        //Log.d("MSASASAAS",movie_database_id+"@@@@@@"+String.valueOf(i));
                        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                        // uri=uri.buildUpon().appendPath(movie_database_id).build();
                        String[] selectionArgs= new String[]{detailMovieObject.getMovieTitle()};
                        getContentResolver().delete(uri,null,selectionArgs);
                        addToFavoritesButton.setText("ADD");
                        movieHasBeenAddedToTheList=false;
                    }



                }
            });

            addToFavoritesButton.setVisibility(View.VISIBLE);
//            cursor.close();

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("CONFIGURATION",movieHasBeenAddedToTheList);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void TrailerItemClick(TrailersAdapter.TrailerViewHolder item) {
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(item.getTrailerLink()));
        startActivity(intent);
    }

    private class CustomHapticListener implements View.OnTouchListener {

        // Duration in milliseconds to vibrate
        private final int durationMs;

        public CustomHapticListener( int ms ) {
            durationMs = ms;
        }

        @Override
        public boolean onTouch( View v, MotionEvent event ) {
            if( event.getAction() == MotionEvent.ACTION_DOWN ){
                Vibrator vibe = (Vibrator) getSystemService( VIBRATOR_SERVICE );
                vibe.vibrate( durationMs );
            }
            return true;
        }
    }
}
