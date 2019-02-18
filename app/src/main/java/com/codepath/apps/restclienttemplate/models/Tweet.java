package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
    public String body;
    public long uid;
    public String createdAt;
    public User user;
    //public String retweet_exist;
    //public Tweet retweet;

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //Populate tweet
       // tweet.retweet_exist = jsonObject.getString("retweet");

        tweet.body = jsonObject.getString("full_text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        /*if(jsonObject.getJSONObject("retweeted_status").length() > 0) {
            tweet.retweet = Tweet.fromJson(jsonObject.getJSONObject("retweeted_status"));
            tweet.retweet_exist = "true";
            tweet.retweet.body = jsonObject.getString("full_text");
            tweet.retweet.uid = jsonObject.getLong("id");
            tweet.retweet.createdAt = jsonObject.getString("created_at");
            tweet.retweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        }else {
            tweet.retweet_exist = "false";
        }*/
        return tweet;
    }
}
