package com.example.hovsep.twitterpublish.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {
	AlertDialog.Builder builder;
	AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		builder = new AlertDialog.Builder(this);
		builder.setMessage("Network is not Available")
				.setCancelable(false)
				.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						BaseActivity.this.finish();
					}
				})
				.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
						startActivity(intent);
					}
				});
	}


	@Override
	protected void onResume() {
		super.onResume();
		registerNetworkCheckReceiver();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(networkStatusBroadcastReceiver);
	}

	private void registerNetworkCheckReceiver() {
		IntentFilter networkStatusFilter = new IntentFilter();
		networkStatusFilter.addAction("android.net.wifi.STATE_CHANGE");
		networkStatusFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(networkStatusBroadcastReceiver, networkStatusFilter);
	}

	private BroadcastReceiver networkStatusBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			final ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			final android.net.NetworkInfo wifi = connMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			final android.net.NetworkInfo mobile = connMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifi.isAvailable() || mobile.isAvailable()) {
				onNetworkConnected();
			} else {
				onNetworkDisconnected();
			}

		}
	};

	private void onNetworkConnected() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	private void onNetworkDisconnected() {
		if (dialog == null) {
			dialog = builder.show();
		} else {
			dialog.show();
		}
	}

}
