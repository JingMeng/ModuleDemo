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

public class UserDao<T> extends BaseDao<T> {
    @Override
    public String createTable(Class<T> cla) {
        Field[] fields = cla.getFields();
        String tableName = cla.getAnnotation(TableName.class)==null?cla.getSimpleName():cla.getAnnotation(TableName.class).value();
        StringBuffer sb = new StringBuffer("create table if not exists "+tableName+"(");
        for (Field field : fields) {
            String columnName = null ;
            field.setAccessible(true);
            if(field.getAnnotation(ColumnName.class)!=null){
                columnName = field.getAnnotation(ColumnName.class).value();
            }else {
                columnName = field.getName() ;
            }
            String typeString = getTypeString(field);
            sb.append(columnName+typeString+",");
        }
        int i = sb.lastIndexOf(",");
        sb.replace(i,i+1,")");
        return sb.toString();
    }
}
