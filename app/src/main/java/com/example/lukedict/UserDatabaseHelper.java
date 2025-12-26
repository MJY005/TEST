package com.example.lukedict;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "test.db";
    public static final int DB_VERSION = 2; // 修复：与数据库当前版本一致
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
                "created_at INTEGER DEFAULT (strftime('%s','now')))");

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
        // 处理数据库版本升级
        if (oldVersion < 2) {
            // 从版本1升级到版本2：添加created_at字段（如果不存在）
            try {
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN created_at INTEGER DEFAULT (strftime('%s','now'))");
            } catch (Exception e) {
                // 字段可能已存在，忽略错误
            }
        }
        // 如果未来需要更多版本升级，在这里添加
    }
    
//    @Override
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // 处理数据库版本降级（通常不推荐，但可以处理）
//        // 如果确实需要降级，可以删除表重新创建（会丢失数据）
////        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD_HISTORY);
////        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
////        onCreate(db);
//    }
}