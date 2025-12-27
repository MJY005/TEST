package com.example.lukedict;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DictionaryApi {
    /**
     * Merriam-Webster Collegiate Dictionary API
     * @param word 要查询的单词
     * @param key API密钥
     */
    @GET("api/v3/references/collegiate/json/{word}")
    Call<List<DictionaryResponse>> getWordDetails(
            @Path("word") String word,
            @Query("key") String key
    );
}
