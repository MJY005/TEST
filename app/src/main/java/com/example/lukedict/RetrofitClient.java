package com.example.lukedict;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//package com.example.lukedict;
//
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
////package com.example.lukedict;
////
////import android.util.Log;
////
////import java.util.concurrent.TimeUnit;
////
////import okhttp3.OkHttpClient;
////import okhttp3.logging.HttpLoggingInterceptor;
////import retrofit2.Retrofit;
////import retrofit2.converter.gson.GsonConverterFactory;
////
/////**
//// * Retrofit 单例客户端，负责创建 API 实例。
//// */
////public class RetrofitClient {
////    // 百度翻译API baseUrl
////    private static final String BASE_URL = "https://fanyi-api.baidu.com/";
////    private static RetrofitClient instance;
////    private final TranslationApi translationApi;
////    private RetrofitClient() {
////        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
////                message -> Log.d("Retrofit", message)
////        );
////        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
////
////        OkHttpClient client = new OkHttpClient.Builder()
////                .addInterceptor(loggingInterceptor)
////                .connectTimeout(15, TimeUnit.SECONDS)
////                .readTimeout(15, TimeUnit.SECONDS)
////                .build();
////
////        Retrofit retrofit = new Retrofit.Builder()
////                .baseUrl(BASE_URL)
////                .addConverterFactory(GsonConverterFactory.create())
////                .client(client)
////                .build();
////        translationApi = retrofit.create(TranslationApi.class);
////    }
////
////    public static synchronized RetrofitClient getInstance() {
////        if (instance == null) {
////            instance = new RetrofitClient();
////        }
////        return instance;
////    }
////
////    public TranslationApi getTranslationApi() { // 提供翻译API
////        return translationApi;
////    }
////}
////
//public class RetrofitClient {
//<<<<<<< HEAD
//    private Retrofit retrofit;
//    private static RetrofitClient instance;
//=======
//    // 百度翻译API baseUrl
//    private static final String BASE_URL = "https://fanyi-api.baidu.com/";
//    private static RetrofitClient instance;
//    private final TranslationApi translationApi;
//>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
//    private RetrofitClient() {
//        // 初始化Retrofit（替换成你的实际基础URL）
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://your-api-base-url.com/")
//                .addConverterFactory(GsonConverterFactory.create()) // 解析JSON的转换器
//                .build();
//<<<<<<< HEAD
//=======
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//        translationApi = retrofit.create(TranslationApi.class);
//>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
//    }
//    // 百度翻译API基础地址
//    private static final String BAIDU_BASE_URL = "https://fanyi-api.baidu.com/";
//
//    private static Retrofit baiduRetrofit;
//
//
//    public static synchronized RetrofitClient getInstance() {
//        if (instance == null) {
//            instance = new RetrofitClient();
//        }
//        return instance;
//    }
//
//<<<<<<< HEAD
//    public static BaiduTranslateApi getBaiduApi() {
//        if (baiduRetrofit == null) {
//            baiduRetrofit = new Retrofit.Builder()
//                    .baseUrl(BAIDU_BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return baiduRetrofit.create(BaiduTranslateApi.class);
//=======
//    public TranslationApi getTranslationApi() { // 提供翻译API
//        return translationApi;
//>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
//    }
//
//}
//
//
//
public class RetrofitClient {
    // 1. 新增百度翻译API基础地址
    private static final String BAIDU_TRANSLATE_URL = "https://fanyi-api.baidu.com/";
    private static final String DICTIONARY_URL = "https://api.dictionaryapi.dev/"; // 保留旧API（如需）

    private static RetrofitClient instance;
    private final DictionaryApi dictionaryApi; // 旧API（可选保留）
    private final BaiduTranslateApi baiduTranslateApi; // 新API

    private RetrofitClient() {
        // 公共OkHttpClient配置（添加超时和日志）
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(
                        message -> Log.d("Retrofit", message)
                ).setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        // 初始化旧词典API（如需保留）
        dictionaryApi = new Retrofit.Builder()
                .baseUrl(DICTIONARY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(DictionaryApi.class);

        // 2. 初始化百度翻译API
        baiduTranslateApi = new Retrofit.Builder()
                .baseUrl(BAIDU_TRANSLATE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(BaiduTranslateApi.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    // 3. 提供百度翻译API实例
    public BaiduTranslateApi getBaiduTranslateApi() {
        return baiduTranslateApi;
    }

    public DictionaryApi getDictionaryApi() {
        return dictionaryApi;
    }
}