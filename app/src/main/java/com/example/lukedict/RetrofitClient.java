package com.example.lukedict;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit单例客户端（解决TranslationManager编译错误）
 */
public class RetrofitClient {
    private static final String BASE_URL = "https://你的翻译接口域名/"; // 替换为实际地址
    private static RetrofitClient instance;
    private final Retrofit retrofit;

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

    // 补充缺失的方法（解决编译错误）
    public TranslationApi getTranslationApi() {
        return retrofit.create(TranslationApi.class);
    }
}