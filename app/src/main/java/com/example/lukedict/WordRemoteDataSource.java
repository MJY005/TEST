package com.example.lukedict;

import retrofit2.Call;

public class WordRemoteDataSource {
    private static final String APP_ID = "20251226002527897";
    private static final String SECRET_KEY = "2bQ5B2RfBc5D_9_oYIiB";
    private RetrofitClient.BaiduTranslateApi translateApi;

    public WordRemoteDataSource() {
        translateApi = RetrofitClient.getBaiduTranslateApi();
    }

    public Call<BaiduTranslateResponse> getTranslation(String word) {
        String salt = String.valueOf(System.currentTimeMillis());
        String sign = MD5Utils.md5(APP_ID + word + salt + SECRET_KEY);

        return translateApi.translate(word, "en", "zh",
                APP_ID, salt, sign);
    }
}