package com.example.lukedict;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用户认证仓库：封装SQLite增删改查，全异步操作，密码加密存储
 */
public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private static final String ENCRYPT_ALGORITHM = "SHA-256";
    private static AuthRepository instance;
    private final UserDatabaseHelper dbHelper;
    private final Context appContext;
    private final ExecutorService backgroundExecutor;

    // 异步回调接口（统一规范）
    public interface AuthCallback<T> {
        void onResult(T result);
        default void onError(Throwable e) {}
        default void onLoading(boolean isLoading) {}
    }

    // 私有构造函数（单例模式）
    private AuthRepository(Context context) {
        this.appContext = context.getApplicationContext(); // 避免内存泄漏
        this.dbHelper = new UserDatabaseHelper(this.appContext);
        this.backgroundExecutor = Executors.newSingleThreadExecutor(); // 串行执行数据库操作
    }

    // 获取单例
    public static synchronized AuthRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AuthRepository(context);
        }
        return instance;
    }

    // 1. 注册用户（异步）
    public void register(User user, AuthCallback<Boolean> callback) {
        if (user == null || TextUtils.isEmpty(user.getUsername()) || TextUtils.isEmpty(user.getPassword())) {
            callback.onResult(false);
            return;
        }

        backgroundExecutor.execute(() -> {
            callback.onLoading(true);
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                // 检查用户名是否存在
                if (userExistsSync(user.getUsername())) {
                    callback.onResult(false);
                    return;
                }

                // 密码加密存储
                ContentValues values = new ContentValues();
                values.put("username", user.getUsername());
                values.put("password", encryptPassword(user.getPassword()));
                values.put("age", user.getAge());
                values.put("phone", user.getPhone());
                values.put("created_at", System.currentTimeMillis());

                long rowId = db.insertOrThrow(UserDatabaseHelper.TABLE_USERS, null, values);
                callback.onResult(rowId != -1);
            } catch (SQLException e) {
                Log.e(TAG, "注册失败", e);
                callback.onError(e);
                callback.onResult(false);
            } finally {
                if (db != null) db.close();
                callback.onLoading(false);
            }
        });
    }

    // 2. 登录验证（异步）
    public void login(String username, String password, AuthCallback<Boolean> callback) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            callback.onResult(false);
            return;
        }

        backgroundExecutor.execute(() -> {
            callback.onLoading(true);
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = dbHelper.getReadableDatabase();
                String encryptedPwd = encryptPassword(password);
                cursor = db.query(
                        UserDatabaseHelper.TABLE_USERS,
                        new String[]{"_id"},
                        "username=? AND password=?",
                        new String[]{username, encryptedPwd},
                        null, null, null
                );
                callback.onResult(cursor.moveToFirst());
            } catch (Exception e) {
                Log.e(TAG, "登录失败", e);
                callback.onError(e);
                callback.onResult(false);
            } finally {
                if (cursor != null) cursor.close();
                if (db != null) db.close();
                callback.onLoading(false);
            }
        });
    }

    // 3. 检查用户名是否存在（异步）
    public void userExists(String username, AuthCallback<Boolean> callback) {
        if (TextUtils.isEmpty(username)) {
            callback.onResult(false);
            return;
        }

        backgroundExecutor.execute(() -> {
            callback.onResult(userExistsSync(username));
        });
    }

    // 4. 获取用户信息（异步）
    public void getUser(String username, AuthCallback<User> callback) {
        if (TextUtils.isEmpty(username)) {
            callback.onResult(null);
            return;
        }

        backgroundExecutor.execute(() -> {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = dbHelper.getReadableDatabase();
                cursor = db.query(
                        UserDatabaseHelper.TABLE_USERS,
                        new String[]{"_id", "username", "age", "phone", "created_at"},
                        "username=?",
                        new String[]{username},
                        null, null, null
                );

                if (cursor.moveToFirst()) {
                    callback.onResult(parseUser(cursor));
                } else {
                    callback.onResult(null);
                }
            } catch (SQLException e) {
                Log.e(TAG, "获取用户信息失败", e);
                callback.onError(e);
                callback.onResult(null);
            } finally {
                if (cursor != null) cursor.close();
                if (db != null) db.close();
            }
        });
    }

    // 5. 更新用户信息（异步）
    public void updateUser(User user, AuthCallback<Boolean> callback) {
        if (user == null || TextUtils.isEmpty(user.getUsername())) {
            callback.onResult(false);
            return;
        }

        backgroundExecutor.execute(() -> {
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                if (!TextUtils.isEmpty(user.getPassword())) {
                    values.put("password", encryptPassword(user.getPassword()));
                }
                if (user.getAge() != null) values.put("age", user.getAge());
                if (!TextUtils.isEmpty(user.getPhone())) values.put("phone", user.getPhone());

                int rows = db.update(
                        UserDatabaseHelper.TABLE_USERS,
                        values,
                        "username=?",
                        new String[]{user.getUsername()}
                );
                callback.onResult(rows > 0);
            } catch (SQLException e) {
                Log.e(TAG, "更新用户失败", e);
                callback.onError(e);
                callback.onResult(false);
            } finally {
                if (db != null) db.close();
            }
        });
    }

    // 6. 删除用户（异步）
    public void deleteUser(String username, AuthCallback<Boolean> callback) {
        if (TextUtils.isEmpty(username)) {
            callback.onResult(false);
            return;
        }

        backgroundExecutor.execute(() -> {
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                int rows = db.delete(
                        UserDatabaseHelper.TABLE_USERS,
                        "username=?",
                        new String[]{username}
                );
                callback.onResult(rows > 0);
            } catch (SQLException e) {
                Log.e(TAG, "删除用户失败", e);
                callback.onError(e);
                callback.onResult(false);
            } finally {
                if (db != null) db.close();
            }
        });
    }

    // 7. 获取所有用户（异步，返回List避免Cursor泄漏）
    public void listUsers(AuthCallback<List<User>> callback) {
        backgroundExecutor.execute(() -> {
            List<User> userList = new ArrayList<>();
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = dbHelper.getReadableDatabase();
                cursor = db.query(
                        UserDatabaseHelper.TABLE_USERS,
                        new String[]{"_id", "username", "age", "phone", "created_at"},
                        null, null, null, null, "_id DESC"
                );

                while (cursor.moveToNext()) {
                    userList.add(parseUser(cursor));
                }
                callback.onResult(userList);
            } catch (SQLException e) {
                Log.e(TAG, "获取用户列表失败", e);
                callback.onError(e);
                callback.onResult(null);
            } finally {
                if (cursor != null) cursor.close();
                if (db != null) db.close();
            }
        });
    }

    // 8. 获取数据库路径（无参数，解决编译错误）
    public String getDbPath() {
        return appContext.getDatabasePath(UserDatabaseHelper.DB_NAME).getAbsolutePath();
    }

    // 内部工具方法：同步检查用户是否存在
    private boolean userExistsSync(String username) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(
                    UserDatabaseHelper.TABLE_USERS,
                    new String[]{"_id"},
                    "username=?",
                    new String[]{username},
                    null, null, null
            );
            return cursor.moveToFirst();
        } catch (SQLException e) {
            Log.e(TAG, "检查用户存在性失败", e);
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    // 内部工具方法：密码加密
    private String encryptPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ENCRYPT_ALGORITHM);
            byte[] hash = digest.digest(rawPassword.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String hexStr = Integer.toHexString(0xff & b);
                if (hexStr.length() == 1) hex.append('0');
                hex.append(hexStr);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "密码加密失败", e);
            return rawPassword; // 降级处理
        }
    }

    // 内部工具方法：解析Cursor到User对象
    @SuppressLint("Range")
    private User parseUser(Cursor cursor) {
        User user = new User();
        try {
            user.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            if (!cursor.isNull(cursor.getColumnIndex("age"))) {
                user.setAge(cursor.getInt(cursor.getColumnIndex("age")));
            }
            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            user.setCreatedAt(cursor.getLong(cursor.getColumnIndex("created_at")));
        } catch (Exception e) {
            Log.e(TAG, "解析用户失败", e);
        }
        return user;
    }

    // 释放资源
    public void release() {
        backgroundExecutor.shutdown();
        dbHelper.close();
        instance = null;
    }
}