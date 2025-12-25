package com.example.lukedict;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class WordDescActivity extends AppCompatActivity {
    TextView titleTv1,descTv,phoneticTv,tranTv;
    Button audioBtn;
    private MediaPlayer mediaPlayer;
    private String audioUrl;
    private String localAudioPath;
    private DownloadAudioTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_desc);
        showactionbar();
        initView();
        //接受上一界面传来的数据
        Intent intent = getIntent();
        WordBean wordBean = (WordBean) intent.getSerializableExtra("word");
        //设置显示控件
        titleTv1.setText(wordBean.getTitle());
        phoneticTv.setText(TextUtils.isEmpty(wordBean.getPhonetic()) ? "" : wordBean.getPhonetic());
        tranTv.setText(TextUtils.isEmpty(wordBean.getTran()) ? "暂无中文释义" : wordBean.getTran());
        audioUrl = wordBean.getAudioUrl();
        if (!TextUtils.isEmpty(audioUrl)) {
            audioBtn.setVisibility(View.VISIBLE);
            audioBtn.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(localAudioPath)) {
                    playAudio(localAudioPath);
                } else {
                    downloadAudio();
                }
            });
        } else {
            audioBtn.setVisibility(View.GONE);
        }
        descTv.setText(wordBean.getDesc());
    }

    private void initView() {
        titleTv1 = findViewById(R.id.worddesc_tv_title);
        phoneticTv = findViewById(R.id.worddesc_tv_phonetic);
        tranTv = findViewById(R.id.worddesc_tv_tran);
        descTv = findViewById(R.id.worddesc_tv_desc);
        audioBtn = findViewById(R.id.worddesc_btn_audio);
    }

    private void showactionbar() {
        ActionBar actionBar = this.getSupportActionBar();//定义actionbar上的返回箭头
        actionBar.setTitle("单词搜索");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {//定义actionbar上的返回箭头
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadAudio() {
        if (TextUtils.isEmpty(audioUrl)) {
            Toast.makeText(this, "暂无音频资源", Toast.LENGTH_SHORT).show();
            return;
        }
        if (downloadTask != null) {
            downloadTask.cancel(true);
        }
        downloadTask = new DownloadAudioTask();
        downloadTask.execute(audioUrl);
    }

    private void playAudio(String path) {
        if (TextUtils.isEmpty(path)) return;
        releasePlayer();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.setAudioAttributes(
                    new android.media.AudioAttributes.Builder()
                            .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()
            );
            mediaPlayer.setVolume(1.0f, 1.0f); // 提升音量到最大
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(mp -> {});
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(this, "音频播放失败", Toast.LENGTH_SHORT).show();
                return true;
            });
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Toast.makeText(this, "音频播放失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (downloadTask != null) {
            downloadTask.cancel(true);
        }
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private class DownloadAudioTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WordDescActivity.this, "正在加载音频…", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            OkHttpClient client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) return null;
                ResponseBody body = response.body();
                if (body == null) return null;
                File outFile = File.createTempFile("audio_", ".mp3", getCacheDir());
                try (FileOutputStream fos = new FileOutputStream(outFile)) {
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = body.byteStream().read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                }
                return outFile.getAbsolutePath();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            if (isCancelled()) return;
            if (TextUtils.isEmpty(path)) {
                Toast.makeText(WordDescActivity.this, "音频加载失败", Toast.LENGTH_SHORT).show();
            } else {
                localAudioPath = path;
                playAudio(path);
            }
        }
    }
}