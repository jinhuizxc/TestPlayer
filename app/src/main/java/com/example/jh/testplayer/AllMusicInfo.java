package com.example.jh.testplayer;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class AllMusicInfo {
	
	static ArrayList<MusicInfo> musicList = new ArrayList<MusicInfo>(); 
	Context context;

	public AllMusicInfo(Context context) {
		this.context = context;
	}
	
	
	public void queryMediaStore() {
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

		ContentResolver resolver = context.getContentResolver();

		String selection = MediaStore.Audio.Media.IS_MUSIC + "=1 and "
				+ MediaStore.Audio.Media.DURATION + ">20000";

		Cursor cursor = resolver.query(uri, new String[] {
				MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DURATION }, selection, null,
				MediaStore.Audio.Media.TITLE);
		
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
			String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
			long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
			
			Log.d("Test", title);
			Log.d("Test", path);
			Log.d("Test", "duration = " + duration);
			
			MusicInfo music = new MusicInfo(title, path, duration);
			musicList.add(music);
			
		}

	}
}
