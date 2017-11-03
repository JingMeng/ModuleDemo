package com.sinieco.lib_volley.volley.inter;

/**
 * Created by BaiMeng on 2017/11/3.
 * 获取到数据后的回调
 */
public interface IDataListener<M> {
    void onSuccess(M response);
    void onFail(Exception e);
}
