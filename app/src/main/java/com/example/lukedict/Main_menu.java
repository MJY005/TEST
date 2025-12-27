package com.example.lukedict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import readinggrid.reading_grid_Activity;

public class Main_menu extends AppCompatActivity implements View.OnClickListener {
    private ImageView block1;
    private ImageView block2;
    private ImageView block3;
    private ImageView block4;

    public void showToast(){
        String message = getIntent().getStringExtra("message");
        String age = getIntent().getStringExtra("age");
        // 优化提示文本，避免过长导致显示不全
        if (!TextUtils.isEmpty(message)) {
            // 如果用户名过长，简化提示文本
            String toastText = message.length() > 10 ? "登录成功，欢迎使用！" : message + "，欢迎您！";
            Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
        } else {
            // 如果没有传递用户名，显示通用提示
            Toast.makeText(this, "登录成功，欢迎使用！", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        showToast();
        initWidget();
    }

    private void initWidget() {
        block1 = (ImageView) findViewById(R.id.block_1);
        block2 = (ImageView) findViewById(R.id.block_2);
        block3 = (ImageView) findViewById(R.id.block_3);
        block4 = (ImageView) findViewById(R.id.block_4);
        block1.setOnClickListener(this);
        block2.setOnClickListener(this);
        block3.setOnClickListener(this);
        block4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.block_1:
                Intent intent = new Intent(Main_menu.this,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.block_2:
                Intent intent2 = new Intent(Main_menu.this, reading_grid_Activity.class);
                startActivity(intent2);
                break;
            case R.id.block_3:
                Intent intent3 = new Intent(Main_menu.this,ChatActivity.class);
                startActivity(intent3);
                break;
            case R.id.block_4:
                Intent intent4 = new Intent(Main_menu.this,AboutActivity.class);
                startActivity(intent4);
                break;
        }
    }
}