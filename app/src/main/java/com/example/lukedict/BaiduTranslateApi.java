package com.example.lukedict;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BaiduTranslateApi {
    @GET("api/trans/vip/translate")
    Call<BaiduTranslateResponse> translate(
            @Query("q") String query,
            @Query("from") String from,
            @Query("to") String to,
            @Query("appid") String appid,
            @Query("salt") String salt,
            @Query("sign") String sign
    );
}