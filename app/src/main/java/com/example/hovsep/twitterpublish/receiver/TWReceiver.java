package com.example.hovsep.twitterpublish.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.hovsep.twitterpublish.manager.TWManager;
import com.example.hovsep.twitterpublish.model.MyTweet;
import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

public class TWReceiver extends BroadcastReceiver {

	final String TAG = getClass().getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
			Log.e(TAG, "Published ");
			MyTweet myTweet = TWManager.getLastTweet(context);
			myTweet.setPublished(true);
			TWManager.makeTweetPublished(context, myTweet);
			final Long tweetId = intent.getExtras().getLong(TweetUploadService.EXTRA_TWEET_ID);
		} else if (TweetUploadService.UPLOAD_FAILURE.equals(intent.getAction())) {
			final Intent retryIntent = intent.getExtras().getParcelable(TweetUploadService.EXTRA_RETRY_INTENT);
			Log.e(TAG, "failure");

		} else if (TweetUploadService.TWEET_COMPOSE_CANCEL.equals(intent.getAction())) {
			Log.e(TAG, "cancel");
		}
	}
}