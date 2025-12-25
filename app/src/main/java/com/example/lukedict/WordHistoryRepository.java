package com.example.lukedict;
<<<<<<< HEAD

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

=======
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * 单词历史仓库：处理单词查询历史的增删改查
 */
>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
public class WordHistoryRepository {
    private static WordHistoryRepository instance;
    private final UserDatabaseHelper dbHelper;

    private WordHistoryRepository(Context context) {
<<<<<<< HEAD
        this.dbHelper = new UserDatabaseHelper(context.getApplicationContext());
    }

=======
        dbHelper = new UserDatabaseHelper(context.getApplicationContext());
    }

    // 单例模式
>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
    public static synchronized WordHistoryRepository getInstance(Context context) {
        if (instance == null) {
            instance = new WordHistoryRepository(context);
        }
        return instance;
    }

<<<<<<< HEAD
    // 保存查询历史（需传入当前登录用户名）
    public void saveHistory(String username, WordBean word) {
        if (TextUtils.isEmpty(username) || word == null) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("word", word.getTitle());
        values.put("translation", word.getTran());
        values.put("phonetic", word.getPhonetic());

        db.insert(UserDatabaseHelper.TABLE_WORD_HISTORY, null, values);
        db.close();
=======
    // 1. 保存单词查询历史
    public void saveWordHistory(String username, String word, String translation, String phonetic) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(UserDatabaseHelper.COLUMN_HISTORY_USERNAME, username);
            values.put(UserDatabaseHelper.COLUMN_HISTORY_WORD, word);
            values.put(UserDatabaseHelper.COLUMN_HISTORY_TRANSLATION, translation);
            values.put(UserDatabaseHelper.COLUMN_HISTORY_PHONETIC, phonetic);
            // 插入数据（无需指定query_time，数据库自动填充默认值）
            db.insert(UserDatabaseHelper.TABLE_WORD_HISTORY, null, values);
        } finally {
            db.close();
        }
    }

    // 2. 查询用户的单词历史记录（按查询时间倒序）
    public List<WordHistory> getUserWordHistory(String username) {
        List<WordHistory> historyList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    UserDatabaseHelper.TABLE_WORD_HISTORY,
                    null,
                    UserDatabaseHelper.COLUMN_HISTORY_USERNAME + "=?",
                    new String[]{username},
                    null, null,
                    UserDatabaseHelper.COLUMN_HISTORY_QUERY_TIME + " DESC"
            );
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    WordHistory history = new WordHistory();
                    history.setId(cursor.getLong(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_ID)));
                    history.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_USERNAME)));
                    history.setWord(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_WORD)));
                    history.setTranslation(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_TRANSLATION)));
                    history.setPhonetic(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_PHONETIC)));
                    history.setQueryTime(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HISTORY_QUERY_TIME)));
                    historyList.add(history);
                }
            }
            return historyList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    // 3. 清空用户的单词历史记录
    public boolean clearUserWordHistory(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int rowsDeleted = db.delete(
                    UserDatabaseHelper.TABLE_WORD_HISTORY,
                    UserDatabaseHelper.COLUMN_HISTORY_USERNAME + "=?",
                    new String[]{username}
            );
            return rowsDeleted > 0;
        } finally {
            db.close();
        }
    }

    // 4. 根据单词删除单条历史记录
    public boolean deleteSingleHistory(String username, String word) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int rowsDeleted = db.delete(
                    UserDatabaseHelper.TABLE_WORD_HISTORY,
                    UserDatabaseHelper.COLUMN_HISTORY_USERNAME + "=? AND " + UserDatabaseHelper.COLUMN_HISTORY_WORD + "=?",
                    new String[]{username, word}
            );
            return rowsDeleted > 0;
        } finally {
            db.close();
        }
>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
    }
}