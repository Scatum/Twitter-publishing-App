package com.example.hovsep.twitterpublish.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.hovsep.twitterpublish.R;
import com.example.hovsep.twitterpublish.adapter.TWAdapter;
import com.example.hovsep.twitterpublish.fragment.TweetsHistoryFragment;
import com.example.hovsep.twitterpublish.fragment.NewPostFragment;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends BaseActivity {
	ViewPager pager;
	TWAdapter adapter;
	List<Fragment> fragments = new ArrayList<>();
	TweetsHistoryFragment tweetsHistoryFragment;
	NewPostFragment newPostFragment;
	private BottomNavigationView navigation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pager = findViewById(R.id.pager);
		navigation = findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		initFragments();
		initViewPager();
		pager.setCurrentItem(0);
	}

	private void initFragments() {
		newPostFragment = NewPostFragment.newInstance();
		tweetsHistoryFragment = TweetsHistoryFragment.newInstance();
		fragments.add(tweetsHistoryFragment);
		fragments.add(newPostFragment);
	}

	private void initViewPager() {
		adapter = new TWAdapter(fragments, getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				navigation.getMenu().getItem(position).setChecked(true);
				if (position == 0) {
					((NewPostFragment)adapter.getItem(1)).resetData();
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}


	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_history:
					pager.setCurrentItem(0);
					return true;
				case R.id.navigation_new_tweet:
					pager.setCurrentItem(1);
					return true;

			}
			return false;
		}
	};

}
