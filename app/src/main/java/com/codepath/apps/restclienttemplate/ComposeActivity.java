package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private EditText etCompose;
    private Button btnTweet;
    private TwitterClient client;
    private static final int TWEET_LIMIT = 280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        // Need a click listener on button to make
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Nothing to say?", Toast.LENGTH_LONG).show();
                }
                else if (tweetContent.length() > TWEET_LIMIT) {
                    Toast.makeText(ComposeActivity.this, "Woah! Too much, try something shorter!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();

                    client.composeTweet(tweetContent, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("TwitterClient", "Successefully posted tweet: " + response.toString());
                            try {
                                Tweet tweet = Tweet.fromJson(response);
                                Intent data = new Intent();
                                data.putExtra("tweet", Parcels.wrap(tweet));
                                setResult(RESULT_OK, data);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.e("TwitterClient", "Failed to post tweet");
                        }
                    });

                }

            }

        });
        // API call to twitter to publish the content in edit text

    }
}
