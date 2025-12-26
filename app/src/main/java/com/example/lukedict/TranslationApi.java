package com.example.lukedict;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 翻译接口（适配TranslationResponse）
 */
public interface TranslationApi {
    @POST("translate")
    Call<TranslationResponse> translate(
            @Query("q") String text,       // 待翻译文本
            @Query("from") String from,    // 源语言
            @Query("to") String to         // 目标语言
    );
}