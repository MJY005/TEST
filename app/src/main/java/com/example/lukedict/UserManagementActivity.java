package com.example.lukedict;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity {
    private AuthRepository authRepository;
    private ListView lvUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        authRepository = AuthRepository.getInstance(this);
        ListView userListView = findViewById(R.id.user_list_view);

        // 加载用户列表
        loadUserList();
    }

    // 加载用户列表（修正listUsers调用）
    private void loadUserList() {
        authRepository.listUsers(new AuthRepository.AuthCallback<List<User>>() {
            @Override
            public void onResult(List<User> users) {
                runOnUiThread(() -> {
                    if (users == null || users.isEmpty()) {
                        Toast.makeText(UserManagementActivity.this, "暂无用户", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 显示用户列表（示例）
                    String[] userNames = new String[users.size()];
                    for (int i = 0; i < users.size(); i++) {
                        userNames[i] = users.get(i).getUsername() + " | " + users.get(i).getPhone();
                    }
                    lvUsers.setAdapter(new ArrayAdapter<>(
                            UserManagementActivity.this,
                            android.R.layout.simple_list_item_1,
                            userNames
                    ));
                });
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    Toast.makeText(UserManagementActivity.this, "加载用户信息失败，请重试", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // 示例：添加用户（修正userExists和register调用）
    public void onAddUserClick(View view) {
        String username = "test_user";
        String password = "123456";
        Integer age = 20;
        String phone = "13800138000";

        // 检查用户名
        authRepository.userExists(username, new AuthRepository.AuthCallback<Boolean>() {
            @Override
            public void onResult(Boolean exists) {
                runOnUiThread(() -> {
                    if (exists) {
                        Toast.makeText(UserManagementActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                    } else {
                        // 注册新用户
                        User newUser = new User(username, password, age, phone);
                        authRepository.register(newUser, new AuthRepository.AuthCallback<Boolean>() {
                            @Override
                            public void onResult(Boolean success) {
                                runOnUiThread(() -> {
                                    if (success) {
                                        Toast.makeText(UserManagementActivity.this, "添加用户成功", Toast.LENGTH_SHORT).show();
                                        loadUserList(); // 刷新列表
                                    } else {
                                        Toast.makeText(UserManagementActivity.this, "添加用户失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    // 示例：更新用户（修正updateUser调用）
    private void updateUser(String username, String newPhone) {
        User user = new User(username, "", null, newPhone);
        authRepository.updateUser(user, new AuthRepository.AuthCallback<Boolean>() {
            @Override
            public void onResult(Boolean success) {
                runOnUiThread(() -> {
                    Toast.makeText(UserManagementActivity.this,
                            success ? "更新成功" : "更新失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // 示例：删除用户（修正deleteUser调用）
    private void deleteUser(String username) {
        authRepository.deleteUser(username, new AuthRepository.AuthCallback<Boolean>() {
            @Override
            public void onResult(Boolean success) {
                runOnUiThread(() -> {
                    Toast.makeText(UserManagementActivity.this,
                            success ? "删除成功" : "删除失败", Toast.LENGTH_SHORT).show();
                    loadUserList(); // 刷新列表
                });
            }
        });
    }
}