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
    @Override
    public String createTable(Class<User> cla) {
        return super.createTable(cla);
    }

    @Override
    public long insert(User entity) {
        List<User> users = query(new User());
        User where = null ;
        int isExist = 0 ;
        for (User user : users) {
            where = new User() ;
            where.setUser_id(user.getUser_id());
            if(user.getName().equals(entity.getName())){
                entity.setLoginStatus(LoginStatus.online.getValue());
                update(entity,user);
                isExist++ ;
                continue;
            }
            user.setLoginStatus(LoginStatus.outline.getValue());
            update(user, where);
        }
        if(isExist != 0){
            return isExist ;
        }
        entity.setLoginStatus(LoginStatus.online.getValue());
        return super.insert(entity);
    }

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
