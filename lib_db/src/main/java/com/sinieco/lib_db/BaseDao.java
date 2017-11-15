package com.sinieco.lib_db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sinieco.lib_db.annotation.ColumnName;
import com.sinieco.lib_db.annotation.TableName;

import java.io.File;
import java.io.FileDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author BaiMeng on 2017/11/13.
 */

public abstract class BaseDao<T> implements IBaseDao<T> {
    private SQLiteDatabase mSqLiteDatabase ;
    private boolean mIsInit = false ;
    private String mTableName ;
    private Class<T> mEntityClass;
    private HashMap<String ,Field> cacheMap ;

    protected synchronized boolean init(Class<T> entityClass, SQLiteDatabase sqLiteDatabase) {
        if(!mIsInit){
            mEntityClass = entityClass;
            mSqLiteDatabase = sqLiteDatabase ;
            TableName tableName = entityClass.getAnnotation(TableName.class);
            if(tableName != null){
                mTableName = tableName.value();
            }else {
                mTableName = entityClass.getSimpleName();
            }
            if(!mSqLiteDatabase.isOpen()){
                return false ;
            }
            if(createTable(mEntityClass)!=null){
                mSqLiteDatabase.execSQL(createTable(mEntityClass));
            }
            cacheMap = new HashMap<>();
            initCacheMap();
            mIsInit = true ;
        }
        return mIsInit ;
    }

    protected void initCacheMap(){
        //从第一条数据开始，查询0条数据，用于获取所有的列名
        String sql = "select * from "+mTableName+" limit 1 , 0" ;
        Cursor cursor = null;
        try {
            cursor = mSqLiteDatabase.rawQuery(sql,null);
            String[] columnNames = cursor.getColumnNames();
            Field[] fields = mEntityClass.getFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
            for (String columnName : columnNames) {
                Field fieldColumn = null ;
                for (Field field : fields) {
                    String filedName ;
                    if(field.getAnnotation(ColumnName.class)!=null){
                        filedName = field.getAnnotation(ColumnName.class).value();
                    }else {
                        filedName = field.getName() ;
                    }
                    if(columnName.equals(filedName)){
                        fieldColumn = field ;
                        break;
                    }
                }
                if(fieldColumn !=null){
                    cacheMap.put(columnName , fieldColumn);
                }
            }
            Iterator<String> iterator = cacheMap.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                Field value =  cacheMap.get(key);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }
    }

    private ContentValues getContentValues(Map<String, String> map) {
        Iterator<String> iterator = map.keySet().iterator();
        ContentValues contentValues = new ContentValues();
        while (iterator.hasNext()){
            String colmunName = iterator.next();
            String value = map.get(colmunName);
            if(value != null){
                contentValues.put(colmunName,value);
            }
        }
        return contentValues;
    }

