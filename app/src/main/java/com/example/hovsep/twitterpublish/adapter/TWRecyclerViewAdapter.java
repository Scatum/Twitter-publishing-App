package com.example.hovsep.twitterpublish.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.hovsep.twitterpublish.R;
import com.example.hovsep.twitterpublish.model.MyTweet;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TWRecyclerViewAdapter extends RecyclerView.Adapter<TWRecyclerViewAdapter.ViewHolder> {
	Context mContext;
	List<MyTweet> myTweets = new ArrayList<>();

	public TWRecyclerViewAdapter(Context context) {
		mContext = context;
	}

	@Override
	public TWRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		View view = layoutInflater.inflate(R.layout.my_tweets_list_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final TWRecyclerViewAdapter.ViewHolder holder, final int position) {

		MyTweet myTweet = myTweets.get(position);

		holder.tweettextView.setText(myTweet.getText());

		if (!TextUtils.isEmpty(myTweet.getPath())) {
			holder.imageView.setVisibility(View.VISIBLE);
			Glide.with(mContext)
					.load(new File(myTweet.getPath()))
					.asBitmap()
					.placeholder(R.drawable.attach)
					.into(holder.imageView);
		} else {
			holder.imageView.setVisibility(View.GONE);
		}

		if (myTweet.isPublished()) {
			holder.published.setColorFilter(Color.GREEN);
		} else {
			holder.published.setColorFilter(mContext.getResources().getColor(R.color.tw__composer_deep_gray));
		}
		holder.date.setText(getDate(Long.parseLong(myTweet.getDate())));
	}


	private String getDate(long milliseconds) {
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(milliseconds);  //here your time in miliseconds
		String date = "" + cl.get(Calendar.DAY_OF_MONTH) + ":" + cl.get(Calendar.MONTH) + ":" + cl.get(Calendar.YEAR);
		String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);
		return date;
	}

	@Override
	public int getItemCount() {
		return myTweets.size();
	}


	public static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView tweettextView;
		private TextView date;
		private ImageView imageView;
		private ImageView published;

		public ViewHolder(View itemView) {
			super(itemView);
			tweettextView = itemView.findViewById(R.id.postText);
			date = itemView.findViewById(R.id.date);
			imageView = itemView.findViewById(R.id.twiitImage);
			published = itemView.findViewById(R.id.published);
		}
	}


	public void swapData(List<MyTweet> myTweets) {
		this.myTweets.clear();
		this.myTweets.addAll(myTweets);
		notifyDataSetChanged();
	}

	public void updateSingleItem(MyTweet myTweet) {
		for (int i = 0; i < myTweets.size(); i++) {
			if (myTweets.get(i).getId() == myTweet.getId()) {
				myTweets.set(i, myTweet);
				notifyItemRangeChanged(i, 1);
				Log.e("Rogus","update " + myTweet.getText());

				break;
			}
		}
	}

	public void addNewItem(MyTweet myTweet) {
		myTweets.add(0, myTweet);
		Log.e("Rogus","Ssssss " + myTweet.getText());
		notifyDataSetChanged();
		//notifyItemChanged(0);
	}


}