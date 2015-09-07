package com.cnwir.pedometer.domain;

/**
 * Created by heaven on 2015/7/23.
 */
public class User {

    private int id;

    private String username;

    private String nickname;

    private String orangekeyId;

    private String orangekey;


    private int goal;

    private String sex;

    private int today_step_num;


    private int total_step_num;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOrangekeyId() {
        return orangekeyId;
    }

    public void setOrangekeyId(String orangekeyId) {
        this.orangekeyId = orangekeyId;
    }

    public String getOrangekey() {
        return orangekey;
    }

    public void setOrangekey(String orangekey) {
        this.orangekey = orangekey;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public void setId(int userId) {
        this.id = userId;
    }


    public int getToday_step_num() {
        return today_step_num;
    }

    public void setToday_step_num(int today_step_num) {
        this.today_step_num = today_step_num;
    }


    public int getTotal_step_num() {
        return total_step_num;
    }

    public void setTotal_step_num(int total_step_num) {
        this.total_step_num = total_step_num;
    }
}
