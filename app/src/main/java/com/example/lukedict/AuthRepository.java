package com.example.lukedict;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * 封装登录/注册相关的数据库操作。
 * 采用单例模式，确保数据库访问的一致性。
 */
public class AuthRepository {
    private static AuthRepository instance;
    private final UserDatabaseHelper dbHelper;
    private final Context context;

    /**
     * 私有构造函数，单例模式
     */
    private AuthRepository(Context context) {
        this.context = context.getApplicationContext();
        this.dbHelper = new UserDatabaseHelper(this.context);
    }

    /**
     * 获取单例实例
     */
    public static synchronized AuthRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AuthRepository(context);
        }
        return instance;
    }

    /**
     * 注册用户，用户名唯一。
     */
    public synchronized boolean register(User user) {
        if (user == null || TextUtils.isEmpty(user.getUsername()) || TextUtils.isEmpty(user.getPassword())) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        if (user.getAge() != null) {
            values.put("age", user.getAge());
        }
        if (!TextUtils.isEmpty(user.getPhone())) {
            values.put("phone", user.getPhone());
        }
        try {
            long rowId = db.insertOrThrow(UserDatabaseHelper.TABLE_USERS, null, values);
            user.setId(rowId);
            return rowId != -1;
        } catch (SQLException e) {
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * 校验用户名密码。
     */
    public boolean login(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                UserDatabaseHelper.TABLE_USERS,
                new String[]{"_id"},
                "username=? AND password=?",
                new String[]{username, password},
                null,
                null,
                null
        );
        boolean success = cursor.moveToFirst();
        cursor.close();
        db.close();
        return success;
    }

    /**
     * 返回数据库物理路径，便于调试查看。
     */
    public String getDbPath(Context context) {
        return context.getDatabasePath(UserDatabaseHelper.DB_NAME).getAbsolutePath();
    }

    /**
     * 是否存在用户名。
     */
    public boolean userExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                UserDatabaseHelper.TABLE_USERS,
                new String[]{"_id"},
                "username=?",
                new String[]{username},
                null,
                null,
                null
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public User getUser(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                UserDatabaseHelper.TABLE_USERS,
                new String[]{"_id","username","password","age","phone"},
                "username=?",
                new String[]{username},
                null,
                null,
                null
        );
        User user = null;
        if (cursor.moveToFirst()) {
            user = parseUser(cursor);
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean updateUser(User user) {
        if (user == null || TextUtils.isEmpty(user.getUsername())) return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(user.getPassword())) {
            values.put("password", user.getPassword());
        }
        if (user.getAge() != null) {
            values.put("age", user.getAge());
        }
        if (!TextUtils.isEmpty(user.getPhone())) {
            values.put("phone", user.getPhone());
        }
        int rows = db.update(UserDatabaseHelper.TABLE_USERS, values, "username=?", new String[]{user.getUsername()});
        db.close();
        return rows > 0;
    }

    public boolean deleteUser(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(UserDatabaseHelper.TABLE_USERS, "username=?", new String[]{username});
        db.close();
        return rows > 0;
    }

    public Cursor listUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(UserDatabaseHelper.TABLE_USERS,
                new String[]{"_id","username","age","phone","created_at"},
                null,null,null,null,"_id DESC");
    }

    private User parseUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
        if (!cursor.isNull(cursor.getColumnIndexOrThrow("age"))) {
            user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("age")));
        }
        user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
        return user;
    }
}

