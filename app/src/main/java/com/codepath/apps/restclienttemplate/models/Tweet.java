package com.codepath.apps.restclienttemplate.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
@Entity
public class Tweet {

    @ColumnInfo
    public String body;
    @PrimaryKey
    public long uid;
    @ColumnInfo
    public String createdAt;
    @ColumnInfo
    public User user;
    @ColumnInfo
    public String retweet_exist;
    @ColumnInfo
    public Tweet retweet;
    @ColumnInfo
    public String media_exists;
    @ColumnInfo
    public String mediaUrl;

    public String videoUrl_exists;

    public String videoUrl;

    public Tweet(){

    }
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("full_text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.createdAt = TimeFormatter.getTimeDifference(tweet.createdAt);
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        // Check if tweet is a retweet
        if (jsonObject.has("retweeted_status")) {
            tweet.retweet_exist = "true";

            tweet.retweet = new Tweet();
            tweet.retweet.body = jsonObject.getJSONObject("retweeted_status").getString("full_text");
            tweet.retweet.uid = jsonObject.getJSONObject("retweeted_status").getLong("id");
            tweet.retweet.createdAt = jsonObject.getJSONObject("retweeted_status").getString("created_at");
            tweet.retweet.createdAt = TimeFormatter.getTimeDifference(tweet.retweet.createdAt);
            tweet.retweet.user = User.fromJson(jsonObject.getJSONObject("retweeted_status").getJSONObject("user"));

            // Check for embedded media
            if(jsonObject.getJSONObject("retweeted_status").has("extended_entities")) {
                String temp = jsonObject.getJSONObject("retweeted_status").getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0).getString("type");
                if(temp.equals("photo")) {
                    tweet.retweet.media_exists = "true";
                    tweet.retweet.videoUrl_exists = "false";
                    tweet.retweet.mediaUrl = jsonObject.getJSONObject("retweeted_status")
                            .getJSONObject("extended_entities")
                            .getJSONArray("media")
                            .getJSONObject(0)
                            .getString("media_url_https");
                }
                else if(temp.equals("video")) {
                    if (jsonObject.getJSONObject("retweeted_status").getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0).has("video_info")) {
                        tweet.retweet.videoUrl_exists = "true";
                        tweet.retweet.media_exists = "false";
                        tweet.retweet.videoUrl = jsonObject.getJSONObject("retweeted_status")
                                .getJSONObject("extended_entities")
                                .getJSONArray("media")
                                .getJSONObject(1)
                                .getJSONObject("video_url")
                                .getJSONArray("variants")
                                .getJSONObject(0)
                                .getString("url");
                    }
                }
            }
            else {
                tweet.retweet.media_exists = "false";
                tweet.retweet.videoUrl_exists = "false";
            }
        }
        else if (jsonObject.has("extended_entities")) {
            tweet.retweet_exist = "false";

            // Check for embedded media
            String temp = jsonObject.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0).getString("type");
            if(temp.equals("photo")){
                tweet.media_exists = "true";
                tweet.videoUrl_exists = "false";
                tweet.mediaUrl = jsonObject.getJSONObject("extended_entities")
                        .getJSONArray("media")
                        .getJSONObject(0)
                        .getString("media_url_https");
            }
            else if(temp.equals("video")) {
                if (jsonObject.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0).has("video_info")) {
                    tweet.videoUrl_exists = "true";
                    tweet.media_exists = "false";
                    tweet.videoUrl = jsonObject.getJSONObject("extended_entities")
                            .getJSONArray("media")
                            .getJSONObject(1)
                            .getJSONObject("video_url")
                            .getJSONArray("variants")
                            .getJSONObject(0)
                            .getString("url");
                }
            }
        }
        else {
            tweet.retweet_exist = "false";
            tweet.media_exists = "false";
            tweet.videoUrl_exists = "false";
        }


        return tweet;
    }
}
