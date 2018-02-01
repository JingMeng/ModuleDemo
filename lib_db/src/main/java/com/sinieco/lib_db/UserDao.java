package com.sinieco.lib_db;

import android.util.Log;

import com.sinieco.lib_db.annotation.ColumnName;
import com.sinieco.lib_db.annotation.TableName;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author BaiMeng on 2017/11/13.
 */

public class UserDao extends BaseDao<User> {
    //建表
    @Override
    public String createTable(Class<User> cla) {
        return super.createTable(cla);
    }

    /**
     *     用于多用户登录，更新用户状态
     */
    @Override
    public long insert(User entity) {
        //查询出所有在当前手机上登录过的用户
        List<User> users = query(new User());
        User where = null ;
        int isExist = 0 ;
        for (User user : users) {
            where = new User() ;
            where.setUser_id(user.getUser_id());
            //如果当前用户登录过就将当前用户更新为在线
            if(user.getName().equals(entity.getName())){
                entity.setLoginStatus(LoginStatus.online.getValue());
                update(entity,user);
                isExist++ ;
                continue;
            }
            //将其他用户跟新为离线状态
            user.setLoginStatus(LoginStatus.outline.getValue());
            update(user, where);
        }
        if(isExist != 0){
            return isExist ;
        }
        entity.setLoginStatus(LoginStatus.online.getValue());
        //调用BaseDao的insert方法
        return super.insert(entity);
    }

    /**
     * 获取当前登录用户，用于多用户登录
     * @return
     */
    public User getCurrentUser() {
        User where = new User();
        where.setLoginStatus(LoginStatus.online.value);
        List<User> onlineUser = query(where);
        if(onlineUser.size()>0){
            return onlineUser.get(0);
        }
        return  null ;
    }
}
