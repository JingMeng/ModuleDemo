package com.sinieco.lib_imageloader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.*;
import android.util.Log;

/**
 * @author BaiMeng on 2017/11/27.
 */

public abstract class BitmapDecoder {

    protected abstract Bitmap decodeBitmapWithOptions(Options potions);

    /**
     * 压缩图片
     * @param reqWidth    ImageView的宽
     * @param reqHeight   ImageView的高
     * @return
     */
    public Bitmap decodeBitmap(int reqWidth , int reqHeight){
        Options options = new Options();
        //设置只读取边界
        options.inJustDecodeBounds = true ;
        //第一次通过流获取Bitmap
        decodeBitmapWithOptions(options);
        //计算图片缩放比例
        calculateSampleSizeWithOptions(options,reqWidth,reqHeight);
        //第二次通过流获取Bitmap
        return decodeBitmapWithOptions(options) ;
    }

    //计算宽高并给Options设置缩放比例
    private void calculateSampleSizeWithOptions(Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth ;
        int height = options.outHeight ;
        //缩放比例
        int inSimpleSize = 1 ;
        //图片比ImageView大,求图片是ImageView大小的几倍，应该按倍数大的进行缩放
        //否则，倍数大的边不能显示完全
        if(width > reqHeight || height > reqHeight){
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSimpleSize = Math.max(heightRatio,widthRatio);
        }
        options.inSampleSize = inSimpleSize ;
        //设置图片质量，每个像素两个字节
        options.inPreferredConfig = Bitmap.Config.RGB_565 ;
        options.inJustDecodeBounds = false ;
        //内存不足时可以回收Bitmap
        options.inPurgeable = true ;
        options.inInputShareable = true ;
    }

}
