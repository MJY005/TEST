package com.example.lukedict;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 本地用户数据库，负责创建与升级。
 */
public class UserDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "test.db"; // 与外部可见的调试库名保持一致
    public static final int DB_VERSION = 2;
    public static final String TABLE_USERS = "user";

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "age INTEGER," +
                    "phone TEXT," +
                    "created_at INTEGER DEFAULT (strftime('%s','now'))" +
                    ");";

    public UserDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS +
                "(username,password,age,phone) VALUES('demo','123456',20,'18800000000');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}

