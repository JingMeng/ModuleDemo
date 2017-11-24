package com.sinieco.lib_imageloader.cache;

import android.graphics.Bitmap;

import com.sinieco.lib_imageloader.request.BitmapRequest;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class MemoryCache implements BitmapCache {
    @Override
    public Bitmap get(BitmapRequest request) {
        return null;
    }

    @Override
    public boolean put(BitmapRequest request, Bitmap bitmap) {
        return false;
    }

    @Override
    public boolean remove(BitmapRequest request) {
        return false;
    }
}
