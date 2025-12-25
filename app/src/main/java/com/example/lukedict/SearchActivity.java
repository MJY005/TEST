package com.example.lukedict;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEt;
    private ImageView searchIv, flushIv;
    private ListView showlv;
    private ProgressBar loadingView;
    private InfoListAdapter adapter;
    private final List<WordBean> mDatas = new ArrayList<>();
    private SearchWordTask currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        showactionbar();
        initView();
        initListener();
    }

    private void initView() {
        searchEt = findViewById(R.id.info_et_search);
        showlv = findViewById(R.id.infolist_lv);
        searchIv = findViewById(R.id.info_iv_search);
        flushIv = findViewById(R.id.info_iv_flush);
        loadingView = findViewById(R.id.search_progress);

        adapter = new InfoListAdapter(this, mDatas);
        showlv.setAdapter(adapter);
    }

    private void initListener() {
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    launchSearch(keyword);
                } else {
                    mDatas.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        showlv.setOnItemClickListener((parent, view, position, id) -> {
            WordBean word = mDatas.get(position);
            Intent intent = new Intent(SearchActivity.this, WordDescActivity.class);
            intent.putExtra("word", word);
            startActivity(intent);
        });

        searchIv.setOnClickListener(v -> {
            String keyword = searchEt.getText().toString().trim();
            if (TextUtils.isEmpty(keyword)) {
                Toast.makeText(SearchActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            launchSearch(keyword);
        });

        flushIv.setOnClickListener(v -> {
            searchEt.setText("");
            mDatas.clear();
            adapter.notifyDataSetChanged();
        });
    }

    private void launchSearch(String keyword) {
        if (currentTask != null) {
            currentTask.cancel(true);
        }
        currentTask = new SearchWordTask();
        currentTask.execute(keyword);
    }

    private void showactionbar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("单词查询");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SearchWordTask extends AsyncTask<String, Void, List<WordBean>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (loadingView != null) {
                loadingView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<WordBean> doInBackground(String... params) {
            String word = params[0];
            Call<List<WordApiResponse>> call = RetrofitClient.getInstance()
                    .getBaiduApi()
                    .getWordInfo(word);
            try {
                Response<List<WordApiResponse>> response = call.execute();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<WordBean> beans = convertApiResponseToWordBean(response.body());
                    // 如果没有本地中文释义，尝试调用翻译 API
                    for (WordBean bean : beans) {
                        if (TextUtils.isEmpty(bean.getTran())) {
                            String translated = translateToChinese(bean.getTitle());
                            if (!TextUtils.isEmpty(translated)) {
                                bean.setTran(translated);
                            }
                        }
                    }
                    return beans;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return SearchUtils.searchLocal(word);
        }

        @Override
        protected void onPostExecute(List<WordBean> result) {
            super.onPostExecute(result);
            if (isCancelled()) return;
            if (loadingView != null) {
                loadingView.setVisibility(View.GONE);
            }
            mDatas.clear();
            if (result != null && !result.isEmpty()) {
                String currentUser = getCurrentUsername();
                if (!TextUtils.isEmpty(currentUser)) {
                    WordHistoryRepository.getInstance(SearchActivity.this)
                            .saveHistory(currentUser, result.get(0));
                }
                mDatas.addAll(result);
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyDataSetChanged();
                Toast.makeText(SearchActivity.this, "未找到该单词", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // 获取当前登录用户名
    private String getCurrentUsername() {
        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        return sp.getString("current_username", "");
    }
    private List<WordBean> convertApiResponseToWordBean(List<WordApiResponse> apiResponses) {
        List<WordBean> wordList = new ArrayList<>();
        for (WordApiResponse apiResponse : apiResponses) {
            StringBuilder desc = new StringBuilder();
            String firstDefinition = "";
            String phonetic = "";
            String audioUrl = "";
            String chineseTran = "";

            if (apiResponse.getPhonetics() != null && !apiResponse.getPhonetics().isEmpty()) {
                for (WordApiResponse.Phonetic p : apiResponse.getPhonetics()) {
                    if (TextUtils.isEmpty(phonetic) && p.getText() != null) {
                        phonetic = p.getText();
                    }
                    if (!TextUtils.isEmpty(p.getAudio())) {
                        audioUrl = normalizeAudioUrl(p.getAudio());
                        if (!TextUtils.isEmpty(audioUrl)) break;
                    }
                }
            }

            if (apiResponse.getMeanings() != null) {
                for (WordApiResponse.Meaning meaning : apiResponse.getMeanings()) {
                    desc.append(meaning.getPartOfSpeech()).append("：\n");
                    if (meaning.getDefinitions() != null) {
                        for (WordApiResponse.Definition def : meaning.getDefinitions()) {
                            if (TextUtils.isEmpty(firstDefinition) && !TextUtils.isEmpty(def.getDefinition())) {
                                firstDefinition = def.getDefinition();
                            }
                            desc.append("- ").append(def.getDefinition()).append("\n");
                            if (def.getExample() != null) {
                                desc.append("  例：").append(def.getExample()).append("\n");
                            }
                        }
                    }
                    desc.append("\n");
                }
            }
            // 尝试从本地词库补充中文释义
            List<WordBean> local = SearchUtils.searchLocal(apiResponse.getWord());
            if (local != null && !local.isEmpty()) {
                chineseTran = local.get(0).getTran();
            }
            String tranToUse = TextUtils.isEmpty(chineseTran) ? firstDefinition : chineseTran;
            wordList.add(new WordBean(apiResponse.getWord(), tranToUse, desc.toString(), phonetic, audioUrl));
        }
        return wordList;
    }

    private String translateToChinese(String text) {
        if (TextUtils.isEmpty(text)) return "";

        String appid = "你的百度appid";
        String secretKey = "你的百度密钥";
        String salt = String.valueOf(System.currentTimeMillis());
        // 生成签名：MD5(appid+text+salt+secretKey)
        String sign = MD5Utils.md5(appid + text + salt + secretKey);

        try {
            Call<BaiduTranslateResponse> call = RetrofitClient.getBaiduApi()
                    .translate(text, "en", "zh", appid, salt, sign);
            Response<BaiduTranslateResponse> resp = call.execute();
            if (resp.isSuccessful() && resp.body() != null
                    && resp.body().getTransResult() != null
                    && !resp.body().getTransResult().isEmpty()) {
                return resp.body().getTransResult().get(0).getDst();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String normalizeAudioUrl(String raw) {
        if (TextUtils.isEmpty(raw)) return "";
        String url = raw.trim();
        if (url.startsWith("//")) {
            url = "https:" + url;
        } else if (url.startsWith("http:")) {
            url = url.replaceFirst("http:", "https:");
        }
        return url;
    }
}