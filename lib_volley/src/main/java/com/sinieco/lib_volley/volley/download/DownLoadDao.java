package com.sinieco.lib_volley.volley.download;

import android.database.Cursor;

import com.sinieco.lib_db.BaseDao;
import com.sinieco.lib_db.annotation.ColumnName;
import com.sinieco.lib_db.annotation.PrimaryKey;
import com.sinieco.lib_db.annotation.TableName;
import com.sinieco.lib_volley.volley.download.en.Priority;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * @author BaiMeng on 2017/11/17.
 */

public class DownLoadDao extends BaseDao<DownLoadItemInfo> {
    //用于存放需要被下载的集合，已下载的需要及时移除
    private List<DownLoadItemInfo> downLoadItemInfoList =
            Collections.synchronizedList(new ArrayList<DownLoadItemInfo>());
    //比较器
    DownLoadInfoComparator comparator = new DownLoadInfoComparator() ;

    /**
     * 生成下载Id
     * @return
     */
    private Integer generateRecordId(){
        int maxId = 0 ;
        String sql = "select max(id) from "+ getTableName() ;
        synchronized (DownLoadDao.class){
            Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);
            if(cursor.moveToNext()){
                int index = cursor.getColumnIndex("max(id)");
                if(index!=-1){
                    Object value = cursor.getInt(index);
                    if(value != null){
                        maxId = Integer.parseInt(String.valueOf(value));
                    }
                }
            }
        }
        return maxId+1 ;
    }

    /**
     * 根据url和文件路径查找记录
     * @param url
     * @param filePath
     * @return
     */
    public DownLoadItemInfo findRecord(String url , String filePath){
        synchronized (DownLoadDao.class){
            for (DownLoadItemInfo downLoadItemInfo : downLoadItemInfoList) {
                if(downLoadItemInfo.getmUrl().equals(url)&&downLoadItemInfo.getFilePath().equals(filePath)){
                    return downLoadItemInfo;
                }
            }
            DownLoadItemInfo where = new DownLoadItemInfo(url,filePath);
            List<DownLoadItemInfo> resultList = super.query(where);
            if(resultList.size()>0){
                return resultList.get(0);
            }
        }
        return null ;
    }

    /**
     * 根据文件路径获取下载文件记录列表
     * @param filePath
     * @return
     */
    public List<DownLoadItemInfo> findRecord(String filePath){
        synchronized (DownLoadDao.class){
            DownLoadItemInfo downloadItemInfo = new DownLoadItemInfo();
            downloadItemInfo.setmFilePath(filePath);
            return super.query(downloadItemInfo);
        }
    }

    public DownLoadItemInfo addRecord(String url , String filePath , String displayName , int priority){
        synchronized (DownLoadDao.class){
            DownLoadItemInfo existRecord = findRecord(url,filePath);
            if(existRecord == null){
                DownLoadItemInfo record = new DownLoadItemInfo();
                record.setmFilePath(filePath);
                record.setmUrl(url);
                record.setCurrentLength(0L);
                record.setId(generateRecordId());
                record.setDisplayName(displayName);
                record.setStatus(DownloadStatus.waitting.getValue());
                record.setTotalLength(0L);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                record.setStartTime(format.format(new Date()));
                record.setEndTime("0");
                record.setPriority(priority);
                super.insert(record);
                downLoadItemInfoList.add(record);
                return record ;
            }
            return null ;
        }
    }

    /**
     * 更新下载记录
     * @param record
     * @return
     */
    public int updateRecord(DownLoadItemInfo record){
        DownLoadItemInfo where = new DownLoadItemInfo();
        where.setId(record.getId());
        int result = 0 ;
        synchronized (DownLoadDao.class){
            result = super.update(record, where);
        }
        if(result>0){
            for (int i = 0 ; i < downLoadItemInfoList.size() ; i++){
                if(downLoadItemInfoList.get(i).getId().intValue() == record.getId()){
                    downLoadItemInfoList.set(i,record);
                    break;
                }
            }
        }
        return result ;
    }

    /**
     * 根据文件路径查找下载记录
     * @param filePath
     * @return
     */
    public DownLoadItemInfo findSigleRecord(String filePath){
        List<DownLoadItemInfo> records = findRecord(filePath);
        if(records.isEmpty()){
            return null ;
        }
        return records.get(0);
    }

    /**
     * 根据id查找下载记录
     * @param recordId
     * @return
     */
    public DownLoadItemInfo findRecord(int recordId){
        synchronized (DownLoadDao.class){
            for (DownLoadItemInfo record : downLoadItemInfoList) {
                if(recordId == record.getId()){
                    return record;
                }
            }
            DownLoadItemInfo where = new DownLoadItemInfo();
            where.setId(recordId);
            List<DownLoadItemInfo> records = super.query(where);
            if(records.size()>0){
                return records.get(0);
            }
            return null ;
        }
    }

    public boolean removeRecordById(int id){
        synchronized (DownLoadDao.class){
           for(int i = 0 ; i < downLoadItemInfoList.size() ; i++){
               if(downLoadItemInfoList.get(i).getId() == id){
                   downLoadItemInfoList.remove(i);
                   break;
               }
           }
           return true ;
        }
    }

    @Override
    public String createTable(Class<DownLoadItemInfo> cla) {
        Field[] fields = cla.getFields();
        String tableName = cla.getAnnotation(TableName.class)==null?cla.getSimpleName():cla.getAnnotation(TableName.class).value();
        StringBuffer sb = new StringBuffer("create table if not exists "+tableName+"(");
        for (Field field : fields) {
            String columnName = null ;
            String primaryKey = null ;
            field.setAccessible(true);
            if(field.getAnnotation(ColumnName.class)!=null){
                columnName = field.getAnnotation(ColumnName.class).value();
                if(field.getAnnotation(PrimaryKey.class)!=null){
                    primaryKey = " primary key";
                }
            }else {
                columnName = field.getName() ;
            }
            String typeString = getTypeString(field);
            sb.append(columnName+typeString+primaryKey+",");
        }
        int i = sb.lastIndexOf(",");
        sb.replace(i,i+1,")");
        return sb.toString();
    }

    private class DownLoadInfoComparator implements Comparator<DownLoadItemInfo>{

        @Override
        public int compare(DownLoadItemInfo itemInfo1, DownLoadItemInfo itemInfo2) {
            return itemInfo1.getId()-itemInfo2.getId();
        }
    }


}
