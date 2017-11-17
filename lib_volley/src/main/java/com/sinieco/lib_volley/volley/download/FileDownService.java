package com.sinieco.lib_volley.volley.download;

import android.util.Log;

import com.sinieco.lib_volley.volley.inter.IHttpListener;
import com.sinieco.lib_volley.volley.inter.IHttpService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author BaiMeng on 2017/11/9.
 */

class FileDownService implements IHttpService {
    private String mUrl ;
    private byte[] mRequestData ;
    private IHttpListener mHttpListener ;
    private Map<String ,String> header = Collections.synchronizedMap(new HashMap<String, String>());
    private HttpClient httpClient = new DefaultHttpClient();
    private HttpGet httpGet ;
    private HttpResponseHandler responseHandler = new HttpResponseHandler() ;

    @Override
    public void setUrl(String url) {
        this.mUrl = url ;
    }

    @Override
    public void setRequestData(byte[] request) {
        mRequestData = request ;
    }

    @Override
    public void setHttpListener(IHttpListener listener) {
        this.mHttpListener = listener ;
    }

    @Override
    public void excute() {
        httpGet = new HttpGet(mUrl);
        Log.e("开始-----------",">>>>>>>>>>>>>>执行");
        constrcutHeader();
        try {
            httpClient.execute(httpGet,responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void constrcutHeader() {
        Iterator<String> iterator = header.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = header.get(key);
            httpGet.addHeader(key,value);
            Log.e("key = "+key ,"    -----  value = "+value);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public Map<String, String> getHttpHeadMap() {
        return header;
    }

    @Override
    public boolean cancle() {
        return false;
    }

    @Override
    public boolean isCancle() {
        return false;
    }

    @Override
    public boolean isPause() {
        return false;
    }

    private class HttpResponseHandler implements ResponseHandler{
        @Override
        public Object handleResponse(HttpResponse httpResponse) throws IOException {
            int code = httpResponse.getStatusLine().getStatusCode() ;
            Log.e("FileDownService","相应码"+code);
            if(200 == code || 206 == code){
                mHttpListener.onSuccess(httpResponse.getEntity());
            }else {
                mHttpListener.onFail();
            }
            return null;
        }
    }
}
