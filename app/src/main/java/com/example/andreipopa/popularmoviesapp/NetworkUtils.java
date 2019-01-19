package com.example.andreipopa.popularmoviesapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import com.example.andreipopa.popularmoviesapp.Objects.Movie;
import com.example.andreipopa.popularmoviesapp.Objects.Review;
import com.example.andreipopa.popularmoviesapp.Objects.Trailer;


public class NetworkUtils {

    private static final String FETCH_IMAGE_BASE_URL="http://image.tmdb.org/t/p/";
    private static final String RECOMMENDED_IMAGE_URL_SIZE="w342";


    private static final String MOVIE_DATABASE_BASE_URL="http://api.themoviedb.org/3/movie";
    private static final String YOUTUBE_VIDEO_BASE_URL="https://www.youtube.com/watch";
    private static final String YOUTUBE_THUMBNAIL_BASE_URL="https://img.youtube.com/vi";
    private static final String YOUTUBE_DEFAULT_THUMBNAIL_PATH_PARAMETER="default.jpg";

    private static final String JSON_RESULT_ARRAY="results";
    private static final String JSON_TITLE_STRING="title";
    private static final String JSON_VOTE_AVERAGE_DOUBLE="vote_average";
    private static final String JSON_RELEASE_DATE_STRING="release_date";
    private static final String JSON_IMAGE_SPECIFIC_PATH_STRING="poster_path";
    private static final String JSON_MOVIE_OVERVIEW_STRING="overview";
    private static final String JSON_MOVIE_ID_DOUBLE="id";
    private static final String JSON_TRAILER_KEY_STRING="key";
    private static final String JSON_REVIEW_AUTHOR_STRING="author";
    private static final String JSON_REVIEW_CONTENT_STRING="content";


    public static Movie[] createOnlineMovieList(String jsonResponse) throws JSONException {

        if(jsonResponse==null || jsonResponse.isEmpty()){
            return null;
        }

        JSONObject reader = new JSONObject(jsonResponse);

        if(reader.has(JSON_RESULT_ARRAY)){
            JSONArray resultsArray= reader.getJSONArray(JSON_RESULT_ARRAY);
            Movie[] movieList=new Movie[resultsArray.length()];
            for(int i=0;i<resultsArray.length();i++){

                String movie_title="";
                String movie_release_date="";
                String movie_vote_average="";
                String movie_overview="";
                String movie_onlineId="";
                String movie_full_poster_link="";
                String movie_trailersLink="";
                String movie_reviewsLink="";
                byte[] movie_posterByteArray=null;

                JSONObject movieObject= resultsArray.getJSONObject(i);

                if(movieObject.has(JSON_TITLE_STRING)){
                    movie_title= movieObject.optString(JSON_TITLE_STRING);
                }

                if(movieObject.has(JSON_RELEASE_DATE_STRING)){
                    movie_release_date= movieObject.optString(JSON_RELEASE_DATE_STRING);
                }

                if(movieObject.has(JSON_VOTE_AVERAGE_DOUBLE)){
                    double average= movieObject.getDouble(JSON_VOTE_AVERAGE_DOUBLE);
                    movie_vote_average= Double.toString(average);
                }

                if(movieObject.has(JSON_MOVIE_OVERVIEW_STRING)){
                    movie_overview=movieObject.optString(JSON_MOVIE_OVERVIEW_STRING);
                }

                if(movieObject.has(JSON_MOVIE_ID_DOUBLE)){
                    double id= movieObject.getDouble(JSON_MOVIE_ID_DOUBLE);
                    movie_onlineId=Double.toString(id);
                }


                if(movieObject.has(JSON_IMAGE_SPECIFIC_PATH_STRING)){
                    String movie_poster_path= movieObject.optString(JSON_IMAGE_SPECIFIC_PATH_STRING);
                    URL full_poster_url= buildFullPosterLink(movie_poster_path);
                    movie_full_poster_link= full_poster_url.toString();
                }

                URL reviewUrl= NetworkUtils.buildMovieInfosUrl(MovieQueryTypeSelector.REVIEWS,movie_onlineId);
                movie_reviewsLink=reviewUrl.toString();

                URL trailersURL= NetworkUtils.buildMovieInfosUrl(MovieQueryTypeSelector.TRAILER,movie_onlineId);
                movie_trailersLink=trailersURL.toString();


                Movie newMovie= new Movie(movie_title,
                        movie_release_date,
                        movie_vote_average,
                        movie_overview,
                        movie_onlineId,
                        movie_full_poster_link,
                        movie_trailersLink,
                        movie_reviewsLink,
                        movie_posterByteArray
                );

                movieList[i]=newMovie;

            }
            return movieList;
        }

        return null;
    }

