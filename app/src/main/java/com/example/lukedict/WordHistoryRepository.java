package com.example.lukedict;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class WordHistoryRepository {
    private static WordHistoryRepository instance;
    private final UserDatabaseHelper dbHelper;

    private WordHistoryRepository(Context context) {
        this.dbHelper = new UserDatabaseHelper(context.getApplicationContext());
    }

    public static synchronized WordHistoryRepository getInstance(Context context) {
        if (instance == null) {
            instance = new WordHistoryRepository(context);
        }
        return instance;
    }

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
    }
}