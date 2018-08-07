package com.example.wenzitong.entity;

import java.io.Serializable;

/**
 * Created by 邹特强 on 2018/2/1.
 * 每个客户对应的javavbean类
 */

public class User implements Serializable {
    /**
     * 用户id
     */
    private String id;
    /**
     * 用户昵称
     */
    private String username;


    /**
     * 从网络加载图片，获取图片的网址
     */
    private String icon;


    /**
     * 用户性别
     */
    private String gender;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户激活状态
     */
    private int state;


    /**
     * 用户学校
     */
    private String school;

    /**
     * 用户班级
     */
    private String grade;

    /**
     * 用户坐标
     */
    private double[] location;

    /**
     * 用户输入的四位密码
     */
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String headImgUrl) {
        this.icon = headImgUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }
}
