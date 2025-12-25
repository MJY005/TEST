package com.example.lukedict;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BaiduTranslateApi {
    // 百度翻译API地址
    @GET("api/trans/vip/translate")
    Call<BaiduTranslateResponse> translate(
            @Query("q") String query,       // 待翻译文本
            @Query("from") String from,     // 源语言（en）
            @Query("to") String to,         // 目标语言（zh）
            @Query("appid") String appid,   // 你的百度appid
            @Query("salt") String salt,     // 随机数
            @Query("sign") String sign      // 签名
    );

    Call<List<WordApiResponse>> getWordInfo(String word);
}