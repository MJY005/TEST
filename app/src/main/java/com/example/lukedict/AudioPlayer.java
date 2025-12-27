package com.example.lukedict;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * 音频播放工具类
 */
public class AudioPlayer {
    private static final String TAG = "AudioPlayer";
    private static MediaPlayer mediaPlayer;

    /**
     * 播放音频（从URL）
     * @param url 音频URL
     */
    public static void playAudio(String url) {
        if (url == null || url.isEmpty()) {
            Log.w(TAG, "音频URL为空");
            return;
        }

        try {
            // 如果正在播放，先停止并释放
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(mp -> {
                release();
            });
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "播放失败: " + what + ", extra: " + extra);
                release();
                return false;
            });
        } catch (IOException e) {
            Log.e(TAG, "初始化失败: " + e.getMessage());
            release();
        }
    }

    /**
     * 释放MediaPlayer资源
     */
    public static void release() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                Log.e(TAG, "释放MediaPlayer失败: " + e.getMessage());
            }
            mediaPlayer = null;
        }
    }

    /**
     * 停止播放
     */
    public static void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}

