package com.example.lukedict;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

<<<<<<< HEAD
public class UserDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "test.db";
    public static final int DB_VERSION = 1;
    // 统一表名为"user"，与DDL一致
    public static final String TABLE_USERS = "user";
    public static final String TABLE_WORD_HISTORY = "word_history";
=======
/**
 * 管理数据库创建
 */
public class UserDatabaseHelper extends SQLiteOpenHelper {
    // 数据库名称（与DBeaver中的test.db对应，可修改为user_db.db）
    public static final String DB_NAME = "test.db";
    // 数据库版本号（新增表/修改结构时需递增）
    private static final int DB_VERSION = 2;

    // 表名常量
    public static final String TABLE_USERS = "user";
    public static final String TABLE_WORD_HISTORY = "word_history";

    // -------------------------- 用户表字段常量 --------------------------
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_AGE = "age";
    public static final String COLUMN_USER_PHONE = "phone";
    public static final String COLUMN_USER_CREATE_TIME = "create_time";

    // -------------------------- 单词历史表字段常量 --------------------------
    public static final String COLUMN_HISTORY_ID = "_id";
    public static final String COLUMN_HISTORY_USERNAME = "username";
    public static final String COLUMN_HISTORY_WORD = "word";
    public static final String COLUMN_HISTORY_TRANSLATION = "translation";
    public static final String COLUMN_HISTORY_PHONETIC = "phonetic";
    public static final String COLUMN_HISTORY_QUERY_TIME = "query_time";
>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471

    public UserDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
<<<<<<< HEAD
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
=======
        // 1. 创建用户表（与DDL完全一致）
        String createUserTableSql = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_USERNAME + " TEXT UNIQUE NOT NULL," +
                COLUMN_USER_PASSWORD + " TEXT NOT NULL," +
                COLUMN_USER_AGE + " INTEGER," +
                COLUMN_USER_PHONE + " TEXT," +
                COLUMN_USER_CREATE_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(createUserTableSql);

        // 给用户表创建索引
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_user_username ON " + TABLE_USERS + "(" + COLUMN_USER_USERNAME + ");");

        // 2. 创建单词历史表（与DDL完全一致）
        String createHistoryTableSql = "CREATE TABLE IF NOT EXISTS " + TABLE_WORD_HISTORY + " (" +
                COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_HISTORY_USERNAME + " TEXT NOT NULL," +
                COLUMN_HISTORY_WORD + " TEXT NOT NULL," +
                COLUMN_HISTORY_TRANSLATION + " TEXT," +
                COLUMN_HISTORY_PHONETIC + " TEXT," +
                COLUMN_HISTORY_QUERY_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(createHistoryTableSql);

        // 3. 开启外键约束并添加外键关联
        db.execSQL("PRAGMA foreign_keys = ON;");
        String addForeignKeySql = "ALTER TABLE " + TABLE_WORD_HISTORY +
                " ADD CONSTRAINT fk_history_username FOREIGN KEY (" + COLUMN_HISTORY_USERNAME + ")" +
                " REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_USERNAME + ") ON DELETE CASCADE;";
        db.execSQL(addForeignKeySql);

        // 4. 给历史表创建索引
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_history_username_time ON " + TABLE_WORD_HISTORY +
                "(" + COLUMN_HISTORY_USERNAME + ", " + COLUMN_HISTORY_QUERY_TIME + " DESC);");
>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
<<<<<<< HEAD
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
=======
        // 版本升级逻辑（后续修改表结构时使用）
        if (oldVersion < 2) {
            // 版本1→2：新增单词历史表（与onCreate中的历史表SQL一致）
            String createHistoryTableSql = "CREATE TABLE IF NOT EXISTS " + TABLE_WORD_HISTORY + " (" +
                    COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_HISTORY_USERNAME + " TEXT NOT NULL," +
                    COLUMN_HISTORY_WORD + " TEXT NOT NULL," +
                    COLUMN_HISTORY_TRANSLATION + " TEXT," +
                    COLUMN_HISTORY_PHONETIC + " TEXT," +
                    COLUMN_HISTORY_QUERY_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";
            db.execSQL(createHistoryTableSql);

            // 新增历史表的外键和索引
            db.execSQL("PRAGMA foreign_keys = ON;");
            String addForeignKeySql = "ALTER TABLE " + TABLE_WORD_HISTORY +
                    " ADD CONSTRAINT fk_history_username FOREIGN KEY (" + COLUMN_HISTORY_USERNAME + ")" +
                    " REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_USERNAME + ") ON DELETE CASCADE;";
            db.execSQL(addForeignKeySql);
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_history_username_time ON " + TABLE_WORD_HISTORY +
                    "(" + COLUMN_HISTORY_USERNAME + ", " + COLUMN_HISTORY_QUERY_TIME + " DESC);");
        }
        // 后续版本升级：oldVersion < 3 → 新增字段/表等
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // 每次打开数据库都开启外键约束
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
    }
}