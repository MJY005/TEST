package com.example.lukedict;

import java.util.List;

/**
 * Merriam-Webster Dictionary API 响应模型
 * 根据实际API响应格式调整
 */
public class DictionaryResponse {
    private String id;
    private Meta meta; // meta是对象，不是字符串
    private Hwi hwi; // 词头信息（单个对象，不是列表）
    private List<Def> def; // 定义列表
    private List<String> shortdef; // 简短定义

    public DictionaryResponse() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Hwi getHwi() {
        return hwi;
    }

    public void setHwi(Hwi hwi) {
        this.hwi = hwi;
    }

    public List<Def> getDef() {
        return def;
    }

    public void setDef(List<Def> def) {
        this.def = def;
    }

    public List<String> getShortdef() {
        return shortdef;
    }

    public void setShortdef(List<String> shortdef) {
        this.shortdef = shortdef;
    }

    // Meta信息（元数据）
    public static class Meta {
        private String id;
        private String uuid;
        private String sort;

        public Meta() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }

    // 词头信息（Headword Information）
    public static class Hwi {
        private String hw; // 词头
        private List<Prs> prs; // 发音信息

        public Hwi() {}

        public String getHw() {
            return hw;
        }

        public void setHw(String hw) {
            this.hw = hw;
        }

        public List<Prs> getPrs() {
            return prs;
        }

        public void setPrs(List<Prs> prs) {
            this.prs = prs;
        }
    }

    // 发音信息（Pronunciation）
    public static class Prs {
        private String mw; // 音标
        private Sound sound; // 发音文件信息

        public Prs() {}

        public String getMw() {
            return mw;
        }

        public void setMw(String mw) {
            this.mw = mw;
        }

        public Sound getSound() {
            return sound;
        }

        public void setSound(Sound sound) {
            this.sound = sound;
        }
    }

    // 发音文件信息
    public static class Sound {
        private String audio; // 音频文件名

        public Sound() {}

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }
    }

    // 定义信息
    public static class Def {
        private List<List<Object>> sseq; // 词义序列（直接是List<List<Object>>）

        public Def() {}

        public List<List<Object>> getSseq() {
            return sseq;
        }

        public void setSseq(List<List<Object>> sseq) {
            this.sseq = sseq;
        }
    }
}
