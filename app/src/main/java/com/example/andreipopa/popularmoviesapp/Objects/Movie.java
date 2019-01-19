package com.example.andreipopa.popularmoviesapp.Objects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.ByteArrayOutputStream;


public class Movie implements Parcelable{

    private String title;
    private String release_date;
    private String overview;
    private String vote_average;
    private String online_id;
    private String reviewsLink;
    private String trailersLink;
    private String full_poster_link;
    private byte[] posterByteArray;



    private final String FETCH_IMAGE_BASE_URL="http://image.tmdb.org/t/p/";
    private final String RECOMMENDED_IMAGE_URL_SIZE="w342";

    public Movie(String movie_title, String movie_release_date,
                 String movie_vote_average, String movie_overview,
                 String movie_online_id, String movie_full_poster_link,
                 String movie_trailersLink, String movie_reviewsLink,
                 byte[] movie_posterByteArray){


        this.title= movie_title;
        this.release_date= movie_release_date;
        this.vote_average= movie_vote_average;
        this.overview= movie_overview;
        this.online_id= movie_online_id;
        this.full_poster_link= movie_full_poster_link;
        this.trailersLink= movie_trailersLink;
        this.reviewsLink=movie_reviewsLink;
        this.posterByteArray=movie_posterByteArray;

    }

    public String getMovieTitle(){
        return this.title;
    }
    public void setMovieTitle(String movieTitle){
        this.title=movieTitle;
    }

    public String getReleaseDate(){
        return release_date;
    }
    public void setReleaseDate(String date){
        release_date= date;
    }

    public String getVoteAverage(){
        return this.vote_average;
    }
    public void setVoteAverage(String average){
        this.vote_average= average;
    }

    public String getOverview(){
        return this.overview;
    }
    public void setMovieOverview(String overview){
        this.overview= overview;
    }

    public String getOnlineId(){return this.online_id;};
    public void setOnlineId(String new_id){
        this.online_id=new_id;
    }

    public String getFullPosterLink(){
        return this.full_poster_link;
    }
    public void setFullPosterLink(String url){
        this.full_poster_link=url;
    }

    public String getReviewLink(){
        return this.reviewsLink;
    }
    public void setReviewsLink(String reviewsLink){
        this.reviewsLink= reviewsLink;
    }

    public String getTrailersLink(){
        return this.trailersLink;
    }
    public void setTrailersLink(String trailersLink){
        this.trailersLink= trailersLink;
    }

    public byte[] getPosterByteArray(){
        return this.posterByteArray;
    }
    public void setPosterByteArray(byte[] byteArray){
        this.posterByteArray=byteArray;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {

        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(vote_average);
        dest.writeString(overview);
        dest.writeString(online_id);
        dest.writeString(full_poster_link);
        dest.writeString(reviewsLink);
        dest.writeString(trailersLink);
        dest.writeInt(posterByteArray.length);
        dest.writeByteArray(posterByteArray);

    }

    public Movie(Parcel parcel){

        this.title=parcel.readString();
        this.release_date=parcel.readString();
        this.vote_average=parcel.readString();
        this.overview=parcel.readString();
        this.online_id=parcel.readString();
        this.full_poster_link=parcel.readString();
        this.reviewsLink=parcel.readString();
        this.trailersLink=parcel.readString();

        int length=parcel.readInt();
        byte[] containerPoster= new byte[length];
        parcel.readByteArray(containerPoster);
        this.posterByteArray=containerPoster;

    }

    public static final Parcelable.Creator<Movie> CREATOR= new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[0];
        }
    };
}
