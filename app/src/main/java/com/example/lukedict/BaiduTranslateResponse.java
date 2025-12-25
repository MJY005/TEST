package com.example.lukedict;

import java.util.List;

public class BaiduTranslateResponse {
    private String from;
    private String to;
    private List<TranslateResult> trans_result;

    public static class TranslateResult {
        private String src;       // 原文
        private String dst;       // 译文

        public String getDst() { return dst; }
    }

    public List<TranslateResult> getTransResult() { return trans_result; }
}