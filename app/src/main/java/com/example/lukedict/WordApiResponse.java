package com.example.lukedict;

import java.util.List;

/**
 * Free Dictionary API 对应的数据结构，用于 Retrofit + Gson 解析。
 * 仅包含当前功能所需字段，未使用字段可按需扩展。
 */
public class WordApiResponse {
    private String word;
    private List<Phonetic> phonetics;
    private List<Meaning> meanings;

    // 空构造函数（Gson需要）
    public WordApiResponse() {}

    public String getWord() {
        return word;
    }

    public List<Phonetic> getPhonetics() {
        return phonetics;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public static class Phonetic {
        private String text;
        private String audio;

        // 空构造函数（Gson需要）
        public Phonetic() {}

        public String getText() {
            return text;
        }

        public String getAudio() {
            return audio;
        }
    }

    public static class Meaning {
        private String partOfSpeech;
        private List<Definition> definitions;

        // 空构造函数（Gson需要）
        public Meaning() {}

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public List<Definition> getDefinitions() {
            return definitions;
        }
    }

    public static class Definition {
        private String definition;
        private String example;

        // 空构造函数（Gson需要）
        public Definition() {}

        public String getDefinition() {
            return definition;
        }

        public String getExample() {
            return example;
        }
    }
}

