package com.example.hovsep.twitterpublish.api;

import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jr on 3/20/17.
 */

public interface ApiRequest {
	@GET("/1.1/users/show.json")
	Call<User> show(@Query("user_id") long userId);

	@GET("https://gnip-api.twitter.com/metrics/usage/accounts/{name}.json")
	Call<User> showTweets(@Path("name") String name);

}
