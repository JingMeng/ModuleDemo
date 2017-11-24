package com.sinieco.lib_imageloader.core;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sinieco.lib_imageloader.config.DisplayConfig;
import com.sinieco.lib_imageloader.config.ImageLoaderConfig;
import com.sinieco.lib_imageloader.request.BitmapRequest;
import com.sinieco.lib_imageloader.request.RequestQueue;

import java.text.SimpleDateFormat;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class SimpleImageLoader {

    private ImageLoaderConfig mConfig ;

    private RequestQueue mQueue ;

    private static volatile SimpleImageLoader mInstance ;

    private SimpleImageLoader(){}

    private SimpleImageLoader(ImageLoaderConfig config){
        this.mConfig = config ;
        this.mQueue = new RequestQueue(config.getThreadCount()) ;
        mQueue.start();
    }

    /**
     * 第一次获取单例对象需要传入ImageLoaderConfig初始化单例对象
     * @param config
     * @return
     */
    public static SimpleImageLoader getInstance(ImageLoaderConfig config){
        if(mInstance == null){
            synchronized (SimpleImageLoader.class){
                if(mInstance==null){
                    mInstance = new SimpleImageLoader(config);
                }
            }
        }
        return mInstance ;
    }

    /**
     * 第二次获取单例对象
     * @return
     */
    public static SimpleImageLoader getInstance(){
        if(mInstance == null){
            throw new UnsupportedOperationException("SimpleImageLoader为初始化");
        }
        return mInstance ;
    }

    public void displayImage(ImageView img , String uri){
        displayImage(img,uri,null,null);
    }

    public void displayImage(ImageView img , String uri , DisplayConfig displayConfig , ImageListener listener){
        BitmapRequest request = new BitmapRequest(img,uri,displayConfig,listener);
        mQueue.addRequst(request);
    }

    public static interface ImageListener{
        void onComplete(ImageView imageView , Bitmap bitmap , String uri);
    }

    public ImageLoaderConfig getConfig(){
        return mConfig ;
    }
}
