package com.example.lukedict;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据仓库：处理用户表的增删改查
 */
public class AuthRepository {
    private static AuthRepository instance;
    private final UserDatabaseHelper dbHelper;

    private AuthRepository(Context context) {
        dbHelper = new UserDatabaseHelper(context.getApplicationContext());
    }

    // 单例模式
    public static synchronized AuthRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AuthRepository(context);
        }
        return instance;
    }

    // 1. 注册用户（插入新用户）
    public boolean registerUser(String username, String password, Integer age, String phone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(UserDatabaseHelper.COLUMN_USER_USERNAME, username);
            values.put(UserDatabaseHelper.COLUMN_USER_PASSWORD, password);
            if (age != null) {
                values.put(UserDatabaseHelper.COLUMN_USER_AGE, age);
            }
            if (phone != null) {
                values.put(UserDatabaseHelper.COLUMN_USER_PHONE, phone);
            }
            // 插入成功返回行ID（>0），失败返回-1（用户名重复）
<<<<<<< HEAD
            long rowId = db.insertOrThrow(UserDatabaseHelper.TABLE_USERS, null, values);

            return rowId != -1;
        } finally {
            db.close();
        }

    }

    // 2. 登录验证（根据用户名和密码查询）
    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
             cursor = db.query(
                    UserDatabaseHelper.TABLE_USERS,
                    new String[]{"_id"},
                    "username=? AND password=?",
                    new String[]{username, password},
                    null, null, null
            );
            // 存在匹配记录则登录成功
            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    // 3. 根据用户名查询用户信息
    public User getUser(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    UserDatabaseHelper.TABLE_USERS,
                    null, // 查询所有字段
                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=?",
                    new String[]{username},
                    null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                User user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PASSWORD)));
                if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE))) {
                    user.setAge(cursor.getInt(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE)));
                }
                if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE))) {
                    user.setPhone(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE)));
                }
                return user;
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    // 4. 修改用户资料（年龄/手机号）
    public boolean updateUserInfo(String username, Integer age, String phone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            if (age != null) {
                values.put(UserDatabaseHelper.COLUMN_USER_AGE, age);
            }
            if (phone != null) {
                values.put(UserDatabaseHelper.COLUMN_USER_PHONE, phone);
            }
            // 执行更新
            int rowsAffected = db.update(
                    UserDatabaseHelper.TABLE_USERS,
                    values,
                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=?",
                    new String[]{username}
            );
            return rowsAffected > 0;
=======
            long rowId = db.insert(UserDatabaseHelper.TABLE_USERS, null, values);
            return rowId != -1;
>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
        } finally {
            db.close();
        }
    }

<<<<<<< HEAD
    // 5. 修改用户密码
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(UserDatabaseHelper.COLUMN_USER_PASSWORD, newPassword);
=======
    // 2. 登录验证（根据用户名和密码查询）
    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    UserDatabaseHelper.TABLE_USERS,
                    new String[]{UserDatabaseHelper.COLUMN_USER_ID},
                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=? AND " + UserDatabaseHelper.COLUMN_USER_PASSWORD + "=?",
                    new String[]{username, password},
                    null, null, null
            );
            // 存在匹配记录则登录成功
            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    // 3. 根据用户名查询用户信息
    public User getUser(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    UserDatabaseHelper.TABLE_USERS,
                    null, // 查询所有字段
                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=?",
                    new String[]{username},
                    null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                User user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PASSWORD)));
                if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE))) {
                    user.setAge(cursor.getInt(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE)));
                }
                if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE))) {
                    user.setPhone(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE)));
                }
                return user;
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    // 4. 修改用户资料（年龄/手机号）
    public boolean updateUserInfo(String username, Integer age, String phone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            if (age != null) {
                values.put(UserDatabaseHelper.COLUMN_USER_AGE, age);
            }
            if (phone != null) {
                values.put(UserDatabaseHelper.COLUMN_USER_PHONE, phone);
            }
            // 执行更新
>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
            int rowsAffected = db.update(
                    UserDatabaseHelper.TABLE_USERS,
                    values,
                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=?",
                    new String[]{username}
            );
            return rowsAffected > 0;
        } finally {
            db.close();
        }
    }

<<<<<<< HEAD
=======
    // 5. 修改用户密码
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(UserDatabaseHelper.COLUMN_USER_PASSWORD, newPassword);
            int rowsAffected = db.update(
                    UserDatabaseHelper.TABLE_USERS,
                    values,
                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=?",
                    new String[]{username}
            );
            return rowsAffected > 0;
        } finally {
            db.close();
        }
    }

>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
    // 6. 查询所有用户（可选，用于管理员功能）
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    UserDatabaseHelper.TABLE_USERS,
                    null,
                    null, null, null, null,
                    UserDatabaseHelper.COLUMN_USER_CREATE_TIME + " DESC"
            );
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    User user = new User();
                    user.setId(cursor.getLong(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_ID)));
                    user.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_USERNAME)));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PASSWORD)));
                    if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE))) {
                        user.setAge(cursor.getInt(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE)));
                    }
                    if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE))) {
                        user.setPhone(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE)));
                    }
                    userList.add(user);
                }
            }
            return userList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
}