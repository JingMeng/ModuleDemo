package com.sinieco.lib_imageloader.utils;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * @author BaiMeng on 2017/11/27.
 */

public class ImageViewHelper {

    private static int DEFAULT_WIDTH = 200 ;
    private static int DEFAULT_HEIGHT = 200 ;

    /**
     * 获取ImageView控件的宽度
     * 1.getWidth（绘制完成，如果视图没有绘制完成没有值）
     * 2.layout_width（有可能设置的是WRAP_CONTENT）
     * 3.maxWidth
     * @param imageView
     * @return
     */
    public static int getImageViewWidth(ImageView imageView){
        if(imageView != null){
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            int width = 0 ;
            if(params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT){
                width = imageView.getWidth() ;
            }
            if(width <= 0 && params != null){
                width = params.width ;
            }
            if(width <= 0){
                width = getImageViewFieldValue(imageView ,"mMaxWidth");
            }
            return width ;
        }
        return DEFAULT_WIDTH ;
    }

    public static int getImageViewHeight(ImageView imageView){
        if(imageView != null){
            int height = 0 ;
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            if(params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT){
                height = imageView.getHeight() ;
            }
            if(height <= 0 && params != null ){
                height = params.height ;
            }
            if(height <= 0){
                height =getImageViewFieldValue(imageView,"mMaxHeight");
            }
            return height ;
        }

        return DEFAULT_HEIGHT ;
    }

    //获取ImageView中对应属性的值
    private static int getImageViewFieldValue(ImageView imageView, String fieldName)  {
        Field field = null;
        try {
            field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Integer maxValue = (Integer)field.get(imageView);
            if(maxValue > 0 && maxValue < Integer.MAX_VALUE){
                return maxValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
