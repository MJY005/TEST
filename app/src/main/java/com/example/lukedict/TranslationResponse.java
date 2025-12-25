package com.example.lukedict;

import com.google.gson.annotations.SerializedName;

import java.util.List;
//（解析API返回数据）
public class TranslationResponse {
    private String error_code;
    private List<TranslateResult> trans_result;

    public static class TranslateResult {
        private String src;
        private String dst;

        public String getDst() { return dst; }
    }

    public boolean isSuccess() { return error_code == null; }
    public String getTranslatedText() {
        if (trans_result != null && !trans_result.isEmpty()) {
            return trans_result.get(0).dst;
        }
        return "翻译失败";
    }
}