package com.example.lukedict;

import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 翻译管理类（修正编译错误）
 */
public class TranslationManager {
    private static final String TAG = "TranslationManager";

    // 翻译方法（异步）
    public void translate(String text, String from, String to, TranslationCallback callback) {
        RetrofitClient.getInstance()
                .getTranslationApi() // 现在有这个方法了
                .translate(text, from, to)
                .enqueue(new Callback<TranslationResponse>() {
                    @Override
                    public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            // 适配getResponseData()
                            if (resp.body().getResponseData() != null) {
                                callback.onSuccess(resp.body().getResponseData().getTranslatedText());
                            } else if (!resp.body().trans_result.isEmpty()) {
                                // 兼容备用格式
                                callback.onSuccess(resp.body().trans_result.get(0).getDst());
                            } else {
                                callback.onError(new Exception("无翻译结果"));
                            }
                        } else {
                            callback.onError(new Exception("请求失败：" + resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<TranslationResponse> call, Throwable t) {
                        Log.e(TAG, "翻译失败", t);
                        callback.onError(t);
                    }
                });
    }

    // 翻译回调接口
    public interface TranslationCallback {
        void onSuccess(String translatedText);
        void onError(Throwable e);
    }
}