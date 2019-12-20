package com.example.demo;

import javax.persistence.Entity;

/**
 * Created by 不爱编程的程序猿
 * 2018/11/8
 */

@Entity
public class User {

    //用户名
    private String userName;
    //密码
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}