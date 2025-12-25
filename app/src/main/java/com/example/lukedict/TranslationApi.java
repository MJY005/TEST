package com.example.lukedict;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 使用 MyMemory 免费翻译 API 将英文翻译为中文（无需密钥）。
 * 示例：https://api.mymemory.translated.net/get?q=hello&langpair=en|zh-CN
 */
public interface TranslationApi {
    @GET("get")
    Call<TranslationResponse> translate(
            @Query("q") String text,
            @Query("langpair") String langPair
    );
}

