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
        etUsername = findViewById(R.id.register_et_username);
        etPassword = findViewById(R.id.register_et_password);
        etAge = findViewById(R.id.register_et_age);
        etPhone = findViewById(R.id.register_et_phone);
        
        // 绑定注册按钮点击事件
        findViewById(R.id.register_btn_submit).setOnClickListener(this::onRegisterClick);
    }

    // 注册按钮点击事件
    public void onRegisterClick(View view) {
        // 获取输入
        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String ageStr = etAge.getText().toString().trim();
        final String phone = etPhone.getText().toString().trim();

        // 输入校验
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "用户名/密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 解析年龄（使用final变量）
        final Integer age;
        if (!ageStr.isEmpty()) {
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "年龄必须是数字", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            age = null;
        }

        // 1. 检查用户名是否存在
        authRepository.userExists(username, new AuthRepository.AuthCallback<Boolean>() {
            @Override
            public void onResult(Boolean exists) {
                runOnUiThread(() -> {
                    if (exists) {
                        Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                    } else {
                        // 2. 执行注册（变量已经是final，可以直接使用）
                        registerUser(username, password, age, phone);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> 
                    Toast.makeText(RegisterActivity.this, "检查用户失败：" + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onLoading(boolean isLoading) {
                runOnUiThread(() -> 
                    findViewById(R.id.register_btn_submit).setEnabled(!isLoading)
                );
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
                runOnUiThread(() -> 
                    Toast.makeText(RegisterActivity.this, "注册失败：" + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onLoading(boolean isLoading) {
                runOnUiThread(() -> 
                    findViewById(R.id.register_btn_submit).setEnabled(!isLoading)
                );
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