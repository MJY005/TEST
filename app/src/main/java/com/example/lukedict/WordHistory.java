package com.example.lukedict;

public class WordHistory {
    private long id;
    private String username;
    private String word;
    private String translation;
    private String phonetic;
    private String queryTime;

    // Getter & Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }
    public String getTranslation() { return translation; }
    public void setTranslation(String translation) { this.translation = translation; }
    public String getPhonetic() { return phonetic; }
    public void setPhonetic(String phonetic) { this.phonetic = phonetic; }
    public String getQueryTime() { return queryTime; }
    public void setQueryTime(String queryTime) { this.queryTime = queryTime; }
}
