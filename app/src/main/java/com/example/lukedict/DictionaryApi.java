package com.example.lukedict;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;



public interface DictionaryApi {
    @GET("api/v2/entries/en/{word}")
    Call<List<WordApiResponse>> getWordInfo(@Path("word") String word);
}

