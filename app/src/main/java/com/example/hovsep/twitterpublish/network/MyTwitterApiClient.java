package com.example.hovsep.twitterpublish.network;

import com.example.hovsep.twitterpublish.api.ApiRequest;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

public class MyTwitterApiClient extends TwitterApiClient {
	public MyTwitterApiClient(TwitterSession session) {
		super(session);
	}
	public ApiRequest getCustomService() {
		return getService(ApiRequest.class);
	}
}
