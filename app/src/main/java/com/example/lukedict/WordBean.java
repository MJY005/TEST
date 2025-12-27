package com.example.lukedict;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 单词数据模型，包含单词基本信息、音标、发音URL、释义等
 */
public class WordBean implements Serializable {
    // 原有字段（保持兼容）
    private String title;         // 单词本身（如 "apple"）
    private String tran;          // 翻译（简略）
    private String desc;          // 补充说明信息
    private String phonetic;      // 通用音标（兼容旧逻辑）
    private String audioUrl;      // 通用发音URL（兼容旧逻辑）
    
    // 新增字段：区分英式/美式音标和发音
    private String ukPhonetic;    // 英式音标（如 "ˈæpl"）
    private String usPhonetic;    // 美式音标（如 "ˈæpl"）
    private String ukAudio;       // 英音发音URL
    private String usAudio;       // 美音发音URL
    private List<WordDetail> details; // 词性+详细释义列表
    private String example;       // 例句
    private String exampleAudio;  // 例句发音URL

    // 内部类：词性+释义模型
    public static class WordDetail implements Serializable {
        private String partOfSpeech; // 词性（如 v, adj, n）
        private String meaning;      // 对应释义

        public WordDetail() {}

        public WordDetail(String partOfSpeech, String meaning) {
            this.partOfSpeech = partOfSpeech;
            this.meaning = meaning;
        }

        public String getPartOfSpeech() { return partOfSpeech; }
        public void setPartOfSpeech(String partOfSpeech) { this.partOfSpeech = partOfSpeech; }
        public String getMeaning() { return meaning; }
        public void setMeaning(String meaning) { this.meaning = meaning; }
    }

    // 原有构造方法（保持兼容）
    public WordBean() {
        this.details = new ArrayList<>();
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
        this.details = new ArrayList<>();
        // 初始化新增音标字段为空（避免默认硬编码提示文本，由展示层处理）
        this.ukPhonetic = "";
        this.usPhonetic = "";
        this.ukAudio = "";
        this.usAudio = "";
    }

    // 新增构造方法：仅初始化单词标题，其他字段置空
    public WordBean(String word) {
        this.title = word;
        this.details = new ArrayList<>();
        // 音标字段初始化为空（提示文本由展示层根据空值判断显示）
        this.phonetic = "";
        this.audioUrl = "";
        this.ukPhonetic = "";
        this.usPhonetic = "";
        this.ukAudio = "";
        this.usAudio = "";
        this.example = "";
        this.exampleAudio = "";
    }

    // 原有Getter和Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTran() {
        return tran;
    }

    public void setTran(String tran) {
        this.tran = tran;
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

    // 修正方法名，与getAudio保持一致（原setAudio命名不规范）
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getAudio() {
        return audioUrl;
    }

    // 新增Getter和Setter（音标/发音相关）
    public String getUkPhonetic() {
        return ukPhonetic;
    }

    public void setUkPhonetic(String ukPhonetic) {
        this.ukPhonetic = ukPhonetic;
    }

    public String getUsPhonetic() {
        return usPhonetic;
    }

    public void setUsPhonetic(String usPhonetic) {
        this.usPhonetic = usPhonetic;
    }

    public String getUkAudio() {
        return ukAudio;
    }

    public void setUkAudio(String ukAudio) {
        this.ukAudio = ukAudio;
    }

    public String getUsAudio() {
        return usAudio;
    }

    public void setUsAudio(String usAudio) {
        this.usAudio = usAudio;
    }

    public List<WordDetail> getDetails() {
        return details;
    }

    public void setDetails(List<WordDetail> details) {
        this.details = details;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExampleAudio() {
        return exampleAudio;
    }

    public void setExampleAudio(String exampleAudio) {
        this.exampleAudio = exampleAudio;
    }
}
