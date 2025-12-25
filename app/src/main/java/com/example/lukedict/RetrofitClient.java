package com.example.lukedict;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//package com.example.lukedict;
//
//import android.util.Log;
//
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Retrofit 单例客户端，负责创建 API 实例。
// */
//public class RetrofitClient {
//    // 百度翻译API baseUrl
//    private static final String BASE_URL = "https://fanyi-api.baidu.com/";
//    private static RetrofitClient instance;
//    private final TranslationApi translationApi;
//    private RetrofitClient() {
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
//                message -> Log.d("Retrofit", message)
//        );
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .readTimeout(15, TimeUnit.SECONDS)
//                .build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//        translationApi = retrofit.create(TranslationApi.class);
//    }
//
//    public static synchronized RetrofitClient getInstance() {
//        if (instance == null) {
//            instance = new RetrofitClient();
//        }
//        return instance;
//    }
//
//    public TranslationApi getTranslationApi() { // 提供翻译API
//        return translationApi;
//    }
//}
//
public class RetrofitClient {
<<<<<<< HEAD
    private Retrofit retrofit;
    private static RetrofitClient instance;
=======
    // 百度翻译API baseUrl
    private static final String BASE_URL = "https://fanyi-api.baidu.com/";
    private static RetrofitClient instance;
    private final TranslationApi translationApi;
>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
    private RetrofitClient() {
        // 初始化Retrofit（替换成你的实际基础URL）
        retrofit = new Retrofit.Builder()
                .baseUrl("https://your-api-base-url.com/")
                .addConverterFactory(GsonConverterFactory.create()) // 解析JSON的转换器
                .build();
<<<<<<< HEAD
=======

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        translationApi = retrofit.create(TranslationApi.class);
>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
    }
    // 百度翻译API基础地址
    private static final String BAIDU_BASE_URL = "https://fanyi-api.baidu.com/";

    private static Retrofit baiduRetrofit;


    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

<<<<<<< HEAD
    public static BaiduTranslateApi getBaiduApi() {
        if (baiduRetrofit == null) {
            baiduRetrofit = new Retrofit.Builder()
                    .baseUrl(BAIDU_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return baiduRetrofit.create(BaiduTranslateApi.class);
=======
    public TranslationApi getTranslationApi() { // 提供翻译API
        return translationApi;
>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
    }

}



