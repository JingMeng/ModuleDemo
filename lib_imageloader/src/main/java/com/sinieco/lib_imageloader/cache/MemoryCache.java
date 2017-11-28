package com.sinieco.lib_imageloader.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.sinieco.lib_imageloader.request.BitmapRequest;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class MemoryCache implements BitmapCache {
    private LruCache<String,Bitmap> mLruCache ;

    public MemoryCache(){
        //当前可用内存的8/1
        int maxSize  = (int)Runtime.getRuntime().freeMemory()*1024/8;
        mLruCache = new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getHeight()*value.getRowBytes()/1024;
            }
        };
    }
    @Override
    public Bitmap get(BitmapRequest request) {
        return mLruCache.get(request.getImageUriMD5());
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        mLruCache.put(request.getImageUriMD5(),bitmap);
    }

    @Override
    public void remove(BitmapRequest request) {
        mLruCache.remove(request.getImageUriMD5());
    }
}
