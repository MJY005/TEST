package com.example.lukedict;

import java.io.Serializable;

public class WordBean implements Serializable {
    private String title;
    private String tran;
    private String desc;   //信息
    private String phonetic;
    private String audioUrl;

    public String getTitle() {
        return title;
    }

    public String getTran() {
        return tran;
    }

    public void setTran(String tran) {
        this.tran = tran;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudio(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public WordBean(String title, String tran, String desc) {
        this(title, tran, desc, "", "");
    }

    public WordBean(String title, String tran, String desc, String phonetic, String audioUrl) {
        this.title = title;
        this.tran = tran;
        this.desc = desc;
        this.phonetic = phonetic;
        this.audioUrl = audioUrl;
    }

    public WordBean() {
    }

    public String getAudio() {
        return null;
    }
}
