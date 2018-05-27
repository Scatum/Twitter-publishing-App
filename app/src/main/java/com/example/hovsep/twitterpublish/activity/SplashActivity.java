package com.example.hovsep.twitterpublish.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.hovsep.twitterpublish.R;
import com.example.hovsep.twitterpublish.util.Utility;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_splash);
		isSigned();
	}

	private boolean isSigned() {
		Intent flowIntnet;
		TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
		if (session != null) {
			flowIntnet = new Intent(this, LandingActivity.class);
		} else {
			flowIntnet = new Intent(this, AutActivity.class);
		}
		Handler handler = new Handler();

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (Utility.isConnected(getApplicationContext())) {
					startActivity(flowIntnet);
					finish();
				}


			}
		}, 3000);


		return session != null;
	}
}
