package com.sinieco.lib_db;

import com.sinieco.lib_db.annotation.ColumnName;
import com.sinieco.lib_db.annotation.TableName;

import java.lang.annotation.Target;

/**
 * Created by BaiMeng on 2017/11/14.
 */
@TableName("tb_user")
public class User {
    @ColumnName("tb_name")
    public String name ;
    public String password ;
    public Double length ;
    public Boolean isMarried ;
    public User son ;

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
                '}'+"\n";
    }
}