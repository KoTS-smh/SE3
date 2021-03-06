package com.sec.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sec.server.enums.Education;
import com.sec.server.enums.Sex;
import com.sec.server.enums.UserLevel;
import com.sec.server.enums.UserProfession;
import com.sec.server.model.UserModel;


public class User {

    private long userId;
    private String username;
    private String password;
    private UserLevel userLevel;
    //信用积分
    private int point;
    private String telPhone;
    private Sex sex;
    private String description;
    private String Email;
    //余额
    private double balance;
    private UserProfession profession;

    public User() {
    }

    public User(UserModel userModel) {
        if(userModel.getUserId() != 0)
            this.userId = userModel.getUserId();
        this.username = userModel.getUsername();
        this.password = userModel.getPassword();
        this.telPhone = userModel.getTelPhone();
        this.sex = userModel.getSex();
        this.description = userModel.getDescription();
        this.Email = userModel.getEmail();
        this.profession = userModel.getProfession();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserLevel getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(UserLevel userLevel) {
        this.userLevel = userLevel;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
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

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public UserProfession getProfession() {
        return profession;
    }

    public void setProfession(UserProfession profession) {
        this.profession = profession;
    }
}
