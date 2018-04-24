package com.sec.server.model;

import javax.validation.constraints.NotBlank;

public class UserModel {
    private String username;
    private long userId;
    private String password;
    private String tel_number;

    @NotBlank(message = "用户名不能为空")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank(message = "密码不能为空")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank(message = "电话号码不能为空")
    public String getTel_number() {
        return tel_number;
    }

    public void setTel_number(String tel_number) {
        this.tel_number = tel_number;
    }

    @NotBlank(message = "用户Id不能为空")
    public long getUserId(){return userId;}

    public void setUserId(long userId){this.userId = userId;}
}
