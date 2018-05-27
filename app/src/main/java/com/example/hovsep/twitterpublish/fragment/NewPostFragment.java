package com.example.hovsep.twitterpublish.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.hovsep.twitterpublish.DAO.DB;
import com.example.hovsep.twitterpublish.R;
import com.example.hovsep.twitterpublish.activity.LandingActivity;
import com.example.hovsep.twitterpublish.manager.TWManager;
import com.example.hovsep.twitterpublish.model.MyTweet;
import com.example.hovsep.twitterpublish.util.Utility;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;

import static com.example.hovsep.twitterpublish.constant.TWConstant.REQUEST_CAMERA;
import static com.example.hovsep.twitterpublish.constant.TWConstant.SELECT_FILE;

public class NewPostFragment extends Fragment implements View.OnClickListener {
	ImageView twiitImage;
	Uri imageUri;
	Bitmap bitmap;
	TextView twButton;
	EditText postText;
	android.support.v7.widget.Toolbar toolbar;

	LandingActivity activity;
	DB.DBListener dbListener;

	public NewPostFragment() {
		// Required empty public constructor
	}

	public static NewPostFragment newInstance() {
		NewPostFragment fragment = new NewPostFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbListener = new DB.DBListener() {
			@Override
			public void onNewItemAdded(MyTweet myTweet) {

			}

			@Override
			public void onItemUpdated(MyTweet myTweet) {

			}
		};

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_new_post, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (getActivity() != null && getActivity() instanceof LandingActivity) {
			activity = (LandingActivity) getActivity();
		}
		TWManager.registerDBListener(getActivity(), dbListener);
		twiitImage = view.findViewById(R.id.twiitImage);
		twiitImage.setOnClickListener(this);
		twButton = view.findViewById(R.id.twiitButton);
		twButton.setOnClickListener(this);
		postText = view.findViewById(R.id.postText);
		toolbar = view.findViewById(R.id.toolbar);
		toolbar.setTitleTextColor(Color.WHITE);
		TwitterSession session = TWManager.getTwitterSession();
		if (session != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				toolbar.setTitle(session.getUserName() + " What's happening");
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_FILE) {
				bitmap = Utility.onSelectFromGalleryResult(data, getActivity());

			} else if (requestCode == REQUEST_CAMERA) {
				bitmap = Utility.onCaptureImageResult(data);
			}
			twiitImage.setImageBitmap(bitmap);
			imageUri = Utility.getImageUri(getActivity(), bitmap);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.twiitImage:
				Utility.selectImage(getActivity(), NewPostFragment.this);
				break;
			case R.id.twiitButton:
				publish();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		dbListener = null;
	}

	private void publish() {
		if (!TextUtils.isEmpty(postText.getText())) {
			String path = Utility.saveToInternalStorage(bitmap, getActivity());
			TWManager.publish(getActivity(), imageUri, postText.getText().toString());
			MyTweet myTweet = new MyTweet((int) System.currentTimeMillis(), postText.getText().toString(),
					String.valueOf(System.currentTimeMillis()), false, path);
			Utility.saveCurrentTweet(myTweet, getContext());
			resetData();
		}
	}

	public void resetData() {
		postText.setText("");
		if (bitmap != null) {
			bitmap.recycle();
		}
		imageUri = null;
		twiitImage.setImageResource(R.drawable.pic_image);
	}
}
