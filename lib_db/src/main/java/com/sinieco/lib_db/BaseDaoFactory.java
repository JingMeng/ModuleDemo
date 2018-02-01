package com.sinieco.lib_db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author BaiMeng on 2017/11/13.
 */

public class BaseDaoFactory {
    private static BaseDaoFactory mInstance = new BaseDaoFactory() ;
    private String mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"moduledemo";
    //储存多用户登录记录的数据库
    private SQLiteDatabase mSqLiteDatabase;
    //储存具体用户数据的数据（每个用户一个数据库）
    private SQLiteDatabase userSQLiteDatabase ;
    //线程安全的HashMap
    private Map<String,BaseDao> map = Collections.synchronizedMap(new HashMap<String, BaseDao>());

    private BaseDaoFactory(){
        File file = new File(mRootPath);
        if(file.exists()||file.isFile()){
            if (file.isFile()){
                file.delete();
            }
        }
        if(!file.exists()){
            file.mkdirs();
        }
        String manyUserDb = file.getAbsolutePath()+ File.separator+"mytest.db";
        mSqLiteDatabase = openDatabase(manyUserDb);
    }

    private SQLiteDatabase openDatabase(String dbPath) {
         return SQLiteDatabase.openOrCreateDatabase(dbPath, null);
    }

    public static BaseDaoFactory getInstance(){
        return mInstance ;
    }

    /**
     * 获取具体的UserDao对象
     * @param daoClass
     * @param entityClass
     * @param <T>
     * @param <M>
     * @return
     */
    public synchronized <T extends BaseDao<M>,M>  T
        getDataHelper(Class<T> daoClass,Class<M> entityClass){
        BaseDao baseDao = null ;
        //首先看所需的Dao是否缓存过
        if(map.get(daoClass.getSimpleName())!=null){
            return (T) map.get(daoClass.getSimpleName());
        }
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(entityClass,mSqLiteDatabase);
            map.put(daoClass.getSimpleName(),baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T)baseDao;
    }

    public synchronized <T extends BaseDao<M>,M>  T
    getUserHelper(Class<T> daoClass,Class<M> entityClass){
        Log.e("database地址","------>>>>  "+PrivateDataBaseEnums.database.getValue());
        userSQLiteDatabase = openDatabase(PrivateDataBaseEnums.database.getValue());
        BaseDao baseDao = null ;
        if(map.get(daoClass.getSimpleName())!=null){
            return (T) map.get(daoClass.getSimpleName());
        }
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(entityClass,userSQLiteDatabase);
            map.put(daoClass.getSimpleName(),baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T)baseDao;
    }
}
