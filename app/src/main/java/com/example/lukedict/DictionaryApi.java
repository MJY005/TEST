package com.example.lukedict;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Free Dictionary API 接口定义。
 * 示例：GET https://api.dictionaryapi.dev/api/v2/entries/en/hello
 */
public interface DictionaryApi {
    @GET("api/v2/entries/en/{word}")
    Call<List<WordApiResponse>> getWordInfo(@Path("word") String word);
}

