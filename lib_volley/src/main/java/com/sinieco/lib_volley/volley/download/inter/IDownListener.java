package com.sinieco.lib_volley.volley.download.inter;

import com.sinieco.lib_volley.volley.inter.IHttpListener;
import com.sinieco.lib_volley.volley.inter.IHttpService;

import org.apache.http.HttpEntity;

/**
 * @author BaiMeng on 2017/11/9.
 */

public interface IDownListener extends IHttpListener {
    void setHttpService(IHttpService httpService);
    void setCancleCalle();
    void setPauseCallble();
}
