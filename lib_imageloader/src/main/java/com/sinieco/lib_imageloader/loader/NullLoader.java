package com.sinieco.lib_imageloader.loader;

import android.graphics.Bitmap;
import android.util.Log;

import com.sinieco.lib_imageloader.request.BitmapRequest;

/**
 * @author BaiMeng on 2017/11/24.
 */

public class NullLoader extends AbstarctLoader {

    @Override
    protected Bitmap onLoad(BitmapRequest request) {
        Log.e("NullLoader","无法加载图片");
        return null;
    }
}
