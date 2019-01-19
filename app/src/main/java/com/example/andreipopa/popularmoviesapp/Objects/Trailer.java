package com.example.andreipopa.popularmoviesapp.Objects;

/**
 * Created by Andrei Popa on 3/26/2018.
 */

public class Trailer {

    private String trailerLink;
    private String thumbnailLink;

    public Trailer(String trailerLink, String thumbnailLink){
        this.trailerLink=trailerLink;
        this.thumbnailLink= thumbnailLink;
    }

    public void setTrailerLink(String newTrailerLink){
        this.trailerLink= newTrailerLink;
    }
    public String getTrailerLink(){
        return this.trailerLink;
    }
    public void setThumbnailLinks(String newThumbnailLink){
        this.trailerLink= newThumbnailLink;
    }
    public String getThumbnailLinks(){
        return this.thumbnailLink;
    }

}
