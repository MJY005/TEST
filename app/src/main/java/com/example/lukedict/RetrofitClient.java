package com.example.lukedict;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit单例客户端（解决TranslationManager编译错误）
 */
public class RetrofitClient {
    private static final String BASE_URL = "https://fanyi-api.baidu.com/"; // 百度翻译API基础地址（不包含路径）
    private static RetrofitClient instance;
    private final Retrofit retrofit;
    
    // 获取BASE_URL（用于检查是否配置）
    public String getBaseUrl() {
        return BASE_URL;
    }

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // 获取单例
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    // 获取百度翻译API
    public BaiduTranslateApi getBaiduTranslateApi() {
        return retrofit.create(BaiduTranslateApi.class);
    }
    
    // 兼容旧接口（已废弃，建议使用getBaiduTranslateApi）
    @Deprecated
    public TranslationApi getTranslationApi() {
        return retrofit.create(TranslationApi.class);
    }
}