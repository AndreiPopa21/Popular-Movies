package com.example.andreipopa.popularmoviesapp.Objects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.andreipopa.popularmoviesapp.R;

import com.example.andreipopa.popularmoviesapp.Adapters.ReviewsAdapter;
import com.example.andreipopa.popularmoviesapp.Adapters.TrailersAdapter;

/**
 * Created by Andrei Popa on 3/30/2018.
 */

public class DetailFragmentObjects {

    @SuppressLint("ValidFragment")
    public static class NoReviewTextViewFragment extends android.support.v4.app.Fragment{

        public TextView textViewForNoReview;

        @SuppressLint("ValidFragment")
        public NoReviewTextViewFragment(){

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View rootView= inflater.inflate(R.layout.no_review_text_view,container,false);
            textViewForNoReview= rootView.findViewById(R.id.text_view_for_no_review);

            return rootView;

        }
    }

    @SuppressLint("ValidFragment")
    public static class NoTrailerTextViewFragment extends android.support.v4.app.Fragment{

        public TextView textViewForNoTrailer;

        @SuppressLint("ValidFragment")
        public NoTrailerTextViewFragment(){

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View rootView= inflater.inflate(R.layout.no_trailer_text_view,container,false);
            textViewForNoTrailer= rootView.findViewById(R.id.text_view_for_no_trailer);

            return rootView;

        }
    }



    @SuppressLint("ValidFragment")
    public static class ReviewLoadingProgressBar extends android.support.v4.app.Fragment{

        public ProgressBar reviewLoadingProgressBar;

        @SuppressLint("ValidFragment")
        public ReviewLoadingProgressBar(){
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View rootView= inflater.inflate(R.layout.loading_reviews_progress_bar,container,false);
            reviewLoadingProgressBar= rootView.findViewById(R.id.loading_reviews_progressBar);
            return rootView;
        }
    }

    @SuppressLint("ValidFragment")
    public static class ReviewListRecyclerView extends android.support.v4.app.Fragment{

        public RecyclerView reviewsRecyclerView;
        public Context context;
        public Review[] reviewsList;

        @SuppressLint("ValidFragment")
        public ReviewListRecyclerView(Context context,Review[] reviewsList){
            this.context=context;
            this.reviewsList=reviewsList;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View rootView= inflater.inflate(R.layout.review_list_recycler_view,container,false);


            setRetainInstance(true);
            reviewsRecyclerView=rootView.findViewById(R.id.reviews_recycler_view);
            reviewsRecyclerView.setHasFixedSize(false);
            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false);
            reviewsRecyclerView.setLayoutManager(linearLayoutManager);
            ReviewsAdapter reviewsAdapter= new ReviewsAdapter(context,reviewsList);
            reviewsRecyclerView.setAdapter(reviewsAdapter);

            return rootView;

        }
    }

    @SuppressLint("ValidFragment")
    public static class TrailerListRecyclerView extends android.support.v4.app.Fragment{

        public RecyclerView trailersRecyclerView;
        public Context context;
        public Trailer[] trailersList;
        public TrailersAdapter.TrailerItemClickListener mTrailerClickListener;

        @SuppressLint("ValidFragment")
        public TrailerListRecyclerView(Context context, Trailer[] trailersList, TrailersAdapter.TrailerItemClickListener mTrailerItemClickListener){
            this.context=context;
            this.trailersList=trailersList;
            this.mTrailerClickListener=mTrailerItemClickListener;

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View rootView= inflater.inflate(R.layout.trailer_list_recycler_view,container,false);
            setRetainInstance(true);
            trailersRecyclerView=rootView.findViewById(R.id.trailers_recycler_view);
            trailersRecyclerView.setHasFixedSize(false);
            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false);
            trailersRecyclerView.setLayoutManager(linearLayoutManager);
            TrailersAdapter trailersAdapter= new TrailersAdapter(context,trailersList,mTrailerClickListener);
            trailersRecyclerView.setAdapter(trailersAdapter);

            return rootView;

        }
    }

}

