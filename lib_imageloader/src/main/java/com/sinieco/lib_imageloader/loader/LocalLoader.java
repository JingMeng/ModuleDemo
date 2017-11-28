package com.sinieco.lib_imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.sinieco.lib_imageloader.request.BitmapRequest;
import com.sinieco.lib_imageloader.utils.BitmapDecoder;
import com.sinieco.lib_imageloader.utils.ImageViewHelper;

import java.io.File;
import java.util.logging.FileHandler;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class LocalLoader extends AbstarctLoader {

    @Override
    protected Bitmap onLoad(BitmapRequest request) {
        final String path = Uri.parse(request.getImageUri()).getPath();
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        BitmapDecoder decoder = new BitmapDecoder() {
            @Override
            protected Bitmap decodeBitmapWithOptions(BitmapFactory.Options potions) {
                return BitmapFactory.decodeFile(path,potions);
            }
        };
        return decoder.decodeBitmap(ImageViewHelper.getImageViewWidth(request.getImageView()),ImageViewHelper.getImageViewHeight(request.getImageView()));
    }
}
