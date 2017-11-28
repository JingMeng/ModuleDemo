package com.sinieco.lib_imageloader.cache;

import android.graphics.Bitmap;

import com.sinieco.lib_imageloader.request.BitmapRequest;

/**
 * @author BaiMeng on 2017/11/27.
 */

public class DoubleCache implements BitmapCache {

    @Override
    public Bitmap get(BitmapRequest request) {
        return null;
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {

    }

    @Override
    public void remove(BitmapRequest request) {
    }
}
