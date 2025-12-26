//package com.example.lukedict;
//
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.AsyncTask;
//
//import com.example.lukedict.User;
//import com.example.lukedict.UserDatabaseHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
////package com.example.lukedict;
////
////import android.content.ContentValues;
////import android.content.Context;
////import android.database.Cursor;
////import android.database.sqlite.SQLiteDatabase;
////import java.util.ArrayList;
////import java.util.List;
////
/////**
//// * 用户数据仓库：处理用户表的增删改查
//// */
////public class AuthRepository {
////    private static AuthRepository instance;
////    private final UserDatabaseHelper dbHelper;
////
////    private AuthRepository(Context context) {
////        dbHelper = new UserDatabaseHelper(context.getApplicationContext());
////    }
////
////    // 单例模式
////    public static synchronized AuthRepository getInstance(Context context) {
////        if (instance == null) {
////            instance = new AuthRepository(context);
////        }
////        return instance;
////    }
////
////    // 1. 注册用户（插入新用户）
////    public boolean registerUser(String username, String password, Integer age, String phone) {
////        SQLiteDatabase db = dbHelper.getWritableDatabase();
////        try {
////            ContentValues values = new ContentValues();
////            values.put(UserDatabaseHelper.COLUMN_USER_USERNAME, username);
////            values.put(UserDatabaseHelper.COLUMN_USER_PASSWORD, password);
////            if (age != null) {
////                values.put(UserDatabaseHelper.COLUMN_USER_AGE, age);
////            }
////            if (phone != null) {
////                values.put(UserDatabaseHelper.COLUMN_USER_PHONE, phone);
////            }
////            // 插入成功返回行ID（>0），失败返回-1（用户名重复）
////<<<<<<< HEAD
////            long rowId = db.insertOrThrow(UserDatabaseHelper.TABLE_USERS, null, values);
////
////            return rowId != -1;
////        } finally {
////            db.close();
////        }
////
////    }
////
////    // 2. 登录验证（根据用户名和密码查询）
////    public boolean loginUser(String username, String password) {
////        SQLiteDatabase db = dbHelper.getReadableDatabase();
////        Cursor cursor = null;
////        try {
////             cursor = db.query(
////                    UserDatabaseHelper.TABLE_USERS,
////                    new String[]{"_id"},
////                    "username=? AND password=?",
////                    new String[]{username, password},
////                    null, null, null
////            );
////            // 存在匹配记录则登录成功
////            return cursor != null && cursor.moveToFirst();
////        } finally {
////            if (cursor != null) {
////                cursor.close();
////            }
////            db.close();
////        }
////    }
////
////    // 3. 根据用户名查询用户信息
////    public User getUser(String username) {
////        SQLiteDatabase db = dbHelper.getReadableDatabase();
////        Cursor cursor = null;
////        try {
////            cursor = db.query(
////                    UserDatabaseHelper.TABLE_USERS,
////                    null, // 查询所有字段
////                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=?",
////                    new String[]{username},
////                    null, null, null
////            );
////            if (cursor != null && cursor.moveToFirst()) {
////                User user = new User();
////                user.setId(cursor.getLong(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_ID)));
////                user.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_USERNAME)));
////                user.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PASSWORD)));
////                if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE))) {
////                    user.setAge(cursor.getInt(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE)));
////                }
////                if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE))) {
////                    user.setPhone(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE)));
////                }
////                return user;
////            }
////            return null;
////        } finally {
////            if (cursor != null) {
////                cursor.close();
////            }
////            db.close();
////        }
////    }
////
////    // 4. 修改用户资料（年龄/手机号）
////    public boolean updateUserInfo(String username, Integer age, String phone) {
////        SQLiteDatabase db = dbHelper.getWritableDatabase();
////        try {
////            ContentValues values = new ContentValues();
////            if (age != null) {
////                values.put(UserDatabaseHelper.COLUMN_USER_AGE, age);
////            }
////            if (phone != null) {
////                values.put(UserDatabaseHelper.COLUMN_USER_PHONE, phone);
////            }
////            // 执行更新
////            int rowsAffected = db.update(
////                    UserDatabaseHelper.TABLE_USERS,
////                    values,
////                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=?",
////                    new String[]{username}
////            );
////            return rowsAffected > 0;
////=======
////            long rowId = db.insert(UserDatabaseHelper.TABLE_USERS, null, values);
////            return rowId != -1;
////>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
////        } finally {
////            db.close();
////        }
////    }
////
////<<<<<<< HEAD
////    // 5. 修改用户密码
////    public boolean updatePassword(String username, String newPassword) {
////        SQLiteDatabase db = dbHelper.getWritableDatabase();
////        try {
////            ContentValues values = new ContentValues();
////            values.put(UserDatabaseHelper.COLUMN_USER_PASSWORD, newPassword);
////=======
////    // 2. 登录验证（根据用户名和密码查询）
////    public boolean loginUser(String username, String password) {
////        SQLiteDatabase db = dbHelper.getReadableDatabase();
////        Cursor cursor = null;
////        try {
////            cursor = db.query(
////                    UserDatabaseHelper.TABLE_USERS,
////                    new String[]{UserDatabaseHelper.COLUMN_USER_ID},
////                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=? AND " + UserDatabaseHelper.COLUMN_USER_PASSWORD + "=?",
////                    new String[]{username, password},
////                    null, null, null
////            );
////            // 存在匹配记录则登录成功
////            return cursor != null && cursor.moveToFirst();
////        } finally {
////            if (cursor != null) {
////                cursor.close();
////            }
////            db.close();
////        }
////    }
////
////    // 3. 根据用户名查询用户信息
////    public User getUser(String username) {
////        SQLiteDatabase db = dbHelper.getReadableDatabase();
////        Cursor cursor = null;
////        try {
////            cursor = db.query(
////                    UserDatabaseHelper.TABLE_USERS,
////                    null, // 查询所有字段
////                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=?",
////                    new String[]{username},
////                    null, null, null
////            );
////            if (cursor != null && cursor.moveToFirst()) {
////                User user = new User();
////                user.setId(cursor.getLong(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_ID)));
////                user.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_USERNAME)));
////                user.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PASSWORD)));
////                if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE))) {
////                    user.setAge(cursor.getInt(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE)));
////                }
////                if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE))) {
////                    user.setPhone(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE)));
////                }
////                return user;
////            }
////            return null;
////        } finally {
////            if (cursor != null) {
////                cursor.close();
////            }
////            db.close();
////        }
////    }
////
////    // 4. 修改用户资料（年龄/手机号）
////    public boolean updateUserInfo(String username, Integer age, String phone) {
////        SQLiteDatabase db = dbHelper.getWritableDatabase();
////        try {
////            ContentValues values = new ContentValues();
////            if (age != null) {
////                values.put(UserDatabaseHelper.COLUMN_USER_AGE, age);
////            }
////            if (phone != null) {
////                values.put(UserDatabaseHelper.COLUMN_USER_PHONE, phone);
////            }
////            // 执行更新
////>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
////            int rowsAffected = db.update(
////                    UserDatabaseHelper.TABLE_USERS,
////                    values,
////                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=?",
////                    new String[]{username}
////            );
////            return rowsAffected > 0;
////        } finally {
////            db.close();
////        }
////    }
////
////<<<<<<< HEAD
////=======
////    // 5. 修改用户密码
////    public boolean updatePassword(String username, String newPassword) {
////        SQLiteDatabase db = dbHelper.getWritableDatabase();
////        try {
////            ContentValues values = new ContentValues();
////            values.put(UserDatabaseHelper.COLUMN_USER_PASSWORD, newPassword);
////            int rowsAffected = db.update(
////                    UserDatabaseHelper.TABLE_USERS,
////                    values,
////                    UserDatabaseHelper.COLUMN_USER_USERNAME + "=?",
////                    new String[]{username}
////            );
////            return rowsAffected > 0;
////        } finally {
////            db.close();
////        }
////    }
////
////>>>>>>> 5d77a0b1a2ba411aa04f10f1f085143c04f96471
////    // 6. 查询所有用户（可选，用于管理员功能）
////    public List<User> getAllUsers() {
////        List<User> userList = new ArrayList<>();
////        SQLiteDatabase db = dbHelper.getReadableDatabase();
////        Cursor cursor = null;
////        try {
////            cursor = db.query(
////                    UserDatabaseHelper.TABLE_USERS,
////                    null,
////                    null, null, null, null,
////                    UserDatabaseHelper.COLUMN_USER_CREATE_TIME + " DESC"
////            );
////            if (cursor != null) {
////                while (cursor.moveToNext()) {
////                    User user = new User();
////                    user.setId(cursor.getLong(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_ID)));
////                    user.setUsername(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_USERNAME)));
////                    user.setPassword(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PASSWORD)));
////                    if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE))) {
////                        user.setAge(cursor.getInt(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_AGE)));
////                    }
////                    if (!cursor.isNull(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE))) {
////                        user.setPhone(cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PHONE)));
////                    }
////                    userList.add(user);
////                }
////            }
////            return userList;
////        } finally {
////            if (cursor != null) {
////                cursor.close();
////            }
////            db.close();
////        }
////    }
////}
//public class AuthRepository {
//    // ... 原有代码 ...
//
//    // 修复1：列名从created_at改为create_time（与DDL一致）
//    public Cursor listUsers() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        // 替换"created_at"为"create_time"
//        return db.query(UserDatabaseHelper.TABLE_USERS,
//                new String[]{"_id", "username", "age", "phone", "create_time"},
//                null, null, null, null, "_id DESC");
//    }
//
//    // 修复2：新增异步查询方法（符合安卓主线程禁止数据库操作的规范）
//    public void listUsersAsync(Callback<List<User>> callback) {
//        new AsyncTask<Void, Void, List<User>>() {
//            @Override
//            protected List<User> doInBackground(Void... voids) {
//                List<User> users = new ArrayList<>();
//                Cursor cursor = null;
//                try {
//                    cursor = listUsers();
//                    while (cursor.moveToNext()) {
//                        users.add(parseUser(cursor));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (cursor != null) cursor.close(); // 确保Cursor关闭
//                }
//                return users;
//            }
//
//            @Override
//            protected void onPostExecute(List<User> users) {
//                callback.onResult(users);
//            }
//        }.execute();
//    }
//
//    // 定义回调接口，避免直接返回Cursor
//    public interface Callback<T> {
//        void onResult(T result);
//    }
//
//    // 修复3：完善parseUser方法，处理create_time（可选）
//    private User parseUser(Cursor cursor) {
//        User user = new User();
//        // ... 原有字段 ...
//        // 新增处理create_time（如果需要）
//        if (!cursor.isNull(cursor.getColumnIndexOrThrow("create_time"))) {
//            user.setCreateTime(cursor.getString(cursor.getColumnIndexOrThrow("create_time")));
//        }
//        return user;
//    }
//
//    // ... 其他方法保持不变，但建议所有数据库操作都添加异步版本 ...
//}

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
 * 封装登录/注册相关的数据库操作，采用单例模式确保数据库访问一致性。
 * 优化点：密码加密存储、异步操作支持、完善异常处理、避免Cursor泄漏。
 */
