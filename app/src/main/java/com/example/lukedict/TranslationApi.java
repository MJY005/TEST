package com.example.lukedict;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslationApi {
    // 百度翻译API接口
    @GET("api/trans/vip/translate")
    Call<TranslationResponse> translate(
            @Query("q") String query,
            @Query("from") String from,
            @Query("to") String to,
            @Query("appid") String appid,
            @Query("salt") String salt,
            @Query("sign") String sign
    );
}