    /**
     * 将实体对象转换成key为列名，value为值的map集合
     * @param entity  实体对象
     * @return
     */
    protected Map<String,String> getMapValue(T entity){
        HashMap<String , String> result = new HashMap<>() ;
        Iterator<Field> iterator = cacheMap.values().iterator();
        while (iterator.hasNext()){
            Field field = iterator.next();
            String cacheKey = null ;
            String cacheValue = null ;
            if(field.getAnnotation(ColumnName.class)!=null){
                cacheKey = field.getAnnotation(ColumnName.class).value();
            }else {
                cacheKey = field.getName() ;
            }
            try {
                if(null == field.get(entity)){
                    continue;
                }
                cacheValue = field.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.put(cacheKey,cacheValue);
        }
        return result ;
    }

    public String getTypeString(Field field) {
        Class<?> type = field.getType();
        if (type == String.class || type == CharSequence.class) {
            return " TEXT";
        } else if (type == int.class || type == Integer.class ||
                type == long.class || type == Long.class ||
                type == boolean.class || type == Boolean.class ) {
            return " INTEGER";
        } else if (type == float.class || type == Float.class ||
                type == double.class || type == Double.class) {
            return " REAL";
        } else  {
            return " BLOB";
        }
    }

    @Override
    public int delete(T where) {
        Map<String, String> whereMap = getMapValue(where);
        Condition condition = new Condition(whereMap);
        return mSqLiteDatabase.delete(mTableName,condition.getWhereClause(),condition.getWhereArgs());
    }

    @Override
    public int update(T entity ,T where){
        Map<String, String> mapValue = getMapValue(entity);
        ContentValues contentValues = getContentValues(mapValue);
        Map<String, String> whereMap = getMapValue(where);
        Condition condition = new Condition(whereMap);
        return mSqLiteDatabase.update(mTableName,contentValues,condition.getWhereClause(),condition.getWhereArgs());
    }

    @Override
    public long insert(T entity) {
        Map<String ,String> map = getMapValue(entity);
        ContentValues contentValue = getContentValues(map);
        return mSqLiteDatabase.insert(mTableName,null,contentValue);
    }

    @Override
    public List<T> query(T where){
        return query(where,null,null,null);
    }

    @Override
    public List<T> query(T where,String orderBy ,Integer startIndex ,Integer limit){
        String limitStr = null ;
        if(startIndex!=null && limit!=null){
            limitStr = startIndex+" , "+limit ;
        }
        String[] colmunNames = cacheMap.keySet().toArray(new String[cacheMap.keySet().size()]);
        Map<String, String> mapValue = getMapValue(where);
        Condition condition = new Condition(mapValue);
        Cursor cursor = mSqLiteDatabase.query(mTableName, colmunNames, condition.getWhereClause(), condition.getWhereArgs(), null, null, orderBy, limitStr);
        return getListFromCursor(cursor,where);
    }

    @Override
    public List<T> query(String sql,Class<T> cla)  {
        Cursor cursor = mSqLiteDatabase.rawQuery(sql, null);
        T t = null;
        try {
            t = cla.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return getListFromCursor(cursor,t);
    }

    private List<T> getListFromCursor(Cursor cursor,T where) {
        ArrayList<T> list = null;
        if (cursor != null) {
            list = new ArrayList<>();
            while (cursor.moveToNext()) {
                T item = null;
                try {
                    item = (T) where.getClass().newInstance();
                    Iterator<String> iterator = cacheMap.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        if (!setValue(cursor, key, item)) {
                            continue;
                        }
                    }
                    if (item != null) {
                        list.add(item);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return list;
    }

    private boolean setValue(Cursor cursor ,String key, Object item) throws IllegalAccessException {
        boolean isSeted = true ;
        Field field = cacheMap.get(key);
        int columnIndex = cursor.getColumnIndex(key);
        if(columnIndex != -1 ){
            if(field.getType() == String.class){
                String string = cursor.getString(columnIndex);
                field.set(item,string);
            }else if(field.getType() == Integer.class){
                int i = cursor.getInt(columnIndex);
                field.set(item,i);
            }else if(field.getType() == Double.class){
                double d = cursor.getDouble(columnIndex);
                field.set(item,d);
            }else if(field.getType() == Short.class){
                short s = cursor.getShort(columnIndex);
                field.set(item,s);
            }else if(field.getType() == Byte[].class){
                byte[] bytes = cursor.getBlob(columnIndex);
                field.set(item,bytes);
            }else if(field.getType() == Long.class){
                long l = cursor.getLong(columnIndex);
                field.set(item,l);
            }else if(field.getType() == Float.class){
                float f = cursor.getFloat(columnIndex);
                field.set(item,f);
            }else if(field.getType() == Boolean.class){
                String bool = cursor.getString(columnIndex);
                if(bool.equals("true")){
                    field.set(item,true);
                }else {
                    field.set(item,false);
                }
            }else {
               isSeted = false ;
            }
        }else {
            isSeted = false ;
        }
        return isSeted ;
    }


    private class Condition{
        private String whereClause ;
        private String[] whereArgs ;
        List<String> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer("1=1");
        public Condition(Map<String, String> whereMap) {
            Iterator<String> iterator = whereMap.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                String value = whereMap.get(key);
                if(value!=null){
                    list.add(value);
                    sb.append(" and "+key+" =?");
                }
            }
            whereClause = sb.toString();
            whereArgs = list.toArray(new String[list.size()]);
        }

        public String getWhereClause() {
            Log.e("打印条件 ------------>>>>>",whereClause);
            return whereClause;
        }

        public void setWhereClause(String whereClause) {
            this.whereClause = whereClause;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }

        public void setWhereArgs(String[] whereArgs) {
            this.whereArgs = whereArgs;
        }
    }

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
