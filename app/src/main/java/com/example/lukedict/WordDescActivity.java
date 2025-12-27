package com.example.lukedict;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class WordDescActivity extends AppCompatActivity {
    TextView titleTv1, descTv, phoneticTv, tranTv, noDescTv, detailTitleTv;
    TextView exampleTv, exampleLabelTv;
    android.widget.Button ukAudioBtn, usAudioBtn;
    ImageButton exampleAudioBtn;
    android.view.ViewGroup detailsLayout;
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
        if (wordBean != null) {
            displayWordInfo(wordBean);
        }
    }

    private void initView() {
        titleTv1 = findViewById(R.id.worddesc_tv_title);
        phoneticTv = findViewById(R.id.worddesc_tv_phonetic);
        tranTv = findViewById(R.id.worddesc_tv_tran);
        descTv = findViewById(R.id.worddesc_tv_desc);
        noDescTv = findViewById(R.id.worddesc_tv_no_desc);
        detailTitleTv = findViewById(R.id.worddesc_tv_detail_title);
        detailsLayout = findViewById(R.id.worddesc_ll_details);
        exampleTv = findViewById(R.id.worddesc_tv_example);
        exampleLabelTv = findViewById(R.id.worddesc_tv_example_label);
        ukAudioBtn = findViewById(R.id.worddesc_btn_uk_audio);
        usAudioBtn = findViewById(R.id.worddesc_btn_us_audio);
        exampleAudioBtn = findViewById(R.id.worddesc_btn_example_audio);
    }

    /**
     * 显示单词信息，优化显示格式
     */
    private void displayWordInfo(WordBean wordBean) {
        // 设置单词标题（居中）
        String title = wordBean.getTitle();
        if (!TextUtils.isEmpty(title)) {
            titleTv1.setText(title);
            titleTv1.setGravity(android.view.Gravity.CENTER);
        }

        // 设置音标（优先使用新的英式/美式音标）
        String phoneticText = "";
        String ukPhonetic = wordBean.getUkPhonetic();
        String usPhonetic = wordBean.getUsPhonetic();
        
        // 检查是否是默认的"暂无"提示
        boolean hasUkPhonetic = !TextUtils.isEmpty(ukPhonetic) && !ukPhonetic.equals("暂无英音音标");
        boolean hasUsPhonetic = !TextUtils.isEmpty(usPhonetic) && !usPhonetic.equals("暂无美音音标");
        
        if (hasUkPhonetic || hasUsPhonetic) {
            String uk = hasUkPhonetic ? ukPhonetic : "暂无英音音标";
            String us = hasUsPhonetic ? usPhonetic : "暂无美音音标";
            phoneticText = "英 " + uk + "  美 " + us;
        } else if (!TextUtils.isEmpty(wordBean.getPhonetic())) {
            phoneticText = wordBean.getPhonetic();
        } else {
            // 如果都没有，显示默认提示
            phoneticText = "英 暂无英音音标  美 暂无美音音标";
        }
        
        phoneticTv.setText(phoneticText);
        phoneticTv.setVisibility(View.VISIBLE);
        phoneticTv.setGravity(android.view.Gravity.CENTER);

        // 设置中文释义
        String tran = wordBean.getTran();
        if (!TextUtils.isEmpty(tran)) {
            tranTv.setText(tran);
            tranTv.setVisibility(View.VISIBLE);
        } else {
            tranTv.setText("暂无中文释义");
            tranTv.setVisibility(View.VISIBLE);
        }

        // 设置英音/美音按钮
        String ukAudio = wordBean.getUkAudio();
        String usAudio = wordBean.getUsAudio();
        
        if (!TextUtils.isEmpty(ukAudio) && !ukAudio.startsWith("http://") && !ukAudio.startsWith("https://")) {
            // 如果audio不是有效URL，忽略
            ukAudio = null;
        }
        if (!TextUtils.isEmpty(usAudio) && !usAudio.startsWith("http://") && !usAudio.startsWith("https://")) {
            usAudio = null;
        }
        
        if (!TextUtils.isEmpty(ukAudio)) {
            ukAudioBtn.setVisibility(View.VISIBLE);
            String finalUkAudio = ukAudio;
            ukAudioBtn.setOnClickListener(v -> AudioPlayer.playAudio(finalUkAudio));
        } else {
            ukAudioBtn.setVisibility(View.GONE);
        }
        
        if (!TextUtils.isEmpty(usAudio)) {
            usAudioBtn.setVisibility(View.VISIBLE);
            String finalUsAudio = usAudio;
            usAudioBtn.setOnClickListener(v -> AudioPlayer.playAudio(finalUsAudio));
        } else {
            usAudioBtn.setVisibility(View.GONE);
        }
        
        // 兼容旧的audioUrl字段（如果没有英音/美音，使用旧的audioUrl作为英音）
        audioUrl = wordBean.getAudioUrl();
        if (!TextUtils.isEmpty(audioUrl) && TextUtils.isEmpty(wordBean.getUkAudio()) && TextUtils.isEmpty(wordBean.getUsAudio())) {
            // 使用旧的audioUrl作为英音
            ukAudioBtn.setVisibility(View.VISIBLE);
            ukAudioBtn.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(localAudioPath)) {
                    playAudio(localAudioPath);
                } else {
                    downloadAudio();
                }
            });
        }
        
        // 显示词性释义列表
        if (wordBean.getDetails() != null && !wordBean.getDetails().isEmpty()) {
            detailsLayout.setVisibility(View.VISIBLE);
            detailTitleTv.setVisibility(View.VISIBLE);
            detailsLayout.removeAllViews();
            
            for (WordBean.WordDetail detail : wordBean.getDetails()) {
                TextView tv = new TextView(this);
                String text = "";
                if (!TextUtils.isEmpty(detail.getPartOfSpeech())) {
                    text = detail.getPartOfSpeech() + "：";
                }
                text += detail.getMeaning();
                tv.setText(text);
                tv.setTextSize(16);
                tv.setTextColor(ContextCompat.getColor(this, android.R.color.black));
                tv.setPadding(0, 8, 0, 8);
                detailsLayout.addView(tv);
            }
        } else {
            detailsLayout.setVisibility(View.GONE);
        }
        
        // 显示例句
        if (!TextUtils.isEmpty(wordBean.getExample())) {
            exampleTv.setText(wordBean.getExample());
            exampleLabelTv.setVisibility(View.VISIBLE);
            View exampleParent = (View) exampleTv.getParent();
            if (exampleParent != null) {
                exampleParent.setVisibility(View.VISIBLE);
            }
            
            if (!TextUtils.isEmpty(wordBean.getExampleAudio())) {
                exampleAudioBtn.setVisibility(View.VISIBLE);
                exampleAudioBtn.setOnClickListener(v -> AudioPlayer.playAudio(wordBean.getExampleAudio()));
            } else {
                exampleAudioBtn.setVisibility(View.GONE);
            }
        } else {
            exampleLabelTv.setVisibility(View.GONE);
            View exampleParent = (View) exampleTv.getParent();
            if (exampleParent != null) {
                exampleParent.setVisibility(View.GONE);
            }
        }

        // 解析并格式化详细释义
        String desc = wordBean.getDesc();
        
        if (!TextUtils.isEmpty(desc)) {
            // 检查是否是简单的"翻译结果：xxx"格式
            boolean isSimpleTranslation = desc.startsWith("翻译结果：") || desc.startsWith("翻译结果:");
            
            if (isSimpleTranslation) {
                // 简单翻译结果，不显示"详细释义"标题，隐藏详细释义区域
                detailTitleTv.setVisibility(View.GONE);
                descTv.setVisibility(View.GONE);
                noDescTv.setVisibility(View.GONE);
            } else {
                // 有详细的词典释义，格式化显示
                SpannableStringBuilder formattedDesc = formatWordDescription(desc);
                descTv.setText(formattedDesc);
                descTv.setVisibility(View.VISIBLE);
                noDescTv.setVisibility(View.GONE);
                detailTitleTv.setVisibility(View.VISIBLE);
                
            }
        } else {
            descTv.setVisibility(View.GONE);
            noDescTv.setVisibility(View.VISIBLE);
            detailTitleTv.setVisibility(View.GONE);
        }
    }

    /**
     * 格式化单词详细释义，使其更美观
     */
    private SpannableStringBuilder formatWordDescription(String desc) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        
        // 如果描述为空，直接返回
        if (TextUtils.isEmpty(desc)) {
            return builder;
        }
        
        // 词性颜色映射
        int partOfSpeechColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
        int definitionColor = ContextCompat.getColor(this, android.R.color.black);
        int exampleColor = ContextCompat.getColor(this, android.R.color.darker_gray);
        int simpleTextColor = ContextCompat.getColor(this, android.R.color.darker_gray);
        
        // 处理简单的"翻译结果：xxx"格式
        if (desc.startsWith("翻译结果：") || desc.startsWith("翻译结果:")) {
            String content = desc.contains("：") ? desc.substring(desc.indexOf("：") + 1) : 
                           desc.contains(":") ? desc.substring(desc.indexOf(":") + 1) : desc;
            SpannableString simpleText = new SpannableString(content.trim());
            simpleText.setSpan(new ForegroundColorSpan(simpleTextColor), 0, simpleText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(simpleText);
            return builder;
        }
        
        // 处理标准格式（词性、定义、例句）
        String[] lines = desc.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            if (TextUtils.isEmpty(line)) {
                builder.append("\n");
                continue;
            }
            
            // 检测词性行（如 "noun："、"verb："）
            if (line.matches("^[a-z]+[：:]$")) {
                // 词性标题
                String pos = line.replaceAll("[：:]", "");
                SpannableString partOfSpeech = new SpannableString(pos.toUpperCase() + "\n");
                partOfSpeech.setSpan(new ForegroundColorSpan(partOfSpeechColor), 0, partOfSpeech.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                partOfSpeech.setSpan(new StyleSpan(Typeface.BOLD), 0, partOfSpeech.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(partOfSpeech);
            } 
            // 检测定义行（以 "- " 开头）
            else if (line.startsWith("- ")) {
                String definition = line.substring(2);
                SpannableString def = new SpannableString("  • " + definition + "\n");
                def.setSpan(new ForegroundColorSpan(definitionColor), 0, def.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(def);
            }
            // 检测例句行（以 "  例：" 或 "  例:" 开头）
            else if (line.startsWith("  例：") || line.startsWith("  例:")) {
                String example = line.substring(3).trim();
                SpannableString ex = new SpannableString("     " + example + "\n");
                ex.setSpan(new ForegroundColorSpan(exampleColor), 0, ex.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ex.setSpan(new StyleSpan(Typeface.ITALIC), 0, ex.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(ex);
            }
            // 其他内容（普通文本）
            else {
                SpannableString normalText = new SpannableString(line + "\n");
                normalText.setSpan(new ForegroundColorSpan(definitionColor), 0, normalText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(normalText);
            }
        }
        
        return builder;
    }

    /**
     * 提取第一个词性
     */
    private String extractFirstPartOfSpeech(String desc) {
        if (TextUtils.isEmpty(desc)) return "";
        Pattern pattern = Pattern.compile("^([a-z]+)：", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(desc);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private void showactionbar() {
        ActionBar actionBar = this.getSupportActionBar();//定义actionbar上的返回箭头
        actionBar.setTitle("单词详情");
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
        AudioPlayer.release(); // 释放AudioPlayer资源
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
