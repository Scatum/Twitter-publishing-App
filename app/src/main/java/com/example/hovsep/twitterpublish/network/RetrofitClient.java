package com.example.hovsep.twitterpublish.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
	private static Retrofit retrofit = null;

	public static Retrofit getClient() {
		Gson gson = new GsonBuilder().setLenient().create();

		if (retrofit==null) {
			retrofit = new Retrofit.Builder()
					.baseUrl("https://api.twitter.com/")
					.addConverterFactory(GsonConverterFactory.create(gson))
					.build();
		}
		return retrofit;
	}
}
