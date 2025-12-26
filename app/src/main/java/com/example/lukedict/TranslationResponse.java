package com.example.lukedict;

import com.google.gson.annotations.SerializedName;

import java.util.List;
//（解析API返回数据）
public class TranslationResponse {
    private String error_code;
    private List<TranslateResult> trans_result;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<TranslateResult> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<TranslateResult> trans_result) {
        this.trans_result = trans_result;
    }

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