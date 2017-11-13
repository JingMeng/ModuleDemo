package com.sinieco.lib_volley.volley.inter;


import com.sinieco.lib_volley.volley.Httptask;

import java.util.Map;

/**
 * Created by BaiMeng on 2017/11/3.
 *具体联网的操作
 */
public interface IHttpService {
    void setUrl (String url);
    void setRequestData(byte[] request);
    void setHttpListener(IHttpListener listener);
    void excute();

    void pause();

    /**
     *
     * 以下的方法是 额外添加的
     * 获取请求头的map
     * @return
     */
    Map<String,String> getHttpHeadMap();

    boolean cancle();

    boolean isCancle();

    boolean isPause();
}
