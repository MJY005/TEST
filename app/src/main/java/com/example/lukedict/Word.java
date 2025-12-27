package com.example.lukedict;

public class Word {
    private int id;
    private String word;
    private String translation;
    private long queryTime;

    // 构造方法、getter和setter
    public Word(String word, String translation) {
        this.word = word;
        this.translation = translation;
        this.queryTime = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public long getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(long queryTime) {
        this.queryTime = queryTime;
    }
}