package com.sinieco.lib_volley.volley;



import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sinieco.lib_volley.volley.inter.IHttpListener;
import com.sinieco.lib_volley.volley.inter.IHttpService;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.FutureTask;


/**
 * Created by BaiMeng on 2017/11/3.
 */
public class Httptask<T> implements Runnable {
    private FutureTask futureTask ;
    private IHttpService service ;
    public Httptask(RequestHolder<T> holder) {
        service = holder.getHttpService() ;
        service.setUrl(holder.getUrl());
        service.setHttpListener(holder.getHttpListener());
        IHttpListener httpListener = holder.getHttpListener();
        httpListener.addHeader(service.getHttpHeadMap());
        try {
            T requestInfo = holder.getRequestInfo();
            String request = JSON.toJSONString(requestInfo);
            service.setRequestData(request.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        service.excute();
    }

    public void pause() {
        service.pause();
        if(futureTask != null){
            Log.e("------------------->>>>>>>>","任务被移除。");
            ThreadPoolManager.getInstance().removeTask(futureTask);
        }
    }

    public void start(){
        futureTask = new FutureTask(this,null);
        try {
            ThreadPoolManager.getInstance().excute(futureTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
