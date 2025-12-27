package com.example.lukedict;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 翻译客户端，使用 MyMemory 免费翻译 API。
 */
public class TranslationClient {
    private static final String BASE_URL = "https://api.mymemory.translated.net/";
    private static BaiduTranslateApi api;

    public static BaiduTranslateApi getApi() {
        if (api == null) {
            synchronized (TranslationClient.class) {
                if (api == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    api = retrofit.create(BaiduTranslateApi.class);
                }
            }
        }
        return api;
    }
}

