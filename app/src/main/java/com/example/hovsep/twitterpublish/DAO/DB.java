package com.example.hovsep.twitterpublish.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.hovsep.twitterpublish.fragment.TweetsHistoryFragment;
import com.example.hovsep.twitterpublish.model.MyTweet;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "MY_TWEETS";
	private static final String TABLE_MY_TWEETS = "myTweets";
	private static final String KEY_ID = "id";
	private static final String KEY_TEXT = "text";
	private static final String KEY_IS_PUBLISHED = "is_published";
	private static final String KEY_DATE = "date";
	private static final String KEY_IMAGE_PATH = "path";
	private static DB mInstance;

	List<DBListener> dbListeners = new ArrayList<>();

	private DB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	private Context mCxt;

	public static synchronized DB getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new DB(ctx.getApplicationContext());
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MY_TWEETS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_TEXT + " TEXT,"
				+ KEY_DATE + " TEXT,"
				+ KEY_IS_PUBLISHED + " INTEGER,"
				+ KEY_IMAGE_PATH + " TEXT"
				+ ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_TWEETS);
		onCreate(db);
	}

	public void addTweet(MyTweet myTweet) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, myTweet.getId());
		values.put(KEY_TEXT, myTweet.getText());
		values.put(KEY_DATE, myTweet.getDate());
		values.put(KEY_IMAGE_PATH, myTweet.getPath());
		values.put(KEY_IS_PUBLISHED, myTweet.isPublished());
		db.insert(TABLE_MY_TWEETS, null, values);
		db.close();
		onNewItemAdded(myTweet);
	}

	@SuppressLint("StaticFieldLeak")
	public void getAllTweets(TweetsHistoryFragment.DBListener dbListener) {
		List<MyTweet> myTweets = new ArrayList<>();
		// Select All Query
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				String selectQuery = "SELECT  * FROM " + TABLE_MY_TWEETS + " ORDER BY id DESC";

				SQLiteDatabase db = DB.this.getWritableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list
				if (!cursor.isClosed() && cursor.moveToFirst()) {
					do {
						int id = Integer.parseInt(cursor.getString(0));
						String text = cursor.getString(1);
						String date = cursor.getString(2);
						boolean isPublished = cursor.getInt(3) == 1;
						String path = cursor.getString(4);
						MyTweet myTweet = new MyTweet(id, text, date, isPublished, path);
						myTweets.add(myTweet);
					} while (cursor.moveToNext());
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);
				if (dbListener != null) {
					dbListener.onAllItemsRecycle(myTweets);
				}
			}
		}.execute();

	}

	public int updateMyTweets(MyTweet myTweet) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, myTweet.getId());
		values.put(KEY_IS_PUBLISHED, myTweet.isPublished());
		onItemUpdated(myTweet);
		// updating row
		return db.update(TABLE_MY_TWEETS, values, KEY_ID + " = ?",
				new String[]{String.valueOf(myTweet.getId())});
	}

	public MyTweet getLastInsertedItem() {
		String countQuery = "SELECT * FROM " + TABLE_MY_TWEETS + " WHERE id = (SELECT MAX(id)  FROM " + TABLE_MY_TWEETS + ")";
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(countQuery, null);
		if (cursor != null)
			cursor.moveToFirst();

		int id = Integer.parseInt(cursor.getString(0));
		String text = cursor.getString(1);
		String date = cursor.getString(2);
		boolean isPublished = cursor.getInt(3) == 1;
		String path = cursor.getString(4);
		MyTweet myTweet = new MyTweet(id, text, date, isPublished, path);
		return myTweet;
	}

	private void onItemUpdated(MyTweet myTweet) {
		for (DBListener dbListener : dbListeners) {
			dbListener.onItemUpdated(myTweet);
		}
	}

	private void onNewItemAdded(MyTweet myTweet) {
		for (DBListener dbListener : dbListeners) {
			dbListener.onNewItemAdded(myTweet);
		}
	}

	public void addListener(DBListener dbListener) {
		dbListeners.add(dbListener);
	}

	public void remoceListener(DBListener dbListener) {
		for (int i = 0; i < dbListeners.size(); i++) {
			if (dbListeners.get(i).equals(dbListener)) {
				dbListeners.remove(dbListener);
				break;
			}
		}
	}

	public interface DBListener {
		void onNewItemAdded(MyTweet myTweet);

		void onItemUpdated(MyTweet myTweet);
	}
}
