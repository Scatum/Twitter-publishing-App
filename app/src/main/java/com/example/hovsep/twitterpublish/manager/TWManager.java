package com.example.hovsep.twitterpublish.manager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.example.hovsep.twitterpublish.DAO.DB;
import com.example.hovsep.twitterpublish.activity.AutActivity;
import com.example.hovsep.twitterpublish.activity.LandingActivity;
import com.example.hovsep.twitterpublish.api.ApiRequest;
import com.example.hovsep.twitterpublish.fragment.TweetsHistoryFragment;
import com.example.hovsep.twitterpublish.model.MyTweet;
import com.example.hovsep.twitterpublish.network.MyTwitterApiClient;
import com.example.hovsep.twitterpublish.network.RetrofitClient;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

import retrofit2.Call;
import retrofit2.Callback;

public class TWManager {

	public static void publish(Activity context, Uri imageUri, String text) {
		final TwitterSession session = TwitterCore.getInstance().getSessionManager()
				.getActiveSession();
		final Intent intent = new ComposerActivity.Builder(context)
				.session(session)
				.image(imageUri)
				.text(text)
				.hashtags("#mobileAppTesting")
				.createIntent();
		context.startActivityForResult(intent, 10);
	}


	public static void getTweetsHistory(Context context, TweetsHistoryFragment.DBListener dbListener) {
		DB db = DB.getInstance(context);
		db.getAllTweets(dbListener);
	}

	public static MyTweet getLastTweet(Context context) {
		DB db = DB.getInstance(context);
		return db.getLastInsertedItem();
	}

	public static void makeTweetPublished(Context context, MyTweet myTweet) {
		DB db = DB.getInstance(context);
		db.updateMyTweets(myTweet);
	}

	public static TwitterSession getTwitterSession() {
		return TwitterCore.getInstance().getSessionManager().getActiveSession();
	}

	public static void logoutTwitter(Activity context) {
		if (context == null) {
			return;
		}
		TwitterSession twitterSession = getTwitterSession();
		if (twitterSession != null) {
			ClearCookies(context);
			TwitterCore.getInstance().getSessionManager().clearActiveSession();
			context.startActivity(new Intent(context, AutActivity.class));
			context.finish();
		}
	}

	private static void ClearCookies(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
			CookieManager.getInstance().removeAllCookies(null);
			CookieManager.getInstance().flush();
		} else {
			CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
			cookieSyncMngr.startSync();
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.removeAllCookie();
			cookieManager.removeSessionCookie();
			cookieSyncMngr.stopSync();
			cookieSyncMngr.sync();
		}
	}


	public static void registerDBListener(Context context, DB.DBListener dbListener) {
		DB db = DB.getInstance(context);
		db.addListener(dbListener);
	}

	public static void unRegisterDbListener(DB.DBListener dbListener, Context context) {
		DB db = DB.getInstance(context);
		db.remoceListener(dbListener);
	}
}
