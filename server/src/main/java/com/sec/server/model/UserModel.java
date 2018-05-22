package com.sec.server.model;

import com.sec.server.enums.Education;
import com.sec.server.enums.Sex;
import com.sec.server.enums.UserLevel;

import javax.validation.constraints.NotBlank;

public class UserModel {
    private long userId;
    private String username;
    private String password;
    private String telPhone;
    private int point;
    private Sex sex;
    private Education education;
    private String description;
    private String Email;
    private UserLevel userLevel;

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
    public String getTelPhone() {
        return telPhone;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public UserLevel getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(UserLevel userLevel) {
        this.userLevel = userLevel;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    @NotBlank(message = "用户Id不能为空")
    public long getUserId(){return userId;}

    public void setUserId(long userId){this.userId = userId;}
}
