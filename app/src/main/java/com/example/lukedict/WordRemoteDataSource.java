package com.example.lukedict;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordRemoteDataSource {
    private static final String APP_ID = "7367498";
    private static final String SECRET_KEY = "GqHxRVJqiM1zycfkAmbwLmq5MLpx2dFb";
    private BaiduTranslateApi translateApi;

    public WordRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fanyi-api.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        translateApi = retrofit.create(BaiduTranslateApi.class);
    }

    public Call<BaiduTranslateResponse> getTranslation(String word) {
        String salt = String.valueOf(System.currentTimeMillis());
        String sign = MD5Utils.md5(APP_ID + word + salt + SECRET_KEY);

        return translateApi.translate(word, "en", "zh",
                APP_ID, salt, sign);
    }
}