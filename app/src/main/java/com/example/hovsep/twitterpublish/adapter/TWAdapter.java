package com.example.hovsep.twitterpublish.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class TWAdapter extends FragmentStatePagerAdapter {
	List<Fragment> fragments;
	Fragment fragment;

	public TWAdapter(List<Fragment> fragments, FragmentManager fm) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}


	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}