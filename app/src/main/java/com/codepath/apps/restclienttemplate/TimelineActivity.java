package com.codepath.apps.restclienttemplate;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetAdapter adapter;
    private List<Tweet> tweets;
    private RecyclerView rvTweets;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private long lowestId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        // Find swipe to refresh layout
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Find recycler view from timeline
        rvTweets = findViewById(R.id.rvTweets);

        // Initialize list of tweets and adapter from tha data source
        tweets = new ArrayList<>();
        adapter = new TweetAdapter(this, tweets);

        // Setup recycler view: layout manager and setting adapter
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(llm);
        rvTweets.setAdapter(adapter);
        rvTweets.addItemDecoration(new DividerItemDecoration(rvTweets.getContext(), DividerItemDecoration.VERTICAL));

        populateHomeTimeline();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("TwitterClient", "Content is being refreshed");
                populateHomeTimeline();
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("TimelineActivity", "reached end of page");
                loadMoreData();
            }
        };
        rvTweets.addOnScrollListener(scrollListener);
    }

    private void loadMoreData() {
        client.getNextPageOfTweets(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("MoreTweets", "success");
                //Prepare adapter and for more items and reset scroll listener

                // Iterate through JSON array(list of tweets)
                List<Tweet> moreTweets = new ArrayList<>();

                if(response.length() <= 0) {
                    Log.d("MoreTweets", "no data found");
                }
                for(int i = 0; i < response.length(); i++){
                    try {
                        // Convert each JSON object into a tweet object
                        JSONObject jsonTweetObject = response.getJSONObject(i);
                        Tweet tweet = Tweet.fromJson(jsonTweetObject);

                        // Add tweet into data source(list)
                        moreTweets.add(tweet);

                        //Test for newest low Id
                        if(tweet.uid < lowestId)
                        {lowestId = tweet.uid;}

                        // Notify adapter
                        adapter.notifyItemInserted(tweets.size() - 1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //clear existing data

                //show data we just received
                adapter.addTweets(moreTweets);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("MoreTweets", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("MoreTweets", responseString);
            }
        }, lowestId - 1);

    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Iterate through JSON array(list of tweets)
                List<Tweet> tweetsToAdd = new ArrayList<>();

                for(int i = 0; i < response.length(); i++){
                    try {
                        // Convert each JSON object into a tweet object
                        JSONObject jsonTweetObject = response.getJSONObject(i);
                        Tweet tweet = Tweet.fromJson(jsonTweetObject);
                        /*if(tweet.retweet != null){
                            tweet = tweet.retweet;
                        }*/
                        // Add tweet into data source(list)
                        tweetsToAdd.add(tweet);

                        if(tweet.uid < lowestId || lowestId == 0)
                        {lowestId = tweet.uid;}

                        // Notify adapter
                        adapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //clear existing data
                adapter.clear();
                //show data we just received
                adapter.addTweets(tweetsToAdd);

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
            }
        });
    }
}