public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private static final String ENCRYPT_ALGORITHM = "SHA-256"; // 密码加密算法
    private static AuthRepository instance;
    private final UserDatabaseHelper dbHelper;
    private final Context context;
    private final ExecutorService backgroundExecutor; // 后台线程池，处理数据库操作

    /**
     * 私有构造函数，确保单例模式
     *
     * @param context 上下文（建议使用Application Context）
     */
    private AuthRepository(Context context) {
        this.context = context.getApplicationContext(); // 使用应用上下文避免内存泄漏
        this.dbHelper = new UserDatabaseHelper(this.context);
        this.backgroundExecutor = Executors.newSingleThreadExecutor(); // 单线程池保证数据库操作串行执行
    }

    /**
     * 获取单例实例
     *
     * @param context 上下文
     * @return AuthRepository实例
     */
    public static synchronized AuthRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AuthRepository(context);
        }
        return instance;
    }

    /**
     * 注册用户（异步操作）
     *
     * @param user     待注册用户信息
     * @param callback 注册结果回调
     */
    public void register(User user, AuthCallback<Boolean> callback) {
        if (user == null || TextUtils.isEmpty(user.getUsername()) || TextUtils.isEmpty(user.getPassword())) {
            if (callback != null) {
                callback.onResult(false);
            }
            return;
        }

        backgroundExecutor.execute(() -> {
            boolean result = false;
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                // 检查用户名是否已存在
                if (userExistsSync(user.getUsername())) {
                    Log.d(TAG, "注册失败：用户名已存在 - " + user.getUsername());
                    result = false;
                } else {
                    // 密码加密后存储
                    String encryptedPwd = encryptPassword(user.getPassword());
                    ContentValues values = new ContentValues();
                    values.put("username", user.getUsername());
                    values.put("password", encryptedPwd);
                    values.put("age", user.getAge());
                    values.put("phone", user.getPhone());
                    values.put("created_at", System.currentTimeMillis()); // 新增创建时间

                    long rowId = db.insertOrThrow(UserDatabaseHelper.TABLE_USERS, null, values);
                    user.setId(rowId);
                    result = rowId != -1;
                    Log.d(TAG, "注册" + (result ? "成功" : "失败") + "：" + user.getUsername());
                }
            } catch (SQLException e) {
                Log.e(TAG, "注册数据库错误", e);
                result = false;
            } finally {
                if (db != null) {
                    db.close();
                }
                // 回调结果到主线程（如果需要，可通过Handler实现）
                if (callback != null) {
                    callback.onResult(result);
                }
            }
        });
    }

    /**
     * 用户登录（异步操作）
     *
     * @param username 用户名
     * @param password 密码（明文，内部会加密验证）
     * @param callback 登录结果回调
     */
