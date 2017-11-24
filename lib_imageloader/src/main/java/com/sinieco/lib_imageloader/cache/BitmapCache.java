package com.sinieco.lib_imageloader.cache;

import android.graphics.Bitmap;

import com.sinieco.lib_imageloader.request.BitmapRequest;

/**
 * @author BaiMeng on 2017/11/22.
 */

public interface BitmapCache {
    Bitmap get(BitmapRequest request);
    boolean put (BitmapRequest request ,Bitmap bitmap);
    boolean remove(BitmapRequest request);
}
