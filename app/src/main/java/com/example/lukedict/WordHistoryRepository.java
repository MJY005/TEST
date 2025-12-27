//package com.example.lukedict;
//<<<<<<< HEAD
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.text.TextUtils;
//
//=======
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 单词历史仓库：处理单词查询历史的增删改查
// */
//public class WordHistoryRepository {
//    private static WordHistoryRepository instance;
//    private final UserDatabaseHelper dbHelper;
//
//    private WordHistoryRepository(Context context) {
//<<<<<<< HEAD
//        this.dbHelper = new UserDatabaseHelper(context.getApplicationContext());
//    }
//
//=======
//        dbHelper = new UserDatabaseHelper(context.getApplicationContext());
//    }
//
//    // 单例模式
//>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
//    public static synchronized WordHistoryRepository getInstance(Context context) {
//        if (instance == null) {
//            instance = new WordHistoryRepository(context);
//        }
//        return instance;
//    }
//
//<<<<<<< HEAD
//    // 保存查询历史（需传入当前登录用户名）
//    public void saveHistory(String username, WordBean word) {
//        if (TextUtils.isEmpty(username) || word == null) return;
//
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("username", username);
//        values.put("word", word.getTitle());
//        values.put("translation", word.getTran());
//        values.put("phonetic", word.getPhonetic());
//
//        db.insert(UserDatabaseHelper.TABLE_WORD_HISTORY, null, values);
//        db.close();
//=======
//    // 1. 保存单词查询历史
//    public void saveWordHistory(String username, String word, String translation, String phonetic) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(UserDatabaseHelper.COLUMN_HISTORY_USERNAME, username);
//            values.put(UserDatabaseHelper.COLUMN_HISTORY_WORD, word);
//            values.put(UserDatabaseHelper.COLUMN_HISTORY_TRANSLATION, translation);
//            values.put(UserDatabaseHelper.COLUMN_HISTORY_PHONETIC, phonetic);
//            // 插入数据（无需指定query_time，数据库自动填充默认值）
//            db.insert(UserDatabaseHelper.TABLE_WORD_HISTORY, null, values);
//        } finally {
//            db.close();
//        }
//    }
//
//    // 2. 查询用户的单词历史记录（按查询时间倒序）
//    public List<WordHistory> getUserWordHistory(String username) {
//        List<WordHistory> historyList = new ArrayList<>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = null;
//        try {
//            cursor = db.query(
//                    UserDatabaseHelper.TABLE_WORD_HISTORY,
//                    null,
//                    UserDatabaseHelper.COLUMN_HISTORY_USERNAME + "=?",
//                    new String[]{username},
//                    null, null,
//                    UserDatabaseHelper.COLUMN_HISTORY_QUERY_TIME + " DESC"
//            );
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    WordHistory history = new WordHistory();
//                    history.setId(cursor.getLong(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_ID)));
//                    history.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_USERNAME)));
//                    history.setWord(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_WORD)));
//                    history.setTranslation(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_TRANSLATION)));
//                    history.setPhonetic(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_PHONETIC)));
//                    history.setQueryTime(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_QUERY_TIME)));
//                    historyList.add(history);
//                }
//            }
//            return historyList;
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            db.close();
//        }
//    }
//
//    // 3. 清空用户的单词历史记录
//    public boolean clearUserWordHistory(String username) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        try {
//            int rowsDeleted = db.delete(
//                    UserDatabaseHelper.TABLE_WORD_HISTORY,
//                    UserDatabaseHelper.COLUMN_HISTORY_USERNAME + "=?",
//                    new String[]{username}
//            );
//            return rowsDeleted > 0;
//        } finally {
//            db.close();
//        }
//    }
//
//    // 4. 根据单词删除单条历史记录
//    public boolean deleteSingleHistory(String username, String word) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        try {
//            int rowsDeleted = db.delete(
//                    UserDatabaseHelper.TABLE_WORD_HISTORY,
//                    UserDatabaseHelper.COLUMN_HISTORY_USERNAME + "=? AND " + UserDatabaseHelper.COLUMN_HISTORY_WORD + "=?",
//                    new String[]{username, word}
//            );
//            return rowsDeleted > 0;
//        } finally {
//            db.close();
//        }
//>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
//    }
//}

