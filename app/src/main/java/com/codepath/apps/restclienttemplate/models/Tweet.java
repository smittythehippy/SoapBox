package com.codepath.apps.restclienttemplate.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

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
    public String retweet_exist;
    public Tweet retweet;

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //Populate tweet
       // tweet.retweet_exist = jsonObject.getString("retweet");

        tweet.body = jsonObject.getString("full_text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

         if (jsonObject.has("retweeted_status")) {
             tweet.retweet_exist = "true";

             tweet.retweet = new Tweet();
             tweet.retweet.body = jsonObject.getJSONObject("retweeted_status").getString("full_text");
             tweet.retweet.uid = jsonObject.getJSONObject("retweeted_status").getLong("id");
             tweet.retweet.createdAt = jsonObject.getJSONObject("retweeted_status").getString("created_at");
             tweet.retweet.user = User.fromJson(jsonObject.getJSONObject("retweeted_status").getJSONObject("user"));

         } else {
             tweet.retweet_exist = "false";
         }

        return tweet;
    }
}
