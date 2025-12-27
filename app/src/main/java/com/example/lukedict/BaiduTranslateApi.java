package com.example.lukedict;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaiduTranslateApi {
    @POST("api/trans/vip/translate")
    @FormUrlEncoded
    Call<BaiduTranslateResponse> translate(
            @Field("q") String query,
            @Field("from") String from,
            @Field("to") String to,
            @Field("appid") String appid,
            @Field("salt") String salt,
            @Field("sign") String sign
    );
}