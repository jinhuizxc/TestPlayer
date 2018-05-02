package com.example.jh.testplayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,
		OnSeekBarChangeListener {

	SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
	TextView tv;
	TextView tvDuration;

	SeekBar seekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		registerReceiver(receiver, new IntentFilter("update music time"));

		// 查多媒体数据库中的音乐文件的名字，路径，时长
		new AllMusicInfo(this).queryMediaStore();

		initWidget();
	}

	private void initWidget() {
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);
		findViewById(R.id.button4).setOnClickListener(this);

		tv = (TextView) findViewById(R.id.textView1);
		tvDuration = (TextView) findViewById(R.id.textView2);

		seekBar = (SeekBar) findViewById(R.id.seekBar1);

		// 添加监听
		seekBar.setOnSeekBarChangeListener(this);

		tvDuration.setText(sdf.format(AllMusicInfo.musicList.get(0).getDuration()));

		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		for (int i = 0; i < AllMusicInfo.musicList.size(); i++) {
			TextView tv = new TextView(this);
			tv.setTextSize(30);
			tv.setText(AllMusicInfo.musicList.get(i).getName());

			ll.addView(tv);
		}
	}


	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			int current = intent.getIntExtra("current", -1);
			int duration = intent.getIntExtra("duration", -1);
			tv.setText(sdf.format(current));
			tvDuration.setText(sdf.format(duration));

			// 更新音乐进度条
			seekBar.setMax(duration);
			seekBar.setProgress(current);
		}
	};

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			Intent intent = new Intent(this, MusicService.class);
			intent.putExtra("control", "start");
			startService(intent);
		} else if (v.getId() == R.id.button2) {
			Intent intent = new Intent(this, MusicService.class);
			intent.putExtra("control", "pause");
			startService(intent);
		} else if (v.getId() == R.id.button3) {
			Intent intent = new Intent(this, MusicService.class);
			intent.putExtra("control", "next");
			startService(intent);
		} else if (v.getId() == R.id.button4) {
			Intent intent = new Intent(this, MusicService.class);
			intent.putExtra("control", "pre");
			startService(intent);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.addSubMenu("退出");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 关闭Activity
		finish();
		// 关闭Service
		stopService(new Intent(this, MusicService.class));

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
								  boolean fromUser) {
		tv.setText(sdf.format(progress));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// 暂停音乐
		Intent intent = new Intent(this, MusicService.class);
		intent.putExtra("control", "pause");
		startService(intent);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// 更新的音乐进度
		int progress = seekBar.getProgress();
		Intent intent = new Intent(this, MusicService.class);
		intent.putExtra("control", "update");
		intent.putExtra("progress", progress);
		startService(intent);
	}
}
