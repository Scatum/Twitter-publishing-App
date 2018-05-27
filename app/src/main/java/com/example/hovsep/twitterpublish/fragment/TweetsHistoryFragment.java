package com.example.hovsep.twitterpublish.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hovsep.twitterpublish.DAO.DB;
import com.example.hovsep.twitterpublish.R;
import com.example.hovsep.twitterpublish.adapter.TWAdapter;
import com.example.hovsep.twitterpublish.adapter.TWRecyclerViewAdapter;
import com.example.hovsep.twitterpublish.manager.TWManager;
import com.example.hovsep.twitterpublish.model.MyTweet;

import java.util.List;

public class TweetsHistoryFragment extends Fragment implements View.OnClickListener {

	TextView logoutButton;
	TextView noTweetsYet;
	Toolbar toolbar;
	RecyclerView recyclerView;
	TWRecyclerViewAdapter adapter;
	DB.DBListener dbListener;

	public TweetsHistoryFragment() {
		// Required empty public constructor
	}

	public static TweetsHistoryFragment newInstance() {
		TweetsHistoryFragment fragment = new TweetsHistoryFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		dbListener = new DB.DBListener() {
			@Override
			public void onNewItemAdded(MyTweet myTweet) {
				noTweetsYet.setVisibility(View.GONE);
				adapter.addNewItem(myTweet);
			}

			@Override
			public void onItemUpdated(MyTweet myTweet) {
				noTweetsYet.setVisibility(View.GONE);
				adapter.updateSingleItem(myTweet);

			}
		};
		TWManager.registerDBListener(getContext(), dbListener);
		return inflater.inflate(R.layout.fragment_my_tweets, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		logoutButton = view.findViewById(R.id.logoutButton);
		noTweetsYet = view.findViewById(R.id.noTweetsYet);
		logoutButton.setOnClickListener(this);
		toolbar = view.findViewById(R.id.toolbar);
		toolbar.setTitle(TWManager.getTwitterSession().getUserName());
		toolbar.setTitleTextColor(getContext().getResources().getColor(R.color.tw__composer_blue_text));
		recyclerView = view.findViewById(R.id.recyclerView);
		adapter = new TWRecyclerViewAdapter(getContext());
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		TWManager.getTweetsHistory(getContext(), new DBListener() {
			@Override
			public void onAllItemsRecycle(List<MyTweet> myTweets) {
				if (myTweets.size() == 0) {
					noTweetsYet.setVisibility(View.VISIBLE);
				} else {
					adapter.swapData(myTweets);
				}
			}
		});

	}

	@Override
	public void onDestroyView() {
		TWManager.unRegisterDbListener(dbListener, getContext());
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.logoutButton:
				TWManager.logoutTwitter(getActivity());
		}
	}

	public interface DBListener {
		void onAllItemsRecycle(List<MyTweet> myTweets);
	}
}
