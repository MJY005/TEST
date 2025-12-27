package com.example.lukedict;

import java.util.List;

public class BaiduTranslateResponse {
    private String from;
    private String to;
    private String error_code;
    private String error_msg;
    private List<TransResult> trans_result;

    // getters
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getErrorCode() { return error_code; }
    public String getErrorMsg() { return error_msg; }
    public List<TransResult> getTransResult() { return trans_result; }

    public static class TransResult {
        private String src;
        private String dst;

        // getters
        public String getSrc() { return src; }
        public String getDst() { return dst; }
    }
}