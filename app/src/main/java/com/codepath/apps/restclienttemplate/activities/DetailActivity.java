package com.codepath.apps.restclienttemplate.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.GlideApp;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TimeFormatter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailActivity extends AppCompatActivity {
    final int radius = 60;
    final int margin = 5;

    private Tweet tweet;
    private TextView tvBody;
    private TextView tvName;
    private TextView tvScreenName;
    private TextView tvCreatedAt;
    private TextView tvRetweetCount;
    private TextView tvLikeCount;
    private ImageView ivVerified;
    private ImageView ivProfilePic;
    private ImageView ivMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        if(tweet.retweet_exist.equals("true")){
            tweet = tweet.retweet;
        }

        tvBody = findViewById(R.id.tvBody);
        tvName = findViewById(R.id.tvName);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvRetweetCount = findViewById(R.id.tvRetweetCount);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        ivMedia = findViewById(R.id.ivMedia);
        ivVerified = findViewById(R.id.ivVerified);

        ivMedia.setVisibility(View.GONE);
        ivVerified.setVisibility(View.GONE);

        tvBody.setText(tweet.body);
        tvName.setText(tweet.user.name);
        tvScreenName.setText("@" + tweet.user.screenName);
        String tempTime = TimeFormatter.getTimeStamp(tweet.createdAt);
        tvCreatedAt.setText(tempTime);

        tvRetweetCount.setText(tweet.retweetCount + " Retweets");
        tvLikeCount.setText(tweet.likeCount + " Likes");

        GlideApp.with(this)
                .load(tweet.user.profileImageUrl)
                .apply(new RequestOptions().circleCrop())
                .into(ivProfilePic);

        //Check for verification of User
        if (tweet.user.verified == "true") {
            ivVerified.setVisibility(View.VISIBLE);
        }

        if(tweet.media_exists.equals("true")){
            GlideApp.with(this)
                    .load(tweet.mediaUrl)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivMedia);
            ivMedia.setVisibility(View.VISIBLE);
        }
        else{
            GlideApp.with(this).clear(ivMedia);
            ivMedia.setVisibility(View.GONE);
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

        Linkify.addLinks(tvBody, mentionPattern, mentionScheme, null, filter);
        Linkify.addLinks(tvBody, hashtagPattern, hashtagScheme, null, filter);
        Linkify.addLinks(tvBody, urlPattern, null, null, filter);
    }
}
