package com.sinieco.lib_volley.volley.inter;


import com.sinieco.lib_volley.volley.Httptask;

/**
 * Created by BaiMeng on 2017/11/3.
 *具体联网的操作
 */
public interface IHttpService {
    void setUrl (String url);
    void setRequestData(byte[] request);
    void setHttpListener(IHttpListener listener);
    void excute();
}
