package com.example.lukedict;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Response;

public class WordRepository {
    private static WordRepository instance;
    private WordLocalDataSource localDataSource;
    private WordRemoteDataSource remoteDataSource;

    private WordRepository(Context context) {
        localDataSource = new WordLocalDataSource(context);
        remoteDataSource = new WordRemoteDataSource();
    }

    public static synchronized WordRepository getInstance(Context context) {
        if (instance == null) {
            instance = new WordRepository(context);
        }
        return instance;
    }

    // 先查本地，本地没有则查网络
    public void getWordTranslation(String word, Callback<Word> callback) {
        // 先查询本地数据库
        Word localWord = localDataSource.queryWord(word);
        if (localWord != null) {
            callback.onSuccess(localWord);
            return;
        }

        // 本地没有，查询网络
        remoteDataSource.getTranslation(word).enqueue(new Callback<BaiduTranslateResponse>() {
            @Override
            public void onResponse(Call<BaiduTranslateResponse> call, Response<BaiduTranslateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaiduTranslateResponse baiduTranslateResponse = response.body();
                    if (baiduTranslateResponse.getTransResult() != null && !baiduTranslateResponse.getTransResult().isEmpty()) {
                        String translation = baiduTranslateResponse.getTransResult().get(0).getDst();
                        Word newWord = new Word(word, translation);
                        // 保存到本地数据库
                        localDataSource.insertWord(newWord);
                        callback.onSuccess(newWord);
                    } else {
                        callback.onFailure(new Exception("No translation found"));
                    }
                } else {
                    callback.onFailure(new Exception("Translation request failed"));
                }
            }

            @Override
            public void onFailure(Call<BaiduTranslateResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public interface Callback<T> {
        void onSuccess(T result);
        void onFailure(Throwable e);
    }
}