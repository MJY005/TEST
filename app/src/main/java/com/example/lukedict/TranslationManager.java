package com.example.lukedict;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Callback;

public class TranslationManager {
    // 替换为你的百度开发者appid和密钥（在百度翻译开放平台申请）
    private static final String APPID = "你的appid";
    private static final String SECRET_KEY = "你的密钥";

    public static void translate(String text, String toLang, Callback<TranslationResponse> callback) {
        String salt = String.valueOf(System.currentTimeMillis());
        // 生成签名：MD5(appid+text+salt+密钥)
        String sign = generateSign(APPID + text + salt + SECRET_KEY);

        RetrofitClient.getInstance().getTranslationApi()
                .translate(text, "auto", toLang, APPID, salt, sign)
                .enqueue(callback);
    }

    private static String generateSign(String content) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(content.getBytes(StandardCharsets.UTF_8));
            // 转换为16进制字符串
            StringBuilder sign = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) sign.append("0");
                sign.append(hex);
            }
            return sign.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
