package com.sinieco.lib_db;

import com.sinieco.lib_db.annotation.ColumnName;
import com.sinieco.lib_db.annotation.TableName;

import java.lang.annotation.Target;

/**
 * Created by BaiMeng on 2017/11/14.
 */
@TableName("tb_user")
public class User {
    public void setLength(Double length) {
        this.length = length;
    }

    public Boolean getMarried() {
        return isMarried;
    }

    public void setMarried(Boolean married) {
        isMarried = married;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    @ColumnName("tb_name")
    public String name ;
    public String password ;
    public Double length ;
    public Boolean isMarried ;
    public User son ;
    public String _id ;
    public Integer loginStatus ;

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }


    public User() {
    }

    public User(String name, String password, double length, boolean isMarried, User son) {
        this.name = name;
        this.password = password;
        this.length = length;
        this.isMarried = isMarried;
        this.son = son;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void setMarried(boolean married) {
        isMarried = married;
    }

    public User getSon() {
        return son;
    }

    public void setSon(User son) {
        this.son = son;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", length=" + length +
                ", isMarried=" + isMarried +
                ", son=" + son +
                ", _id='" + _id + '\'' +
                ", loginStatus=" + loginStatus +
                '}';
    }

    public void setUser_id(String s) {
        _id = s ;
    }

    public String getUser_id(){
        return _id;
    }
}
