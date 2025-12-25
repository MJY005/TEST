package com.example.lukedict;

/**
 * 用户实体，存储在本地 SQLite。
 */
public class User {
    private long id;
    private String username;
    private String password;
    private Integer age;
    private String phone;

    public User() {}

    public User(String username, String password, Integer age, String phone) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