//    public void login(String username, String password, AuthCallback<Boolean> callback) {
//        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
//            if (callback != null) {
//                callback.onResult(false);
//            }
//            return;
//        }
//
//        backgroundExecutor.execute(() -> {
//            boolean success = false;
//            SQLiteDatabase db = null;
//            Cursor cursor = null;
//            try {
//                db = dbHelper.getReadableDatabase();
//                String encryptedPwd = encryptPassword(password);
//                cursor = db.query(
//                        UserDatabaseHelper.TABLE_USERS,
//                        new String[]{"_id"},
//                        "username=? AND password=?",
//                        new String[]{username, encryptedPwd},
//                        null, null, null
//                );
//                success = cursor.moveToFirst();
//                Log.d(TAG, "用户" + username + "登录" + (success ? "成功" : "失败"));
//            } catch (Exception e) {
//                Log.e(TAG, "登录数据库错误", e);
//                success = false;
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//                if (db != null) {
//                    db.close();
//                }
//                if (callback != null) {
//                    callback.onResult(success);
//                }
//            }
//        });
//    }

    /**
     * 检查用户名是否存在（同步方法，仅供内部调用）
     */
    private boolean userExistsSync(String username) {
        if (TextUtils.isEmpty(username)) {
            return false;
        }
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
            Log.e(TAG, "检查用户存在性错误", e);
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 检查用户名是否存在（异步方法）
     */
    public void userExists(String username, AuthCallback<Boolean> callback) {
        if (TextUtils.isEmpty(username)) {
            if (callback != null) {
                callback.onResult(false);
            }
            return;
        }

        backgroundExecutor.execute(() -> {
            boolean exists = userExistsSync(username);
            if (callback != null) {
                callback.onResult(exists);
            }
        });
    }

    /**
     * 获取用户信息（异步操作）
     *
     * @param username 用户名
     * @param callback 结果回调（返回User对象或null）
     */
    public void getUser(String username, AuthCallback<User> callback) {
        if (TextUtils.isEmpty(username)) {
            if (callback != null) {
                callback.onResult(null);
            }
            return;
        }

        backgroundExecutor.execute(() -> {
            User user = null;
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = dbHelper.getReadableDatabase();
                cursor = db.query(
                        UserDatabaseHelper.TABLE_USERS,
                        new String[]{"_id", "username", "password", "age", "phone", "created_at"},
                        "username=?",
                        new String[]{username},
                        null, null, null
                );
                if (cursor.moveToFirst()) {
                    user = parseUser(cursor);
                }
            } catch (SQLException e) {
                Log.e(TAG, "获取用户信息错误", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                if (db != null) {
                    db.close();
                }
                if (callback != null) {
                    callback.onResult(user);
                }
            }
        });
    }

    /**
     * 更新用户信息（异步操作）
     *
     * @param user     待更新的用户信息（必须包含username）
     * @param callback 结果回调
     */
    public void updateUser(User user, AuthCallback<Boolean> callback) {
        if (user == null || TextUtils.isEmpty(user.getUsername())) {
            if (callback != null) {
                callback.onResult(false);
            }
            return;
        }

        backgroundExecutor.execute(() -> {
            boolean success = false;
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                // 密码不为空则加密后更新
                if (!TextUtils.isEmpty(user.getPassword())) {
                    values.put("password", encryptPassword(user.getPassword()));
                }
                if (user.getAge() != null) {
                    values.put("age", user.getAge());
                }
                if (!TextUtils.isEmpty(user.getPhone())) {
                    values.put("phone", user.getPhone());
                }

                int rows = db.update(
                        UserDatabaseHelper.TABLE_USERS,
                        values,
                        "username=?",
                        new String[]{user.getUsername()}
                );
                success = rows > 0;
                Log.d(TAG, "用户" + user.getUsername() + "信息更新" + (success ? "成功" : "失败"));
            } catch (SQLException e) {
                Log.e(TAG, "更新用户信息错误", e);
            } finally {
                if (db != null) {
                    db.close();
                }
                if (callback != null) {
                    callback.onResult(success);
                }
            }
        });
    }

    /**
     * 删除用户（异步操作）
     *
     * @param username 用户名
     * @param callback 结果回调
     */
    public void deleteUser(String username, AuthCallback<Boolean> callback) {
        if (TextUtils.isEmpty(username)) {
            if (callback != null) {
                callback.onResult(false);
            }
            return;
        }

        backgroundExecutor.execute(() -> {
            boolean success = false;
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                int rows = db.delete(
                        UserDatabaseHelper.TABLE_USERS,
                        "username=?",
                        new String[]{username}
                );
                success = rows > 0;
                Log.d(TAG, "用户" + username + "删除" + (success ? "成功" : "失败"));
            } catch (SQLException e) {
                Log.e(TAG, "删除用户错误", e);
            } finally {
                if (db != null) {
                    db.close();
                }
                if (callback != null) {
                    callback.onResult(success);
                }
            }
        });
    }

    /**
     * 获取所有用户列表（异步操作，返回List避免Cursor泄漏）
     *
     * @param callback 结果回调
     */
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
                    User user = parseUser(cursor);
                    userList.add(user); // 不包含密码，保护敏感信息
                }
            } catch (SQLException e) {
                Log.e(TAG, "获取用户列表错误", e);
                userList = null;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                if (db != null) {
                    db.close();
                }
                if (callback != null) {
                    callback.onResult(userList);
                }
            }
        });
    }

    /**
     * 获取数据库物理路径（用于调试）
     */
    public String getDbPath() {
        return context.getDatabasePath(UserDatabaseHelper.DB_NAME).getAbsolutePath();
    }

    /**
     * 密码加密（SHA-256）
     *
     * @param rawPassword 明文密码
     * @return 加密后的密码字符串（失败返回原密码，避免崩溃）
     */
    private String encryptPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ENCRYPT_ALGORITHM);
            byte[] hash = digest.digest(rawPassword.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "密码加密算法不存在", e);
            return rawPassword; // 加密失败时返回原密码（仅作为降级处理）
        }
    }

    /**
     * 从Cursor解析User对象
     */
    @SuppressLint("Range")
    private User parseUser(Cursor cursor) {
        User user = new User();
        try {
            user.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            // 列表查询时不解析密码，保护敏感信息
            if (cursor.getColumnIndex("password") != -1) {
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            }
            if (!cursor.isNull(cursor.getColumnIndex("age"))) {
                user.setAge(cursor.getInt(cursor.getColumnIndex("age")));
            }
            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            if (!cursor.isNull(cursor.getColumnIndex("created_at"))) {
                user.setCreatedAt(cursor.getLong(cursor.getColumnIndex("created_at")));
            }
        } catch (Exception e) {
            Log.e(TAG, "解析用户信息错误", e);
        }
        return user;
    }

    /**
     * 释放资源（如退出应用时调用）
     */
    public void release() {
        backgroundExecutor.shutdown();
        dbHelper.close();
        instance = null; // 允许重新初始化
    }

    /**
     * 通用回调接口，用于异步操作结果返回
     *
     * @param <T> 回调结果的数据类型
     */

    public interface AuthCallback<T> {
        void onResult(T result);
        default void onError(Throwable e) {} // 默认空实现，避免强制重写
        default void onLoading(boolean isLoading) {}
    }
    // 修改 login 方法，使用线程池异步执行，通过回调返回结果
    public void login(String username, String password, AuthCallback<Boolean> callback) {

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            if (callback != null) {
                callback.onResult(false);
            }
            return;
        }

        // 用线程池执行数据库操作（避免阻塞UI线程）
        Executors.newSingleThreadExecutor().execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    UserDatabaseHelper.TABLE_USERS,
                    new String[]{"_id"},
                    "username=? AND password=?",
                    new String[]{username, password},
                    null, null, null
            );
            boolean success = cursor.moveToFirst();
            cursor.close();
            db.close();

            // 回调结果（若需在UI线程处理，可通过Handler切换）
            if (callback != null) {
                callback.onResult(success);
            }
        });
    }
}