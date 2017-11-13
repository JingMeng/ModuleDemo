package com.sinieco.lib_volley.volley;



import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sinieco.lib_volley.volley.inter.IHttpService;

import java.io.UnsupportedEncodingException;


/**
 * Created by BaiMeng on 2017/11/3.
 */
public class Httptask<T> implements Runnable {
    private IHttpService service ;
    public Httptask(RequestHolder<T> holder) {
        service = holder.getHttpService() ;
        service.setUrl(holder.getUrl());
        service.setHttpListener(holder.getHttpListener());
        T requestInfo = holder.getRequestInfo();
        String request = JSON.toJSONString(requestInfo);
        try {
            service.setRequestData(request.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        service.excute();
    }
}
