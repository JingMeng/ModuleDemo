package com.sinieco.lib_db;

import java.util.List;

/**
 * @author BaiMeng on 2017/11/13.
 */

public interface IBaseDao<T> {
    long insert(T entity);
    int update(T entity , T where);
    int delete(T where);
    List<T> query(T where);
    List<T> query(T where ,String orderBy , Integer startIndex ,Integer limit) ;
    List<T> query(String sql,Class<T> cla);
}
