package com.sinieco.lib_volley.volley;

import com.sinieco.lib_volley.volley.inter.IDataListener;
import com.sinieco.lib_volley.volley.inter.IHttpListener;
import com.sinieco.lib_volley.volley.inter.IHttpService;

/**
 * Created by BaiMeng on 2017/11/3.
 */
public class RequestHolder<T> {
    private String url ;
    private IHttpService httpService ;
    private T requestInfo ;

    public IHttpListener getHttpListener() {
        return httpListener;
    }

    public void setHttpListener(IHttpListener httpListener) {
        this.httpListener = httpListener;
    }

    public IHttpService getHttpService() {
        return httpService;
    }

    public void setHttpService(IHttpService httpService) {
        this.httpService = httpService;
    }

    private IHttpListener httpListener ;

    public RequestHolder() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRequestInfo(T requestInfo){
        this.requestInfo =  requestInfo ;
    }

    public T getRequestInfo() {
        return requestInfo;
    }
}
