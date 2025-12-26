//package com.example.lukedict;
//
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
////package com.example.lukedict;
////
////import androidx.appcompat.app.ActionBar;
////import androidx.appcompat.app.AppCompatActivity;
////
////import android.content.Intent;
////import android.content.SharedPreferences;
////import android.os.AsyncTask;
////import android.os.Bundle;
////import android.text.Editable;
////import android.text.TextUtils;
////import android.text.TextWatcher;
////import android.view.MenuItem;
////import android.view.View;
////import android.widget.EditText;
////import android.widget.ImageView;
////import android.widget.ListView;
////import android.widget.ProgressBar;
////import android.widget.Toast;
////
////import java.io.IOException;
////import java.util.ArrayList;
////import java.util.List;
////
////import retrofit2.Call;
////import retrofit2.Response;
////
////public class SearchActivity extends AppCompatActivity {
////    private EditText searchEt;
////    private ImageView searchIv, flushIv;
////    private ListView showlv;
////    private ProgressBar loadingView;
////    private InfoListAdapter adapter;
////    private final List<WordBean> mDatas = new ArrayList<>();
////    private SearchWordTask currentTask;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_search);
////        showactionbar();
////        initView();
////        initListener();
////    }
////
////    private void initView() {
////        searchEt = findViewById(R.id.info_et_search);
////        showlv = findViewById(R.id.infolist_lv);
////        searchIv = findViewById(R.id.info_iv_search);
////        flushIv = findViewById(R.id.info_iv_flush);
////        loadingView = findViewById(R.id.search_progress);
////
////        adapter = new InfoListAdapter(this, mDatas);
////        showlv.setAdapter(adapter);
////    }
////
////    private void initListener() {
////        searchEt.addTextChangedListener(new TextWatcher() {
////            @Override
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
////
////            @Override
////            public void onTextChanged(CharSequence s, int start, int before, int count) { }
////
////            @Override
////            public void afterTextChanged(Editable s) {
////                String keyword = s.toString().trim();
////                if (!TextUtils.isEmpty(keyword)) {
////                    launchSearch(keyword);
////                } else {
////                    mDatas.clear();
////                    adapter.notifyDataSetChanged();
////                }
////            }
////        });
////
////        showlv.setOnItemClickListener((parent, view, position, id) -> {
////            WordBean word = mDatas.get(position);
////            Intent intent = new Intent(SearchActivity.this, WordDescActivity.class);
////            intent.putExtra("word", word);
////            startActivity(intent);
////        });
////
////        searchIv.setOnClickListener(v -> {
////            String keyword = searchEt.getText().toString().trim();
////            if (TextUtils.isEmpty(keyword)) {
////                Toast.makeText(SearchActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
////                return;
////            }
////            launchSearch(keyword);
////        });
////
////        flushIv.setOnClickListener(v -> {
////            searchEt.setText("");
////            mDatas.clear();
////            adapter.notifyDataSetChanged();
////        });
////    }
////
////    private void launchSearch(String keyword) {
////        if (currentTask != null) {
////            currentTask.cancel(true);
////        }
////        currentTask = new SearchWordTask();
////        currentTask.execute(keyword);
////    }
////
////    private void showactionbar() {
////        ActionBar actionBar = this.getSupportActionBar();
////        if (actionBar != null) {
////            actionBar.setTitle("单词查询");
////            actionBar.setDisplayHomeAsUpEnabled(true);
////        }
////    }
////
////    public boolean onOptionsItemSelected(MenuItem item) {
////        if (item.getItemId() == android.R.id.home) {
////            finish();
////            return true;
////        }
////        return super.onOptionsItemSelected(item);
////    }
////
////    private class SearchWordTask extends AsyncTask<String, Void, List<WordBean>> {
////        @Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////            if (loadingView != null) {
////                loadingView.setVisibility(View.VISIBLE);
////            }
////        }
////
////        @Override
////        protected List<WordBean> doInBackground(String... params) {
////            String word = params[0];
////            Call<List<WordApiResponse>> call = RetrofitClient.getInstance()
////                    .getBaiduApi()
////                    .getWordInfo(word);
////            try {
////                Response<List<WordApiResponse>> response = call.execute();
////                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
////                    List<WordBean> beans = convertApiResponseToWordBean(response.body());
////                    // 如果没有本地中文释义，尝试调用翻译 API
////                    for (WordBean bean : beans) {
////                        if (TextUtils.isEmpty(bean.getTran())) {
////                            String translated = translateToChinese(bean.getTitle());
////                            if (!TextUtils.isEmpty(translated)) {
////                                bean.setTran(translated);
////                            }
////                        }
////                    }
////                    return beans;
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////            return SearchUtils.searchLocal(word);
////        }
////
////        @Override
////        protected void onPostExecute(List<WordBean> result) {
////            super.onPostExecute(result);
////            if (isCancelled()) return;
////            if (loadingView != null) {
////                loadingView.setVisibility(View.GONE);
////            }
////            mDatas.clear();
////            if (result != null && !result.isEmpty()) {
////                String currentUser = getCurrentUsername();
////                if (!TextUtils.isEmpty(currentUser)) {
////                    WordHistoryRepository.getInstance(SearchActivity.this)
////                            .saveHistory(currentUser, result.get(0));
////                }
////                mDatas.addAll(result);
////                adapter.notifyDataSetChanged();
////            } else {
////                adapter.notifyDataSetChanged();
////                Toast.makeText(SearchActivity.this, "未找到该单词", Toast.LENGTH_SHORT).show();
////            }
////        }
////    }
////
////    // 获取当前登录用户名
////    private String getCurrentUsername() {
////        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
////        return sp.getString("current_username", "");
////    }
////    private List<WordBean> convertApiResponseToWordBean(List<WordApiResponse> apiResponses) {
////        List<WordBean> wordList = new ArrayList<>();
////        for (WordApiResponse apiResponse : apiResponses) {
////            StringBuilder desc = new StringBuilder();
////            String firstDefinition = "";
////            String phonetic = "";
////            String audioUrl = "";
////            String chineseTran = "";
////
////            if (apiResponse.getPhonetics() != null && !apiResponse.getPhonetics().isEmpty()) {
////                for (WordApiResponse.Phonetic p : apiResponse.getPhonetics()) {
////                    if (TextUtils.isEmpty(phonetic) && p.getText() != null) {
////                        phonetic = p.getText();
////                    }
////                    if (!TextUtils.isEmpty(p.getAudio())) {
////                        audioUrl = normalizeAudioUrl(p.getAudio());
////                        if (!TextUtils.isEmpty(audioUrl)) break;
////                    }
////                }
////            }
////
////            if (apiResponse.getMeanings() != null) {
////                for (WordApiResponse.Meaning meaning : apiResponse.getMeanings()) {
////                    desc.append(meaning.getPartOfSpeech()).append("：\n");
////                    if (meaning.getDefinitions() != null) {
////                        for (WordApiResponse.Definition def : meaning.getDefinitions()) {
////                            if (TextUtils.isEmpty(firstDefinition) && !TextUtils.isEmpty(def.getDefinition())) {
////                                firstDefinition = def.getDefinition();
////                            }
////                            desc.append("- ").append(def.getDefinition()).append("\n");
////                            if (def.getExample() != null) {
////                                desc.append("  例：").append(def.getExample()).append("\n");
////                            }
////                        }
////                    }
////                    desc.append("\n");
////                }
////            }
////            // 尝试从本地词库补充中文释义
////            List<WordBean> local = SearchUtils.searchLocal(apiResponse.getWord());
////            if (local != null && !local.isEmpty()) {
////                chineseTran = local.get(0).getTran();
////            }
////            String tranToUse = TextUtils.isEmpty(chineseTran) ? firstDefinition : chineseTran;
////            wordList.add(new WordBean(apiResponse.getWord(), tranToUse, desc.toString(), phonetic, audioUrl));
////        }
////        return wordList;
////    }
////
////    private String translateToChinese(String text) {
////        if (TextUtils.isEmpty(text)) return "";
////
////        String appid = "你的百度appid";
////        String secretKey = "你的百度密钥";
////        String salt = String.valueOf(System.currentTimeMillis());
////        // 生成签名：MD5(appid+text+salt+secretKey)
////        String sign = MD5Utils.md5(appid + text + salt + secretKey);
////
////        try {
////            Call<BaiduTranslateResponse> call = RetrofitClient.getBaiduApi()
////                    .translate(text, "en", "zh", appid, salt, sign);
////            Response<BaiduTranslateResponse> resp = call.execute();
////            if (resp.isSuccessful() && resp.body() != null
////                    && resp.body().getTransResult() != null
////                    && !resp.body().getTransResult().isEmpty()) {
////                return resp.body().getTransResult().get(0).getDst();
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return "";
////    }
////
////    private String normalizeAudioUrl(String raw) {
////        if (TextUtils.isEmpty(raw)) return "";
////        String url = raw.trim();
////        if (url.startsWith("//")) {
////            url = "https:" + url;
////        } else if (url.startsWith("http:")) {
////            url = url.replaceFirst("http:", "https:");
////        }
////        return url;
////    }
////}
////
////
//public class SearchActivity extends AppCompatActivity {
//    private static final String BAIDU_APP_ID = "你的APP_ID"; // 替换为实际APP_ID
//    private static final String BAIDU_SECRET_KEY = "你的SECRET_KEY"; // 替换为实际密钥
//
//    private TextView resultTv;
//    private String currentUsername;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//        resultTv = findViewById(R.id.result_tv);
//        // 获取当前登录用户（从SharedPreferences）
//        currentUsername = getSharedPreferences("user_info", MODE_PRIVATE)
//                .getString("current_username", "");
//    }
//
//    // 点击查询按钮触发
//    public void onSearchClick(View view) {
//        EditText inputEt = findViewById(R.id.input_et);
//        String word = inputEt.getText().toString().trim();
//        if (TextUtils.isEmpty(word)) {
//            Toast.makeText(this, "请输入单词", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        // 1. 异步调用百度翻译API（避免主线程错误）
//        translateWordAsync(word);
//    }
//
//    // 2. 异步翻译方法（使用Retrofit的enqueue）
//    private void translateWordAsync(String word) {
//        String salt = String.valueOf(System.currentTimeMillis());
//        String sign = MD5Utils.md5(BAIDU_APP_ID + word + salt + BAIDU_SECRET_KEY);
//
//        RetrofitClient.getInstance()
//                .getBaiduTranslateApi()
//                .translate(word, "en", "zh", BAIDU_APP_ID, salt, sign)
//                .enqueue(new Callback<BaiduTranslateResponse>() {
//                    @Override
//                    public void onResponse(Call<BaiduTranslateResponse> call, Response<BaiduTranslateResponse> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            handleTranslateSuccess(response.body(), word);
//                        } else {
//                            showError("翻译失败：" + response.message());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<BaiduTranslateResponse> call, Throwable t) {
//                        showError("网络错误：" + t.getMessage());
//                    }
//                });
//    }
//
//    // 3. 处理翻译成功结果
//    private void handleTranslateSuccess(BaiduTranslateResponse response, String originalWord) {
//        if (response.getTransResult() == null || response.getTransResult().isEmpty()) {
//            resultTv.setText("未找到翻译结果");
//            return;
//        }
//        String translation = response.getTransResult().get(0).getDst();
//        resultTv.setText(translation);
//
//        // 4. 保存历史记录（使用之前定义的WordHistoryRepository）
//        if (!TextUtils.isEmpty(currentUsername)) {
//            WordBean wordBean = new WordBean();
//            wordBean.setTitle(originalWord);
//            wordBean.setTran(translation);
//            // 如需音标，可补充（百度翻译API需额外处理）
//            WordHistoryRepository.getInstance(this)
//                    .saveHistory(currentUsername, wordBean);
//        }
//    }
//
//    private void showError(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//        resultTv.setText("错误：" + message);
//    }
//}

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
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_CURRENT_USER = "current_username";

    private EditText searchEt;
    private ImageView searchIv, flushIv;
    private ListView showlv;
    private ProgressBar loadingView;
    private InfoListAdapter adapter;
    private final List<WordBean> mDatas = new ArrayList<>();
    private SearchWordTask currentTask;
    private String currentUsername; // 当前登录用户名
    private WordHistoryRepository historyRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // 初始化历史记录仓库
        historyRepository = WordHistoryRepository.getInstance(this);
        // 获取当前登录用户
        currentUsername = getCurrentUsername();
        showActionBar();
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
        // 文本变化监听：自动搜索
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    launchSearch(keyword);
                } else {
                    clearSearchResults();
                }
            }
        });

        // 列表项点击：进入详情并保存历史
        showlv.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < mDatas.size()) {
                WordBean word = mDatas.get(position);
                // 跳转到详情页
                Intent intent = new Intent(SearchActivity.this, WordDescActivity.class);
                intent.putExtra("word", word);
                startActivity(intent);
                // 保存到历史记录（仅当用户已登录）
                saveToHistory(word);
            }
        });

        // 搜索按钮点击
        searchIv.setOnClickListener(v -> {
            String keyword = searchEt.getText().toString().trim();
            if (TextUtils.isEmpty(keyword)) {
                Toast.makeText(SearchActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            launchSearch(keyword);
        });

        // 清空按钮点击
        flushIv.setOnClickListener(v -> {
            searchEt.setText("");
            clearSearchResults();
        });
    }

    /**
     * 启动搜索任务
     */
    private void launchSearch(String keyword) {
        // 取消当前任务，避免重复请求
        if (currentTask != null && !currentTask.isCancelled()) {
            currentTask.cancel(true);
        }
        currentTask = new SearchWordTask();
        currentTask.execute(keyword);
    }

    /**
     * 清空搜索结果
     */
    private void clearSearchResults() {
        mDatas.clear();
        adapter.notifyDataSetChanged();
    }

    /**
     * 保存单词到历史记录
     */
    private void saveToHistory(WordBean word) {
        if (TextUtils.isEmpty(currentUsername) || word == null) {
            return; // 未登录用户不保存历史
        }
        historyRepository.saveHistory(currentUsername, word, isSuccess -> {
            // 可根据需要处理保存结果（如日志输出）
            if (!isSuccess) {
                runOnUiThread(() ->
                        Toast.makeText(SearchActivity.this, "历史记录保存失败", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    /**
     * 从SharedPreferences获取当前登录用户名
     */
    private String getCurrentUsername() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(KEY_CURRENT_USER, "");
    }

    /**
     * 显示ActionBar并设置返回按钮
     */
    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("单词查询");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 处理ActionBar返回按钮
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 搜索单词的异步任务
     */
    private class SearchWordTask extends AsyncTask<String, Void, List<WordBean>> {
        private Call<List<WordApiResponse>> apiCall; // 用于取消网络请求

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingView.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<WordBean> doInBackground(String... params) {
            String word = params[0];
            // 构建网络请求
            apiCall = RetrofitClient.getInstance()
                    .getDictionaryApi()
                    .getWordInfo(word);

            try {
                // 检查任务是否已取消
                if (isCancelled()) return null;

                Response<List<WordApiResponse>> response = apiCall.execute();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<WordBean> beans = convertApiResponseToWordBean(response.body());
                    // 补充中文翻译
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
            } finally {
                // 任务取消时取消网络请求
                if (isCancelled() && apiCall != null) {
                    apiCall.cancel();
                }
            }
            // 网络请求失败时查询本地词库
            return SearchUtils.searchLocal(word);
        }

        @Override
        protected void onPostExecute(List<WordBean> result) {
            super.onPostExecute(result);
            loadingView.setVisibility(View.GONE);
            updateSearchResults(result);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            loadingView.setVisibility(View.GONE);
            apiCall = null; // 释放资源
        }
    }

    /**
     * 更新搜索结果列表
     */
    private void updateSearchResults(List<WordBean> result) {
        mDatas.clear();
        if (result != null && !result.isEmpty()) {
            mDatas.addAll(result);
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "未找到该单词", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 转换API响应为WordBean列表
     */
    private List<WordBean> convertApiResponseToWordBean(List<WordApiResponse> apiResponses) {
        List<WordBean> wordList = new ArrayList<>();
        for (WordApiResponse apiResponse : apiResponses) {
            StringBuilder desc = new StringBuilder();
            String firstDefinition = "";
            String phonetic = "";
            String audioUrl = "";
            String chineseTran = "";

            // 解析音标和发音URL
            if (apiResponse.getPhonetics() != null && !apiResponse.getPhonetics().isEmpty()) {
                for (WordApiResponse.Phonetic p : apiResponse.getPhonetics()) {
                    if (TextUtils.isEmpty(phonetic) && p.getText() != null) {
                        phonetic = p.getText();
                    }
                    if (!TextUtils.isEmpty(p.getAudio()) && TextUtils.isEmpty(audioUrl)) {
                        audioUrl = normalizeAudioUrl(p.getAudio());
                    }
                }
            }

            // 解析释义
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

            // 补充本地中文释义
            List<WordBean> local = SearchUtils.searchLocal(apiResponse.getWord());
            if (local != null && !local.isEmpty()) {
                chineseTran = local.get(0).getTran();
            }

            String tranToUse = TextUtils.isEmpty(chineseTran) ? firstDefinition : chineseTran;
            wordList.add(new WordBean(apiResponse.getWord(), tranToUse, desc.toString(), phonetic, audioUrl));
        }
        return wordList;
    }

    /**
     * 翻译文本为中文
     */
    private String translateToChinese(String text) {
        try {
            Call<TranslationResponse> call = TranslationClient.getApi()
                    .translate(text, "en|zh-CN");
            Response<TranslationResponse> resp = call.execute();
            if (resp.isSuccessful() && resp.body() != null && resp.body().getResponseData() != null) {
                return resp.body().getResponseData().getTranslatedText();
            }
        } catch (IOException e) {
            // 翻译失败不影响主流程
        }
        return "";
    }

    /**
     * 标准化音频URL格式
     */
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

    /**
     * 销毁时取消任务，防止内存泄漏
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentTask != null && !currentTask.isCancelled()) {
            currentTask.cancel(true);
        }
    }
}