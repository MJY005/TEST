package com.example.lukedict;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WordLocalDataSource {
    private WordDatabaseHelper dbHelper;

    public WordLocalDataSource(Context context) {
        dbHelper = new WordDatabaseHelper(context);
    }

    // 插入单词
    public void insertWord(Word word) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word", word.getWord());
        values.put("translation", word.getTranslation());
        values.put("query_time", word.getQueryTime());
        db.insert(WordDatabaseHelper.TABLE_NAME, null, values);
        db.close();
    }

    // 查询单词
    public Word queryWord(String word) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(WordDatabaseHelper.TABLE_NAME,
                null, "word = ?", new String[]{word},
                null, null, null);

        Word result = null;
        if (cursor.moveToFirst()) {
            String translation = cursor.getString(cursor.getColumnIndex("translation"));
            long queryTime = cursor.getLong(cursor.getColumnIndex("query_time"));
            result = new Word(word, translation);
            result.setQueryTime(queryTime);
        }
        cursor.close();
        db.close();
        return result;
    }

    // 关闭数据库连接
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    // 其他操作：更新、删除、查询历史记录等

}