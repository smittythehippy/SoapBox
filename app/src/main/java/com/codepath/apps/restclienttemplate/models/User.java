package com.codepath.apps.restclienttemplate.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
@Entity
public class User {
    @ColumnInfo
    public String name;
    @PrimaryKey
    public long uid;
    @ColumnInfo
    public String screenName;
    @ColumnInfo
    public String profileImageUrl;
    @ColumnInfo
    public String verified;

    public User(){

    }

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();

        user.name = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        user.verified = jsonObject.getString("verified");

        return user;
    }
}
