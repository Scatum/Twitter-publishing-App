package com.example.hovsep.twitterpublish.application;

import android.app.Application;
import android.util.Log;

import com.example.hovsep.twitterpublish.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TWApplication extends Application {


	@Override
	public void onCreate() {
		TwitterConfig config = new TwitterConfig.Builder(this)
				.logger(new DefaultLogger(Log.DEBUG))
				.twitterAuthConfig(new TwitterAuthConfig("IyEy0Z6yAlT0BQ6xvHuSHJj7j", "DlIHi1RV9CwO8URCQxWgyUlKO3cnBkgRfwgqV6QFJrKs88dqub"))
				.debug(true)
				.build();
		Twitter.initialize(config);
		super.onCreate();

		CaocConfig.Builder.create()
				.backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
				.enabled(true) //default: true
				.showErrorDetails(true) //default: true
				.showRestartButton(false) //default: true
				.logErrorOnRestart(true) //default: true
				.trackActivities(true) //default: false
				.minTimeBetweenCrashesMs(2000) //default: 3000
				.errorDrawable(R.drawable.attach) //default: bug image
				.apply();
	}
}
