package com.example.lukedict;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理界面：展示SQLite的增删改查操作
 * 采用MVC架构：Activity作为Controller，AuthRepository作为Model，布局作为View
 */
public class UserManagementActivity extends AppCompatActivity {
    private ListView userListView;
    private UserListAdapter adapter;
    private AuthRepository authRepository;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);
        
        // 使用单例模式获取Repository
        authRepository = AuthRepository.getInstance(this);
        userList = new ArrayList<>();
        
        initView();
        loadUsers();
        
        // 设置ActionBar返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("用户管理");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        userListView = findViewById(R.id.user_list_view);
        adapter = new UserListAdapter();
        userListView.setAdapter(adapter);
        
        Button btnAdd = findViewById(R.id.btn_add_user);
        btnAdd.setOnClickListener(v -> showAddUserDialog());
    }

    /**
     * 加载所有用户（查询操作 - Read）
     */
    private void loadUsers() {
        userList.clear();
        Cursor cursor = authRepository.listUsers();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                int ageIndex = cursor.getColumnIndex("age");
                Integer age = cursor.isNull(ageIndex) ? null : cursor.getInt(ageIndex);
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                
                User user = new User(username, "", age, phone);
                user.setId(id);
                userList.add(user);
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 显示添加用户对话框（增加操作 - Create）
     */
    private void showAddUserDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_user_edit, null);
        EditText etUsername = dialogView.findViewById(R.id.dialog_et_username);
        EditText etPassword = dialogView.findViewById(R.id.dialog_et_password);
        EditText etAge = dialogView.findViewById(R.id.dialog_et_age);
        EditText etPhone = dialogView.findViewById(R.id.dialog_et_phone);
        
        new AlertDialog.Builder(this)
                .setTitle("添加用户")
                .setView(dialogView)
                .setPositiveButton("确定", (dialog, which) -> {
                    String username = etUsername.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    String ageStr = etAge.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    
                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                        Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    if (authRepository.userExists(username)) {
                        Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    Integer age = null;
                    if (!TextUtils.isEmpty(ageStr)) {
                        try {
                            age = Integer.parseInt(ageStr);
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "年龄需为数字", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    
                    User newUser = new User(username, password, age, phone);
                    boolean success = authRepository.register(newUser);
                    if (success) {
                        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                        loadUsers(); // 刷新列表
                    } else {
                        Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 显示编辑用户对话框（修改操作 - Update）
     */
    private void showEditUserDialog(User user) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_user_edit, null);
        EditText etUsername = dialogView.findViewById(R.id.dialog_et_username);
        EditText etPassword = dialogView.findViewById(R.id.dialog_et_password);
        EditText etAge = dialogView.findViewById(R.id.dialog_et_age);
        EditText etPhone = dialogView.findViewById(R.id.dialog_et_phone);
        
        etUsername.setText(user.getUsername());
        etUsername.setEnabled(false); // 用户名不可修改
        etPassword.setText(""); // 密码留空，如需修改则输入新密码
        if (user.getAge() != null) {
            etAge.setText(String.valueOf(user.getAge()));
        }
        if (!TextUtils.isEmpty(user.getPhone())) {
            etPhone.setText(user.getPhone());
        }
        
        new AlertDialog.Builder(this)
                .setTitle("编辑用户")
                .setView(dialogView)
                .setPositiveButton("确定", (dialog, which) -> {
                    String password = etPassword.getText().toString().trim();
                    String ageStr = etAge.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    
                    User updatedUser = new User(user.getUsername(), password, null, phone);
                    if (!TextUtils.isEmpty(ageStr)) {
                        try {
                            updatedUser.setAge(Integer.parseInt(ageStr));
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "年龄需为数字", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    
                    boolean success = authRepository.updateUser(updatedUser);
                    if (success) {
                        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                        loadUsers(); // 刷新列表
                    } else {
                        Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 删除用户（删除操作 - Delete）
     */
    private void deleteUser(User user) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除用户 " + user.getUsername() + " 吗？")
                .setPositiveButton("删除", (dialog, which) -> {
                    boolean success = authRepository.deleteUser(user.getUsername());
                    if (success) {
                        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                        loadUsers(); // 刷新列表
                    } else {
                        Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 用户列表适配器
     */
    private class UserListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return userList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(UserManagementActivity.this)
                        .inflate(R.layout.item_user, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            User user = userList.get(position);
            holder.tvId.setText("ID: " + user.getId());
            holder.tvUsername.setText("用户名: " + user.getUsername());
            holder.tvAge.setText("年龄: " + (user.getAge() != null ? user.getAge() : "未设置"));
            holder.tvPhone.setText("手机: " + (TextUtils.isEmpty(user.getPhone()) ? "未设置" : user.getPhone()));
            
            holder.btnEdit.setOnClickListener(v -> showEditUserDialog(user));
            holder.btnDelete.setOnClickListener(v -> deleteUser(user));
            
            return convertView;
        }

        class ViewHolder {
            TextView tvId, tvUsername, tvAge, tvPhone;
            Button btnEdit, btnDelete;

            ViewHolder(View view) {
                tvId = view.findViewById(R.id.item_user_tv_id);
                tvUsername = view.findViewById(R.id.item_user_tv_username);
                tvAge = view.findViewById(R.id.item_user_tv_age);
                tvPhone = view.findViewById(R.id.item_user_tv_phone);
                btnEdit = view.findViewById(R.id.item_user_btn_edit);
                btnDelete = view.findViewById(R.id.item_user_btn_delete);
            }
        }
    }
}

