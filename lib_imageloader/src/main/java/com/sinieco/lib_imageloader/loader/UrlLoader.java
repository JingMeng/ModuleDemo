package com.sinieco.lib_imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.sinieco.lib_imageloader.request.BitmapRequest;
import com.sinieco.lib_imageloader.utils.BitmapDecoder;
import com.sinieco.lib_imageloader.utils.ImageViewHelper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class UrlLoader extends AbstarctLoader {

    @Override
    protected Bitmap onLoad(BitmapRequest request) {
        HttpURLConnection connection = null ;
        InputStream is = null ;
        try {
            connection = (HttpURLConnection) new URL(request.getImageUri()).openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            //从可用的字节开始（排除图片信息占用的字节）
            is.mark(is.available());
            final InputStream inputStream = is ;
            BitmapDecoder decoder = new BitmapDecoder() {
                @Override
                public Bitmap decodeBitmapWithOptions(BitmapFactory.Options potions) {
                    /**
                     * 此函数是一个出参入参函数，将options传入，函数给options赋值
                     */
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,potions);
                    if(potions.inJustDecodeBounds){
                        //第一次读流后重置
                        try {
                            inputStream.reset();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        //第二次读流后关闭流
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return bitmap;
                }
            };
            return decoder.decodeBitmap(ImageViewHelper.getImageViewWidth(request.getImageView()),ImageViewHelper.getImageViewHeight(request.getImageView()));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
            try {
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
