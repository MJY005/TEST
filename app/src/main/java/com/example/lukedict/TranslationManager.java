package com.example.lukedict;// main/java/com/example/lukedict/TranslationManager.java
import android.util.Log;

import com.example.lukedict.BaiduTranslateResponse;
import com.example.lukedict.RetrofitClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslationManager {
    private static final String TAG = "TranslationManager";
    // 百度翻译配置（建议后续移至安全存储）
    private static final String APP_ID = "7367498";
    private static final String SECRET_KEY = "GqHxRVJqiM1zycfkAmbwLmq5MLpx2dFb";

    // 翻译方法（适配百度翻译API）
    public void translate(String text, String from, String to, TranslationCallback callback) {
        // 生成随机数salt
        String salt = String.valueOf(new Random().nextInt(1000000));
        // 计算百度签名（appid + text + salt + 密钥的MD5）
        String sign = generateSign(text, salt);

        // 调用百度翻译API（替换原MyMemory接口调用）
        RetrofitClient.getInstance()
                .getBaiduTranslateApi() // 改用百度翻译接口
                .translate(text, from, to, APP_ID, salt, sign) // 百度要求的参数
                .enqueue(new Callback<BaiduTranslateResponse>() { // 改用百度响应模型
                    @Override
                    public void onResponse(Call<BaiduTranslateResponse> call, Response<BaiduTranslateResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            BaiduTranslateResponse response = resp.body();
                            // 百度API错误处理（如appid错误、签名错误等）
                            if (response.getErrorCode() != null) {
                                callback.onError(new Exception("翻译失败：" + response.getErrorCode()));
                                return;
                            }
                            // 解析百度翻译结果（trans_result中的dst字段）
                            if (response.getTransResult() != null && !response.getTransResult().isEmpty()) {
                                callback.onSuccess(response.getTransResult().get(0).getDst());
                            } else {
                                callback.onError(new Exception("无翻译结果"));
                            }
                        } else {
                            callback.onError(new Exception("请求失败：" + resp.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaiduTranslateResponse> call, Throwable t) {
                        Log.e(TAG, "翻译失败", t);
                        callback.onError(t);
                    }
                });
    }

    // 生成百度翻译签名（复用项目中已有的MD5Utils更佳）
    private String generateSign(String text, String salt) {
        String origin = APP_ID + text + salt + SECRET_KEY;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(origin.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) sb.append("0");
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    // 回调接口保持不变
    public interface TranslationCallback {
        void onSuccess(String translatedText);
        void onError(Throwable e);
    }
}
//package com.example.lukedict;
//
//import android.content.Context;
//import android.security.keystore.KeyGenParameterSpec;
//import android.security.keystore.KeyProperties;
//import android.util.Log;
//
////import androidx.security.crypto.EncryptedSharedPreferences;
////import androidx.security.crypto.MasterKey;
//
//import com.example.lukedict.BaiduTranslateResponse;
//import com.example.lukedict.RetrofitClient;
//
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class TranslationManager {
//    private static final String TAG = "TranslationManager";
//    // 安全存储的key（用于获取APP_ID和SECRET_KEY）
//    private static final String PREF_NAME = "baidu_translate_prefs";
//    private static final String KEY_APP_ID = "7367498";
//    private static final String KEY_SECRET_KEY = "GqHxRVJqiM1zycfkAmbwLmq5MLpx2dFb";
//
//    private final String appId;
//    private final String secretKey;
//
//    private static final Map<String, String> ERROR_CODE_MAP = new HashMap<String, String>() {{
//        put("1000", "APP_ID或SECRET_KEY错误");
//        put("1001", "签名错误");
//        put("1002", "翻译文本为空");
//        put("1003", "文本长度超过限制（单次最大6000字节）");
//        put("1005", "API权限不足（未开通翻译服务）");
//    }};
//
//    // 构造方法：从安全存储初始化APP_ID和SECRET_KEY
//    public TranslationManager(Context context) {
//        try {
//            // 使用EncryptedSharedPreferences安全存储（需依赖AndroidX Security库）
//            MasterKey masterKey = new MasterKey.Builder(context)
//                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//                    .build();
//
//            var sharedPreferences = EncryptedSharedPreferences.create(
//                    context,
//                    PREF_NAME,
//                    masterKey,
//                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//            );
//
//            this.appId = sharedPreferences.getString(KEY_APP_ID, "");
//            this.secretKey = sharedPreferences.getString(KEY_SECRET_KEY, "");
//
//            // 校验初始化结果
//            if (appId.isEmpty() || secretKey.isEmpty()) {
//                Log.e(TAG, "APP_ID或SECRET_KEY未配置，请检查安全存储");
//            }
//        } catch (GeneralSecurityException | IOException e) {
//            Log.e(TAG, "初始化安全存储失败", e);
//            this.appId = "";
//            this.secretKey = "";
//        }
//    }
//
//    /**
//     * 翻译方法（适配百度翻译API）
//     * @param text 待翻译文本
//     * @param from 源语言（如"auto"自动检测）
//     * @param to 目标语言（如"en"英文）
//     * @param callback 翻译结果回调
//     */
//    public void translate(String text, String from, String to, TranslationCallback callback) {
//        // 1. 校验必要参数
//        if (appId.isEmpty() || secretKey.isEmpty()) {
//            callback.onError(new Exception("翻译服务未初始化：APP_ID或SECRET_KEY缺失"));
//            return;
//        }
//        if (text == null || text.trim().isEmpty()) {
//            callback.onError(new Exception("待翻译文本不能为空"));
//            return;
//        }
//        // 2. 校验文本长度（百度API单次翻译最大6000字节，UTF-8编码下大致对应2000个汉字）
//        try {
//            if (text.getBytes("UTF-8").length > 6000) {
//                callback.onError(new Exception("文本过长，请控制在6000字节以内"));
//                return;
//            }
//        } catch (Exception e) {
//            callback.onError(new Exception("文本编码校验失败"));
//            return;
//        }
//
//        // 3. 生成随机数salt（使用ThreadLocalRandom更安全）
//        String salt = String.valueOf(ThreadLocalRandom.current().nextInt(1000000));
//        // 4. 计算签名
//        String sign = generateSign(text, salt);
//        if (sign.isEmpty()) {
//            callback.onError(new Exception("签名生成失败"));
//            return;
//        }
//
//        // 5. 调用百度翻译API
//        RetrofitClient.getInstance()
//                .getBaiduTranslateApi()
//                .translate(text, from, to, appId, salt, sign)
//                .enqueue(new Callback<BaiduTranslateResponse>() {
//                    @Override
//                    public void onResponse(Call<BaiduTranslateResponse> call, Response<BaiduTranslateResponse> resp) {
//                        if (resp.isSuccessful() && resp.body() != null) {
//                            BaiduTranslateResponse response = resp.body();
//                            // 处理API返回的错误
//                            if (response.getErrorCode() != null) {
//                                String errorMsg = ERROR_CODE_MAP.getOrDefault(
//                                        response.getErrorCode(),
//                                        "翻译失败：错误码=" + response.getErrorCode()
//                                );
//                                callback.onError(new Exception(errorMsg));
//                                return;
//                            }
//                            // 解析翻译结果
//                            if (response.getTransResult() != null && !response.getTransResult().isEmpty()) {
//                                callback.onSuccess(response.getTransResult().get(0).getDst());
//                            } else {
//                                callback.onError(new Exception("无翻译结果"));
//                            }
//                        } else {
//                            callback.onError(new Exception("请求失败：HTTP状态码=" + resp.code()));
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<BaiduTranslateResponse> call, Throwable t) {
//                        Log.e(TAG, "翻译请求失败", t);
//                        callback.onError(new Exception("网络异常：" + t.getMessage()));
//                    }
//                });
//    }
//
//    /**
//     * 生成百度翻译签名（appid + text + salt + 密钥的MD5）
//     */
//    private String generateSign(String text, String salt) {
//        try {
//            // 明确使用UTF-8编码，避免系统默认编码差异
//            String origin = appId + text + salt + secretKey;
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] bytes = md.digest(origin.getBytes("UTF-8"));
//
//            // 转换为16进制字符串
//            StringBuilder sb = new StringBuilder();
//            for (byte b : bytes) {
//                String hex = Integer.toHexString(b & 0xFF);
//                if (hex.length() == 1) {
//                    sb.append("0");
//                }
//                sb.append(hex);
//            }
//            return sb.toString();
//        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
//            Log.e(TAG, "生成签名失败", e);
//            return "";
//        }
//    }
//
//    /**
//     * 翻译结果回调接口
//     */
//    public interface TranslationCallback {
//        void onSuccess(String translatedText);
//        void onError(Throwable e);
//    }
//}
