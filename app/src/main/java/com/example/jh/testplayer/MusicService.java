package com.example.jh.testplayer;

import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service implements Runnable,
		OnCompletionListener {

	private static final String TAG = "MusicService";
	boolean isRun = true;

	// 控制音频、视频的播放
	MediaPlayer player;

	int songIndex = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		player = new MediaPlayer();
		initSong();

		isRun = true;
		new Thread(this).start();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		String control = intent.getStringExtra("control");
		switch (control) {
			case "start":
				// 播放
				player.start();
				break;
			case "pause":
				// 暂停
				player.pause();
				break;
			case "update":
				// 更新音乐播放位置
				player.seekTo(intent.getIntExtra("progress", -1));
				// 恢复播放
				player.start();
				break;
			case "next":
				nextSong();
				player.start();
				break;
			case "pre":
				preSong();
				player.start();
				break;
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isRun = false;
		// 停止音乐
		player.stop();
		// 释放音乐资源
		player.release();
	}

	@Override
	public void run() {
		Intent intent = new Intent("update music time");
		while (isRun) {
			// 获取当前正在播放的时间
			int current = player.getCurrentPosition();

			if (player.isPlaying()) {
				// 发送广播
				intent.putExtra("current", current);
				intent.putExtra("duration", player.getDuration());
				sendBroadcast(intent);
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Toast.makeText(this, "音乐播放完毕", Toast.LENGTH_SHORT).show();

		nextSong();
		player.start();
	}

	/**
	 * 下一首
	 */
	private void nextSong() {
		songIndex = (songIndex + 1) % AllMusicInfo.musicList.size();
		Log.e(TAG, "AllMusicInfo.musicList.size() = " + AllMusicInfo.musicList.size());
		initSong();
	}

	private void preSong() {
		songIndex--;
		if(songIndex < 0){
			songIndex = AllMusicInfo.musicList.size()-1;
		}

		initSong();
	}

	private void initSong() {
		try {
			player.reset();
			// 从sdcard上载入音频文件
//			String path = AllMusicInfo.musicList.get(songIndex).getPath();
//			Log.e(TAG, "path = " + path);
			String url = "https://lcadream.oss-cn-shanghai.aliyuncs.com/yinpin/%E4%B8%83%E8%99%9E.mp3";

			// E/MusicService: path = /storage/emulated/0/netease/cloudmusic/Music/Angela Ammons - Always Getting Over You.mp3
			player.setDataSource(url);

			player.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 给音乐播放器添加监听，监听音乐是否播放完毕
		player.setOnCompletionListener(this);
	}
}
