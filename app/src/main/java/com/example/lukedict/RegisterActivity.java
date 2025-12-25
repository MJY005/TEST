package com.example.lukedict;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 注册界面：将用户信息写入本地 SQLite。
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPhone;
    private EditText etAge;
    private EditText etPassword;
    private EditText etConfirm;
    private Button btnRegister;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // 使用单例模式获取Repository
        authRepository = AuthRepository.getInstance(this);
        initView();
    }

    private void initView() {
        etUsername = findViewById(R.id.register_et_username);
        etPhone = findViewById(R.id.register_et_phone);
        etAge = findViewById(R.id.register_et_age);
        etPassword = findViewById(R.id.register_et_password);
        etConfirm = findViewById(R.id.register_et_confirm);
        btnRegister = findViewById(R.id.register_btn_submit);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();
        Integer age = null;
        if (!TextUtils.isEmpty(ageStr)) {
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "年龄需为数字", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm)) {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirm)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if (authRepository.userExists(username)) {
            Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean success = authRepository.register(new User(username, password, age, phone));
        if (success) {
            Toast.makeText(this, "注册成功，数据已写入: " + authRepository.getDbPath(this), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "注册失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }
}

