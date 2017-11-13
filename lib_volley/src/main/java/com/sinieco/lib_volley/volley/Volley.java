package com.sinieco.lib_volley.volley;

import com.sinieco.lib_volley.volley.inter.IDataListener;
import com.sinieco.lib_volley.volley.inter.IHttpListener;
import com.sinieco.lib_volley.volley.inter.IHttpService;

import org.apache.http.protocol.HttpService;

import java.util.concurrent.FutureTask;

/**
 * Created by BaiMeng on 2017/11/3.
 */
public class Volley {

    public static<T,M> void sendRequest(String url ,T params , Class<T> requestType, IDataListener listener){
        IHttpService httpService = new JsonHttpService();
        IHttpListener<M> httpListener = new JsonDealListener<>(requestType,listener);
        RequestHolder<T> holder = new RequestHolder<>() ;
        holder.setUrl(url);
        holder.setHttpService(httpService);
        holder.setHttpListener(httpListener);
        Httptask<T> task = new Httptask<>(holder);
        try {
            ThreadPoolManager.getInstance().excute(new FutureTask<T>(task,null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
