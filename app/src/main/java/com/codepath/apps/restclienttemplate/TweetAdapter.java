package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private Context context;
    private List<Tweet> tweets;

    public TweetAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }
    //Pass in the context and list of tweets


    //For each row, inflate item_tweet layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Create View by inflating, then wrap in ViewHolder so we can use it
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, viewGroup, false);
        return new ViewHolder(view);
    }

    //Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Tweet tweet = tweets.get(i);

        //Convert file
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        Date converted = new Date();
        try {
            converted = dateFormat.parse(tweet.createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PrettyTime reformat = new PrettyTime();
        String timestamp = reformat.format(converted);

        viewHolder.tvBody.setText(tweet.body);
        viewHolder.tvScreenNameAndTime.setText("@" + tweet.user.screenName + " • " + timestamp);
        viewHolder.tvName.setText(tweet.user.name);

        //Check for verification of User
        if(tweet.user.verified == "true"){
            viewHolder.ivVerified.setVisibility(View.VISIBLE);
        }

        // Check to see if tweet is a retweet
       /* if(tweet.retweet_exist == "true"){
            viewHolder.tvRetweetHeader.setText(tweet.user.name + " Retweeted");
        }*/

        GlideApp.with(context)
                .load(tweet.user.profileImageUrl)
                .apply(new RequestOptions().circleCrop())
                .into(viewHolder.ivProfilePic);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //Clear list of old items from Recycler View
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    //Add a list of items
    public void addTweets(List<Tweet> tweetsList) {
        tweets.addAll(tweetsList);
        notifyDataSetChanged();
    }

    //Define Viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvBody;
        public TextView tvScreenNameAndTime;
        public ImageView ivProfilePic;
        public TextView tvName;
        public ImageView ivVerified;
     //   public TextView tvRetweetHeader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvScreenNameAndTime = itemView.findViewById(R.id.tvScreenNameAndTime);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvName = itemView.findViewById(R.id.tvName);
            ivVerified = itemView.findViewById(R.id.ivVerified);
            ivVerified.setVisibility(View.INVISIBLE);
        }
    }
}
