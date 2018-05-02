package com.example.jh.testplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

/**
 * Created by jinhui on 2018/5/3.
 * Email:1004260403@qq.com
 */
public class TestActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    //    String url = "https://lcadream.oss-cn-shanghai.aliyuncs.com/yinpin/%E4%B8%83%E8%99%9E.mp3";
    String url = "https://lcadream.oss-cn-shanghai.aliyuncs.com/yinpin/%E5%85%AB%E9%BD%90.mp3";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mediaPlayer = new MediaPlayer();

//        try {
//            mediaPlayer.setDataSource(url);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 不能播放！是因为没有加联网权限，这怎么能忘了呢！
        Uri uri  = Uri.parse(url);
        try {
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(url);
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mediaPlayer.start();
//                }
//            });
//
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    if (mediaPlayer != null) {
//                        mediaPlayer.release();
//                        mediaPlayer = null;
//                    }
//                }
//            });
//            mediaPlayer.prepareAsync();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

}