    public static Review[] getReviewsFromJson(String jsonResponse) throws JSONException {

        if(jsonResponse==null || jsonResponse.isEmpty()){
            return null;
        }

        JSONObject reader = new JSONObject(jsonResponse);

        if(reader.has(JSON_RESULT_ARRAY)){
            JSONArray resultsArray= reader.getJSONArray(JSON_RESULT_ARRAY);
            Review[] reviewsList= new Review[resultsArray.length()];
            for(int i=0;i<reviewsList.length;i++){

                String reviewAuthor="";
                String reviewText="";

                JSONObject reviewObject= resultsArray.getJSONObject(i);

                if(reviewObject.has(JSON_REVIEW_AUTHOR_STRING)){
                    reviewAuthor=reviewObject.optString(JSON_REVIEW_AUTHOR_STRING);
                }

                if(reviewObject.has(JSON_REVIEW_CONTENT_STRING)){
                    reviewText=reviewObject.optString(JSON_REVIEW_CONTENT_STRING);
                }

                Review newReview= new Review(reviewAuthor,reviewText);

                reviewsList[i]=newReview;


            }
            return reviewsList;
        }
        return null;
    }

    public static Trailer[] getTrailersFromJson(String jsonResponse) throws JSONException {

        if(jsonResponse==null || jsonResponse.isEmpty()){
            return null;
        }

        JSONObject reader = new JSONObject(jsonResponse);

        if(reader.has(JSON_RESULT_ARRAY)){
            JSONArray resultsArray= reader.getJSONArray(JSON_RESULT_ARRAY);
            Trailer[] trailersList= new Trailer[resultsArray.length()];

            for(int i=0;i<trailersList.length;i++){

                String trailerLink="";
                String thumbnailLink="";

                JSONObject trailerObject= resultsArray.getJSONObject(i);

                String trailerKey="";

                if(trailerObject.has(JSON_TRAILER_KEY_STRING)){
                    trailerKey=trailerObject.optString(JSON_TRAILER_KEY_STRING);
                }

                trailerLink=NetworkUtils.buildTrailerLink(trailerKey).toString();
                thumbnailLink=NetworkUtils.buildThumbnailLink(trailerKey).toString();

                trailersList[i]= new Trailer(trailerLink,thumbnailLink);

            }
            return  trailersList;
        }
        return null;

    }

    public static URL buildTrailerLink(String trailerKey){

        Uri builtUri=Uri.parse(YOUTUBE_VIDEO_BASE_URL).buildUpon()
                .appendQueryParameter("v",trailerKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildThumbnailLink(String trailerKey){

        Uri builtUri=Uri.parse(YOUTUBE_THUMBNAIL_BASE_URL).buildUpon()
                .appendPath(trailerKey)
                .appendPath(YOUTUBE_DEFAULT_THUMBNAIL_PATH_PARAMETER)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static URL buildMovieListUrl(int query_type){

        String new_path="";

        switch (query_type){
            case MovieQueryTypeSelector.POPULAR:
                new_path="popular";
                break;
            case MovieQueryTypeSelector.TOP_RATED:
                new_path="top_rated";
                break;
            case MovieQueryTypeSelector.UPCOMING:
                new_path="upcoming";
                break;
        }

        Uri builtUri = Uri.parse(MOVIE_DATABASE_BASE_URL).buildUpon()
                .appendPath(new_path)
                .appendQueryParameter("api_key",BuildConfig.MY_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Log.v("THIS IS MY TAG", url.toString());
        return url;
    }

    public static URL buildMovieInfosUrl(int query_type, String movie_id){
        String new_path="";

        switch(query_type){
            case MovieQueryTypeSelector.TRAILER:
                new_path="videos";
                break;
            case MovieQueryTypeSelector.REVIEWS:
                new_path="reviews";
                break;
        }
        Uri builtUri= Uri.parse(MOVIE_DATABASE_BASE_URL).buildUpon()
                .appendPath(movie_id)
                .appendPath(new_path)
                .appendQueryParameter("api_key",BuildConfig.MY_API_KEY)
                .build();

        URL url=null;
        try{
            url= new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttp(URL url) throws IOException{

        String moviesJsonResponse="";
        HttpURLConnection httpURLConnection=null;
        InputStream moviesInputStream=null;

        try{
            httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            moviesInputStream= httpURLConnection.getInputStream();

            if(moviesInputStream!=null){
                StringBuilder jsonString= new StringBuilder();
                InputStreamReader inputStreamReader= new InputStreamReader(moviesInputStream, Charset.defaultCharset());
                BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
                String newLine= bufferedReader.readLine();
                while(newLine!=null){
                    jsonString.append(newLine);
                    newLine=bufferedReader.readLine();
                }
                moviesJsonResponse=jsonString.toString();

            }
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if(moviesInputStream!=null){
                moviesInputStream.close();
            }
        }

        return moviesJsonResponse;
    }

    public static URL buildFullPosterLink(String poster_short_link){

        String urlPattern="";

        urlPattern= new StringBuilder().append(FETCH_IMAGE_BASE_URL).
                append(RECOMMENDED_IMAGE_URL_SIZE).
                append(poster_short_link).toString();

        try {
            URL url = new URL(urlPattern);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static Bitmap convertByteArrayToBitmap(byte[] byteArray){

        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);

        return bitmap;
    }


}
