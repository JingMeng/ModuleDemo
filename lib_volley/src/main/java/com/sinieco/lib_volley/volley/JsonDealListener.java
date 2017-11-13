package com.sinieco.lib_volley.volley;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sinieco.lib_volley.volley.inter.IDataListener;
import com.sinieco.lib_volley.volley.inter.IHttpListener;

import org.apache.http.HttpEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by BaiMeng on 2017/11/3.
 */
public class JsonDealListener<M> implements IHttpListener {
    private Class<M> mResponse ;
    private IDataListener<M> mDataListener ;
    Handler handler = new Handler(Looper.getMainLooper());

    public JsonDealListener(Class<M> response, IDataListener<M> dataListener) {
        this.mResponse = response;
        this.mDataListener = dataListener;
    }

    @Override
    public void onSuccess(HttpEntity entity) {
        try {
            InputStream is = entity.getContent();
            String content = getContent(is);
            final M response = JSON.parseObject(content,mResponse);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mDataListener.onSuccess(response);
                }
            });
        } catch (Exception e) {
            mDataListener.onFail(e);
            e.printStackTrace();
        }
    }

    private String getContent(InputStream is) throws IllegalAccessException, InstantiationException {
        String content = null ;
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null ;
            try{
                while ((line = br.readLine())!=null){
                    sb.append(line+"\n");
                }
            }catch (Exception e){
                mDataListener.onFail(e);
            }finally {
                try {
                    //关闭流
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return sb.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
            mDataListener.onFail(e);
            String.class.newInstance();
        }
        return content;
    }

    @Override
    public void onFail() {
        mDataListener.onFail(null);
    }
}