package com.example.lukedict;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单词历史记录仓库，负责单词查询历史的增删改查
 * 采用单例模式，所有数据库操作在子线程执行，通过回调返回结果
 */
public class WordHistoryRepository {
    private static final String TAG = "WordHistoryRepository";
    private static volatile WordHistoryRepository instance;
    private final WordHistoryDbHelper dbHelper;
    private final ExecutorService executor = Executors.newSingleThreadExecutor(); // 单线程池处理数据库操作

    // 私有构造函数，确保单例
    private WordHistoryRepository(Context context) {
        this.dbHelper = new WordHistoryDbHelper(context.getApplicationContext());
    }

    /**
     * 获取单例实例
     * @param context 上下文（建议使用Application Context避免内存泄漏）
     */
    public static WordHistoryRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (WordHistoryRepository.class) {
                if (instance == null) {
                    instance = new WordHistoryRepository(context);
                }
            }
        }
        return instance;
    }

    /**
     * 保存单词查询历史（异步执行）
     * @param username 关联的用户名
     * @param word 单词信息
     * @param callback 操作结果回调
     */
    public void saveHistory(String username, WordBean word, HistoryCallback<Boolean> callback) {
        if (TextUtils.isEmpty(username) || word == null || TextUtils.isEmpty(word.getTitle())) {
            if (callback != null) {
                callback.onResult(false);
            }
            return;
        }

        executor.execute(() -> {
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(WordHistoryDbHelper.COLUMN_USERNAME, username);
                values.put(WordHistoryDbHelper.COLUMN_WORD, word.getTitle());
                values.put(WordHistoryDbHelper.COLUMN_TRANSLATION, word.getTran());
                values.put(WordHistoryDbHelper.COLUMN_PHONETIC, word.getPhonetic());
                values.put(WordHistoryDbHelper.COLUMN_DEFINITION, word.getDesc());
                values.put(WordHistoryDbHelper.COLUMN_AUDIO_URL, word.getAudio());
                values.put(WordHistoryDbHelper.COLUMN_TIMESTAMP, System.currentTimeMillis()); // 记录时间戳

                // 先删除旧记录（避免重复）
                db.delete(
                        WordHistoryDbHelper.TABLE_HISTORY,
                        WordHistoryDbHelper.COLUMN_USERNAME + "=? AND " + WordHistoryDbHelper.COLUMN_WORD + "=?",
                        new String[]{username, word.getTitle()}
                );

                // 插入新记录
                long rowId = db.insert(WordHistoryDbHelper.TABLE_HISTORY, null, values);
                if (callback != null) {
                    callback.onResult(rowId != -1);
                }
            } catch (Exception e) {
                Log.e(TAG, "保存历史记录失败", e);
                if (callback != null) {
                    callback.onResult(false);
                }
            } finally {
                if (db != null) {
                    db.close();
                }
            }
        });
    }

    /**
     * 查询用户的单词历史记录（异步执行，按时间倒序）
     * @param username 用户名
     * @param callback 结果回调（返回历史列表）
     */
    public void queryHistory(String username, HistoryCallback<List<WordBean>> callback) {
        if (TextUtils.isEmpty(username)) {
            if (callback != null) {
                callback.onResult(new ArrayList<>());
            }
            return;
        }

        executor.execute(() -> {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            List<WordBean> historyList = new ArrayList<>();
            try {
                db = dbHelper.getReadableDatabase();
                cursor = db.query(
                        WordHistoryDbHelper.TABLE_HISTORY,
                        null, // 查询所有列
                        WordHistoryDbHelper.COLUMN_USERNAME + "=?",
                        new String[]{username},
                        null,
                        null,
                        WordHistoryDbHelper.COLUMN_TIMESTAMP + " DESC" // 按时间倒序
                );

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        WordBean word = parseWordFromCursor(cursor);
                        historyList.add(word);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e(TAG, "查询历史记录失败", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                if (db != null) {
                    db.close();
                }
                if (callback != null) {
                    callback.onResult(historyList);
                }
            }
        });
    }

    /**
     * 删除单条历史记录
     * @param username 用户名
     * @param word 单词
     * @param callback 操作结果回调
     */
    public void deleteHistory(String username, String word, HistoryCallback<Boolean> callback) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(word)) {
            if (callback != null) {
                callback.onResult(false);
            }
            return;
        }

        executor.execute(() -> {
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                int rowsDeleted = db.delete(
                        WordHistoryDbHelper.TABLE_HISTORY,
                        WordHistoryDbHelper.COLUMN_USERNAME + "=? AND " + WordHistoryDbHelper.COLUMN_WORD + "=?",
                        new String[]{username, word}
                );
                if (callback != null) {
                    callback.onResult(rowsDeleted > 0);
                }
            } catch (Exception e) {
                Log.e(TAG, "删除历史记录失败", e);
                if (callback != null) {
                    callback.onResult(false);
                }
            } finally {
                if (db != null) {
                    db.close();
                }
            }
        });
    }

    /**
     * 清空用户的所有历史记录
     * @param username 用户名
     * @param callback 操作结果回调
     */
    public void clearAllHistory(String username, HistoryCallback<Boolean> callback) {
        if (TextUtils.isEmpty(username)) {
            if (callback != null) {
                callback.onResult(false);
            }
            return;
        }

        executor.execute(() -> {
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                int rowsDeleted = db.delete(
                        WordHistoryDbHelper.TABLE_HISTORY,
                        WordHistoryDbHelper.COLUMN_USERNAME + "=?",
                        new String[]{username}
                );
                if (callback != null) {
                    callback.onResult(rowsDeleted > 0);
                }
            } catch (Exception e) {
                Log.e(TAG, "清空历史记录失败", e);
                if (callback != null) {
                    callback.onResult(false);
                }
            } finally {
                if (db != null) {
                    db.close();
                }
            }
        });
    }

    /**
     * 从游标解析WordBean对象
     */
    private WordBean parseWordFromCursor(Cursor cursor) {
        WordBean word = new WordBean();
        word.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(WordHistoryDbHelper.COLUMN_WORD)));
        word.setTran(cursor.getString(cursor.getColumnIndexOrThrow(WordHistoryDbHelper.COLUMN_TRANSLATION)));
        word.setPhonetic(cursor.getString(cursor.getColumnIndexOrThrow(WordHistoryDbHelper.COLUMN_PHONETIC)));
        word.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(WordHistoryDbHelper.COLUMN_DEFINITION)));
        word.setAudioUrl(cursor.getString(cursor.getColumnIndexOrThrow(WordHistoryDbHelper.COLUMN_AUDIO_URL)));
        // 可根据需要添加时间戳字段到WordBean
        return word;
    }

    /**
     * 历史操作回调接口
     */
    public interface HistoryCallback<T> {
        void onResult(T result);
    }

    /**
     * 数据库帮助类，负责创建和升级历史记录表
     */
    private static class WordHistoryDbHelper extends android.database.sqlite.SQLiteOpenHelper {
        private static final String DB_NAME = "word_history.db";
        private static final int DB_VERSION = 1;
        static final String TABLE_HISTORY = "word_history";
        // 表字段
        static final String COLUMN_ID = "_id";
        static final String COLUMN_USERNAME = "username"; // 关联用户名
        static final String COLUMN_WORD = "word"; // 单词
        static final String COLUMN_TRANSLATION = "translation"; // 翻译
        static final String COLUMN_PHONETIC = "phonetic"; // 音标
        static final String COLUMN_DEFINITION = "definition"; // 释义
        static final String COLUMN_AUDIO_URL = "audio_url"; // 发音URL
        static final String COLUMN_TIMESTAMP = "timestamp"; // 记录时间戳

        public WordHistoryDbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // 创建历史记录表
            String createTableSql = "CREATE TABLE " + TABLE_HISTORY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_WORD + " TEXT NOT NULL, " +
                    COLUMN_TRANSLATION + " TEXT, " +
                    COLUMN_PHONETIC + " TEXT, " +
                    COLUMN_DEFINITION + " TEXT, " +
                    COLUMN_AUDIO_URL + " TEXT, " +
                    COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                    // 联合唯一约束：同一用户的同一单词不重复记录
                    "UNIQUE (" + COLUMN_USERNAME + ", " + COLUMN_WORD + ") ON CONFLICT REPLACE" +
                    ")";
            db.execSQL(createTableSql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // 数据库升级逻辑（如需升级，在此处理）
            if (oldVersion < newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
                onCreate(db);
            }
        }
    }
}