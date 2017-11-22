package com.sinieco.lib_db;

import android.os.Environment;

import java.io.File;

/**
 * @author BaiMeng on 2017/11/21.
 */

public enum  PrivateDataBaseEnums {
    database("local/data/database/");
    private String value ;
    PrivateDataBaseEnums (String value){
        this.value = value ;
    }
    public String getValue(){
        UserDao userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class,User.class);
        if(userDao!=null){
            User currentUser = userDao.getCurrentUser();
            if(currentUser != null){
                File file = new File(Environment.getExternalStorageDirectory()+"/moduledemo",currentUser.getName());
                if(!file.exists()){
                    file.mkdirs();
                }
                return file.getAbsolutePath()+"/logic.db";
            }
        }
        return value ;
    }
}
