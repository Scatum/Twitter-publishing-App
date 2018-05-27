package com.example.hovsep.twitterpublish.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.example.hovsep.twitterpublish.DAO.DB;
import com.example.hovsep.twitterpublish.model.MyTweet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.hovsep.twitterpublish.constant.TWConstant.REQUEST_CAMERA;
import static com.example.hovsep.twitterpublish.constant.TWConstant.SELECT_FILE;

public class Utility {
	public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
	String userChoosenTask;


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static boolean checkPermission(final Context context) {
		int currentAPIVersion = Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
					|| ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

				String requiredPermission = "External storage permission is necessary";
				if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
					requiredPermission = "Camera permission is necessary";
				}
				boolean shouldShowDialog = ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE) ||
						ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA);

				if (shouldShowDialog) {
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
					alertBuilder.setCancelable(true);
					alertBuilder.setTitle("Permission necessary");
					alertBuilder.setMessage(requiredPermission);
					alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
						public void onClick(DialogInterface dialog, int which) {
							ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
						}
					});
					AlertDialog alert = alertBuilder.create();
					alert.show();
				} else {
					ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
				}
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	public static Bitmap onCaptureImageResult(Intent data) {
		Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

		File destination = new File(Environment.getExternalStorageDirectory(),
				System.currentTimeMillis() + ".jpg");

		FileOutputStream fo;
		try {
			destination.createNewFile();
			fo = new FileOutputStream(destination);
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//ivImage.setImageBitmap(thumbnail);
		return thumbnail;
	}

	public static Bitmap onSelectFromGalleryResult(Intent data, Context context) {

		Bitmap bm = null;
		if (data != null) {
			try {
				bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//ivImage.setImageBitmap(bm);

		return bm;
	}

	public static void selectImage(Activity activity, Fragment fragment) {
		final CharSequence[] items = {"Take Photo", "Choose from Library",
				"Cancel"};

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				boolean result = Utility.checkPermission(activity);

				if (items[item].equals("Take Photo")) {
					//	userChoosenTask = "Take Photo";
					if (result)
						cameraIntent(fragment);

				} else if (items[item].equals("Choose from Library")) {
					//	userChoosenTask = "Choose from Library";
					if (result)
						galleryIntent(fragment);

				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}


	public static String saveToInternalStorage(Bitmap bitmapImage, Context context) {
		if (bitmapImage == null) return null;
		ContextWrapper cw = new ContextWrapper(context);
		File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
		File mypath = new File(directory, System.currentTimeMillis() + ".jpg");

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(mypath);
			// Use the compress method on the BitMap object to write image to the OutputStream
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mypath.getPath();
	}

	private static void galleryIntent(Fragment fragment) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);//
		fragment.startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
	}

	private static void cameraIntent(Fragment fragment) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fragment.startActivityForResult(intent, REQUEST_CAMERA);
	}

	public static Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	public static void saveCurrentTweet(MyTweet myTweet, Context context) {
		DB db = DB.getInstance(context);
		db.addTweet(myTweet);
	}

	public static boolean isConnected(Context context) {
		ConnectivityManager
				cm = (ConnectivityManager) (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
	}
}