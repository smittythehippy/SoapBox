package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private Context context;
    private List<Tweet> tweets;
    final int radius = 20;
    final int margin = 5;

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
        if(tweet.retweet_exist == "false") {

            viewHolder.tvRetweeter.setVisibility(View.GONE);
            viewHolder.ivRetweet.setVisibility(View.GONE);

            viewHolder.tvBody.setText(tweet.body);
            viewHolder.tvScreenNameAndTime.setText("@" + tweet.user.screenName + " • " + tweet.createdAt);
            viewHolder.tvName.setText(tweet.user.name);

            //Check for media
            if(tweet.media_exists == "true") {
                GlideApp.with(context)
                        .load(tweet.mediaUrl)
                        .transforms(new CenterCrop(), new RoundedCornersTransformation(radius, margin))
                        .into(viewHolder.ivMedia);
                viewHolder.ivMedia.setVisibility(View.VISIBLE);
            }
            else if(tweet.videoUrl_exists == "true")
            {
                /* To be implemented
                GlideApp.with(context)
                        .load(tweet.videoUrl)
                        .transforms(new CenterCrop(), new RoundedCornersTransformation(radius, margin))
                        .into(viewHolder.ivMedia);
                viewHolder.ivMedia.setVisibility(View.VISIBLE);
                */
                GlideApp.with(context).clear(viewHolder.ivMedia);
                viewHolder.ivMedia.setVisibility(View.GONE);

            }
            else{
                GlideApp.with(context).clear(viewHolder.ivMedia);
                viewHolder.ivMedia.setVisibility(View.GONE);
            }
            //Check for verification of User
            if (tweet.user.verified == "true") {
                viewHolder.ivVerified.setVisibility(View.VISIBLE);
            }

            GlideApp.with(context)
                    .load(tweet.user.profileImageUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(viewHolder.ivProfilePic);
        }
        else{
            viewHolder.tvBody.setText(tweet.retweet.body);
            viewHolder.tvScreenNameAndTime.setText("@" + tweet.retweet.user.screenName + " • " + tweet.retweet.createdAt);
            viewHolder.tvName.setText(tweet.retweet.user.name);

            //Check for media
            if(tweet.retweet.media_exists == "true") {
                GlideApp.with(context)
                        .load(tweet.retweet.mediaUrl)
                        .transforms(new CenterCrop(), new RoundedCornersTransformation(radius, margin))
                        .into(viewHolder.ivMedia);
                viewHolder.ivMedia.setVisibility(View.VISIBLE);
            }
            else if(tweet.retweet.videoUrl_exists == "true")
            {
                /* To be implemented
                GlideApp.with(context)
                        .load(tweet.retweet.videoUrl)
                        .transforms(new CenterCrop(), new RoundedCornersTransformation(radius, margin))
                        .into(viewHolder.ivMedia);
                viewHolder.ivMedia.setVisibility(View.VISIBLE);
                */
                GlideApp.with(context).clear(viewHolder.ivMedia);
                viewHolder.ivMedia.setVisibility(View.GONE);

            }
            else{
                GlideApp.with(context).clear(viewHolder.ivMedia);
                viewHolder.ivMedia.setVisibility(View.GONE);
            }

            //Check for verification of User
            if (tweet.retweet.user.verified == "true") {
                viewHolder.ivVerified.setVisibility(View.VISIBLE);
            }

            viewHolder.ivRetweet.setVisibility(View.VISIBLE);
            viewHolder.tvRetweeter.setVisibility(View.VISIBLE);
            viewHolder.tvRetweeter.setText(tweet.user.name + " Retweeted");

            GlideApp.with(context)
                    .load(tweet.retweet.user.profileImageUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(viewHolder.ivProfilePic);
        }

        Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            @Override
            public final String transformUrl(final Matcher match, String url) {
                return match.group();
            }
        };

        Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
        Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9_-]+)");
        Pattern urlPattern = Patterns.WEB_URL;

        String mentionScheme = "http://www.twitter.com/";
        String hashtagScheme = "http://www.twitter.com/search/";

        Linkify.addLinks(viewHolder.tvBody, mentionPattern, mentionScheme, null, filter);
        Linkify.addLinks(viewHolder.tvBody, hashtagPattern, hashtagScheme, null, filter);
        Linkify.addLinks(viewHolder.tvBody, urlPattern, null, null, filter);
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
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvBody;
        public TextView tvScreenNameAndTime;
        public ImageView ivProfilePic;
        public TextView tvName;
        public ImageView ivVerified;
        public TextView tvRetweeter;
        public ImageView ivRetweet;
        public ImageView ivMedia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvScreenNameAndTime = itemView.findViewById(R.id.tvScreenNameAndTime);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvName = itemView.findViewById(R.id.tvName);
            ivVerified = itemView.findViewById(R.id.ivVerified);
            tvRetweeter = itemView.findViewById(R.id.tvRetweeter);
            ivRetweet = itemView.findViewById(R.id.ivRetweet);
            ivMedia = itemView.findViewById(R.id.ivMedia);

            ivMedia.setVisibility(View.GONE);
            ivVerified.setVisibility(View.GONE);
        }
    }
}
            /*To use with detailed tweet view
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
            Date converted = new Date();

            try {
                converted = dateFormat.parse(tweet.createdAt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            PrettyTime reformat = new PrettyTime();
            String timestamp = reformat.format(converted);
            */
