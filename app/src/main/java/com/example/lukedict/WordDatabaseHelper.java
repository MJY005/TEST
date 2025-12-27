package com.example.lukedict;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "words.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "word";

    // 表结构：id, 单词, 中文翻译, 查询时间
    public WordDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "word TEXT NOT NULL, " +
                "translation TEXT NOT NULL, " +
                "query_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}