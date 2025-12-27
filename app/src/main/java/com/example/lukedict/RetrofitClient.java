package com.example.lukedict;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RetrofitClient {
    private static RetrofitClient instance;
    private Retrofit retrofit;
    private Retrofit dictionaryRetrofit;
    private static final String BASE_URL_BAIDU = "https://fanyi-api.baidu.com/";
    private static final String BASE_URL_DICTIONARY = "https://www.dictionaryapi.com/";


    public Retrofit getRetrofit() {
        return retrofit;
    }
    private RetrofitClient() {
        // 百度翻译API Retrofit实例
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_BAIDU)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        // 词典API Retrofit实例
        dictionaryRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_DICTIONARY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
    @Deprecated
    public BaiduTranslateApi getTranslationApi() {
        throw new UnsupportedOperationException("已废弃，请使用百度翻译接口");
    }

    // 单例Retrofit实例
    private static Retrofit getBaiduRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_BAIDU)
                .addConverterFactory(GsonConverterFactory.create())
                // 如需添加拦截器传递appid、签名等参数，这里配置
                // .client(okHttpClient)
                .build();
    }

    // 构建百度翻译API的接口服务
    public static BaiduTranslateApi getBaiduTranslateApi() {
        return getBaiduRetrofitInstance().create(BaiduTranslateApi.class);
    }
    
    // 构建词典API的接口服务
    public DictionaryApi getDictionaryApi() {
        return dictionaryRetrofit.create(DictionaryApi.class);
    }

    // 百度翻译API接口定义
    public interface BaiduTranslateApi {
        // 接口路径：api/trans/vip/translate（不能以/开头）
        @POST("api/trans/vip/translate")
        @FormUrlEncoded
        Call<BaiduTranslateResponse> translate(
                @Field("q") String query,       // 要翻译的文本
                @Field("from") String from,     // 源语言
                @Field("to") String to,         // 目标语言
                @Field("appid") String appid,   // 百度翻译APPID
                @Field("salt") String salt,     // 随机数
                @Field("sign") String sign      // 签名（appid+q+salt+密钥的MD5）
        );
    }
}