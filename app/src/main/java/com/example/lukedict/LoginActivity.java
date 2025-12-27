package com.example.lukedict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private CheckBox cb_remember;
    private CheckBox cb_autologin;
    private EditText et_pwd;
    private EditText et_username;
    private TextView registeraccount;
    private ImageView arrowicon;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 使用单例模式获取Repository
        authRepository = AuthRepository.getInstance(this);
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        initView();
        restoreState();
    }

    private void initView()   //初始化
    {
        registeraccount = findViewById(R.id.registeraccount);
        et_username = findViewById(R.id.et_username);
        et_pwd = findViewById(R.id.et_pwd);
        cb_remember = findViewById(R.id.cb_rememberpwd);
        cb_autologin = findViewById(R.id.cb_autologin);
        arrowicon = findViewById(R.id.arrowicon);
        arrowicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
        registeraccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void restoreState() {
        boolean rememberpwd = sp.getBoolean("rememberpwd",false);
        boolean autologin = sp.getBoolean("autologin",false);
        if(rememberpwd){
            String name = sp.getString("name","");
            String pwd = sp.getString("pwd","");
            et_username.setText(name);
            et_pwd.setText(pwd);
            cb_remember.setChecked(true);
        }
        cb_autologin.setChecked(autologin);
        if(autologin && !TextUtils.isEmpty(et_username.getText()) && !TextUtils.isEmpty(et_pwd.getText())){
            handleLogin();
        }
    }

    private void handleLogin(){
        String name = et_username.getText().toString().trim();
        String pwd = et_pwd.getText().toString().trim();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)){
            Toast.makeText(LoginActivity.this,"用户名或密码为空",Toast.LENGTH_SHORT).show();
            return;
        }
        // 使用回调接口进行登录验证
        authRepository.login(name, pwd, new AuthRepository.AuthCallback<Boolean>() {
            @Override
            public void onResult(Boolean success) {
                runOnUiThread(() -> {
                    if (!success) {
                        Toast.makeText(LoginActivity.this, "账号或密码错误，请先注册或重试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(cb_remember.isChecked()){
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("name",name);
                        editor.putString("pwd",pwd);
                        editor.putBoolean("rememberpwd",true);
                        editor.apply();
                    }else{
                        sp.edit().putBoolean("rememberpwd",false).apply();
                    }
                    if(cb_autologin.isChecked()){
                        sp.edit().putBoolean("autologin",true).apply();
                    }else{
                        sp.edit().putBoolean("autologin",false).apply();
                    }
                    // 传递用户名到主菜单，用于显示欢迎提示
                    Intent intent = new Intent(LoginActivity.this, Main_menu.class);
                    intent.putExtra("message", name);
                    startActivity(intent);
                    finish(); // 关闭当前页面
                });
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "登录失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

}