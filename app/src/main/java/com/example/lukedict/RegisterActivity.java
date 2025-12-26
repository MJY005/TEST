package com.example.lukedict;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private AuthRepository authRepository;
    private EditText etUsername, etPassword, etAge, etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // 替换为你的布局ID

        // 初始化
        authRepository = AuthRepository.getInstance(this);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etAge = findViewById(R.id.et_age);
        etPhone = findViewById(R.id.et_phone);
    }

    // 注册按钮点击事件（替换为你的按钮ID对应的点击方法）
    public void onRegisterClick(View view) {
        // 获取输入
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // 输入校验
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "用户名/密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 解析年龄
        Integer age = null;
        if (!ageStr.isEmpty()) {
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "年龄必须是数字", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 1. 检查用户名是否存在
        authRepository.userExists(username, new AuthRepository.AuthCallback<Boolean>() {
            @Override
            public void onResult(Boolean exists) {
                runOnUiThread(() -> {
                    if (exists) {
                        Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                    } else {
                        // 2. 执行注册
                        registerUser(username, password, age, phone);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "检查用户失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onLoading(boolean isLoading) {
                runOnUiThread(() -> {
                    // 可选：显示加载动画
                    findViewById(R.id.btn_register).setEnabled(!isLoading);
                });
            }
        });
    }

    // 执行注册逻辑
    private void registerUser(String username, String password, Integer age, String phone) {
        User user = new User(username, password, age, phone);
        authRepository.register(user, new AuthRepository.AuthCallback<Boolean>() {
            @Override
            public void onResult(Boolean success) {
                runOnUiThread(() -> {
                    if (success) {
                        // 修正getDbPath调用（无参数）
                        String dbPath = authRepository.getDbPath();
                        Toast.makeText(RegisterActivity.this,
                                "注册成功！数据库路径：" + dbPath, Toast.LENGTH_LONG).show();
                        finish(); // 返回登录页
                    } else {
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "注册失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onLoading(boolean isLoading) {
                runOnUiThread(() -> {
                    findViewById(R.id.btn_register).setEnabled(!isLoading);
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 可选：释放资源
        // authRepository.release();
    }
}