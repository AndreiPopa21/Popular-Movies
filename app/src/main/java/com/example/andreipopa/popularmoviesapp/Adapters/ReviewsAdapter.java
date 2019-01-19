package com.example.andreipopa.popularmoviesapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andreipopa.popularmoviesapp.R;

import com.example.andreipopa.popularmoviesapp.Objects.Review;

/**
 * Created by Andrei Popa on 3/26/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>{


    private Context context;
    private LayoutInflater mInflater;
    private Review[] reviewsList;

    public ReviewsAdapter(Context context, Review[] reviewsList){
        this.context=context;
        this.mInflater= LayoutInflater.from(context);
        this.reviewsList=reviewsList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= mInflater.inflate(R.layout.review_item,parent,false);
        ReviewViewHolder reviewViewHolder= new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
         String author= reviewsList[position].getReviewAuthor();
         String content= reviewsList[position].getReviewText();

         holder.authorTextView.setText(author);
         holder.contentTextView.setText(content);
    }

    @Override
    public int getItemCount() {
        if(reviewsList==null){
            return 0;
        }else{
            if(reviewsList.length>2){
                return 2;
            }else{
                return reviewsList.length;
            }

        }
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView authorTextView;
        private TextView contentTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            authorTextView=(TextView)itemView.findViewById(R.id.review_author_textView);
            contentTextView=(TextView)itemView.findViewById(R.id.review_content_textView);
        }
    }
}
