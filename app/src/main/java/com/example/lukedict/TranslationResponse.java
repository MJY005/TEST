package com.example.lukedict;

import java.util.List;

/**
 * 翻译接口响应实体（解决getResponseData编译错误）
 */
public class TranslationResponse {
    private int code;
    private String msg;
    private ResponseData responseData; // 对应getResponseData()

    // Getter（解决编译错误）
    public ResponseData getResponseData() {
        return responseData;
    }

    // 内部实体类
    public static class ResponseData {
        private String translatedText; // 翻译结果

        // Getter
        public String getTranslatedText() {
            return translatedText;
        }
    }

    // 备用：兼容常见翻译接口格式（如百度翻译）
    List<TransResult> trans_result;
    public static class TransResult {
        private String dst; // 翻译结果
        public String getDst() { return dst; }
    }
}