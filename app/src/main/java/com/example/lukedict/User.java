package com.example.lukedict;

public class User {
    private long id;
    private String username;
    private String password;
    private Integer age;
    private String phone;
    private long createdAt; // 创建时间（时间戳）

    // 核心构造函数（解决编译错误）
    public User(String username, String password, Integer age, String phone) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.phone = phone;
    }

    // 空构造函数（适配数据库解析）
    public User() {}

    // Getter & Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}