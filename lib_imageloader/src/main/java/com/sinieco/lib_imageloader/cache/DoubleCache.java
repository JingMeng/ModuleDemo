package com.sinieco.lib_imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.sinieco.lib_imageloader.request.BitmapRequest;

/**
 * @author BaiMeng on 2017/11/27.
 */

public class DoubleCache implements BitmapCache {

    private MemoryCache mMemoryCache = new MemoryCache();
    private DiskCache mDiskCache ;

    public DoubleCache(Context context) {
        mDiskCache = DiskCache.getInstance(context);
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        Bitmap bitmap = mMemoryCache.get(request);
        if(bitmap == null){
            bitmap = mDiskCache.get(request);
            if(bitmap != null){
                mMemoryCache.put(request,bitmap);
            }
        }
        return bitmap;
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        mMemoryCache.put(request,bitmap);
        mDiskCache.put(request,bitmap);
    }

    @Override
    public void remove(BitmapRequest request) {
        mMemoryCache.remove(request);
        mDiskCache.remove(request);
    }
}
