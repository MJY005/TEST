package com.example.lukedict;

import java.util.List;

public class BaiduTranslateResponse {
    private String from;
    private String to;
    private List<TransResult> trans_result;

    // getter
    public List<TransResult> getTransResult() { return trans_result; }

    public static class TransResult {
        private String src;
        private String dst;

        // getter
        public String getDst() { return dst; }
    }
}