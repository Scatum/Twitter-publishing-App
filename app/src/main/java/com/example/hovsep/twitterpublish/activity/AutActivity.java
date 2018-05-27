package com.example.hovsep.twitterpublish.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.hovsep.twitterpublish.R;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class AutActivity extends BaseActivity {
	TwitterLoginButton loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aut);


		loginButton = findViewById(R.id.login_button);
		loginButton.setCallback(new Callback<TwitterSession>() {
			@Override
			public void success(Result<TwitterSession> result) {
				if (result != null) {
					startActivity(new Intent(getApplicationContext(), LandingActivity.class));
					finish();
				}

			}

			@Override
			public void failure(TwitterException exception) { }
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loginButton.onActivityResult(requestCode, resultCode, data);
	}
}
