package com.example.andreipopa.popularmoviesapp.Objects;

/**
 * Created by Andrei Popa on 3/26/2018.
 */

public class Review {

    private String reviewAuthor;
    private String reviewText;

    public Review(String reviewAuthor,String reviewText){

        this.reviewAuthor=reviewAuthor;
        this.reviewText= reviewText;
    }

    public void setReviewText(String newReviewText){
        this.reviewText=newReviewText;
    }
    public String getReviewText(){
        return this.reviewText;
    }
    public void setReviewAuthor(String newReviewAuthor){
        this.reviewAuthor= newReviewAuthor;
    }
    public String getReviewAuthor()
    {
        return this.reviewAuthor;
    }
}
