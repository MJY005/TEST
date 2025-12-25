package com.example.lukedict;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "test.db";
    public static final int DB_VERSION = 1;
    // 统一表名为"user"，与DDL一致
    public static final String TABLE_USERS = "user";
    public static final String TABLE_WORD_HISTORY = "word_history";

    public UserDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行你的DDL语句
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "age INTEGER, " +
                "phone TEXT, " +
                "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE INDEX IF NOT EXISTS idx_user_username ON " + TABLE_USERS + "(username)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_WORD_HISTORY + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "word TEXT NOT NULL, " +
                "translation TEXT, " +
                "phonetic TEXT, " +
                "query_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        // 开启外键约束
        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL("ALTER TABLE " + TABLE_WORD_HISTORY +
                " ADD CONSTRAINT fk_history_username FOREIGN KEY (username) " +
                "REFERENCES " + TABLE_USERS + "(username) ON DELETE CASCADE");

        db.execSQL("CREATE INDEX IF NOT EXISTS idx_history_username_time ON " +
                TABLE_WORD_HISTORY + "(username, query_time DESC)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}