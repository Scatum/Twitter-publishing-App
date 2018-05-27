package com.example.hovsep.twitterpublish.model;

import android.net.Uri;

public class MyTweet {
	private int id;
	private String text;
	private String date;
	private boolean isPublished;
	private String path;

	public MyTweet(int id, String text, String date, boolean isPublished, String path) {
		this.id = id;
		this.text = text;
		this.date = date;
		this.isPublished = isPublished;
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isPublished() {
		return isPublished;
	}

	public void setPublished(boolean published) {
		isPublished = published;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
