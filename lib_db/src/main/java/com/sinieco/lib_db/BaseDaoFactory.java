package com.sinieco.lib_db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Field;

/**
 * @author BaiMeng on 2017/11/13.
 */

public class BaseDaoFactory<T> {
    private static BaseDaoFactory mInstance = new BaseDaoFactory() ;
    private String mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"moduledemo";
    private SQLiteDatabase mSqLiteDatabase;

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
        String dbPath = file.getAbsolutePath()+ File.separator+"mytest.db";
        openDatabase(dbPath);
    }

    private void openDatabase(String dbPath) {
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
    }

    public static BaseDaoFactory getInstance(){
        return mInstance ;
    }

    public synchronized <T extends BaseDao<M>,M>  T
        getDataHelper(Class<T> daoClass,Class<M> entityClass){
        BaseDao baseDao = null ;
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(entityClass,mSqLiteDatabase);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T)baseDao;
    }
}
