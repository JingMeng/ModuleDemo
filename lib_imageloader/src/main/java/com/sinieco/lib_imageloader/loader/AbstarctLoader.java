package com.sinieco.lib_imageloader.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sinieco.lib_imageloader.cache.BitmapCache;
import com.sinieco.lib_imageloader.config.DisplayConfig;
import com.sinieco.lib_imageloader.config.ImageLoaderConfig;
import com.sinieco.lib_imageloader.core.SimpleImageLoader;
import com.sinieco.lib_imageloader.policy.LoaderPolicy;
import com.sinieco.lib_imageloader.request.BitmapRequest;

import java.util.Calendar;

/**
 * @author BaiMeng on 2017/11/22.
 */

public abstract class AbstarctLoader implements Loader {
    private DisplayConfig mDisplayConfig = SimpleImageLoader.getInstance().getConfig().getDisplayConfig();
    private BitmapCache mBitmapCache = SimpleImageLoader.getInstance().getConfig().getCachePolicy() ;

    @Override
    public void loadImage(BitmapRequest request) {
        //先从缓存中查找图片
        Bitmap bitmap = mBitmapCache.get(request);
        if(bitmap == null){
            //显示加载占位图
            showLoadingImage(request);
            bitmap = onLoad(request);
            cacheBitmap(request,bitmap);
        }
        deliveryToUIThread(request,bitmap);
    }

    private void deliveryToUIThread(final BitmapRequest request, final Bitmap bitmap) {
        ImageView imageView = request.getImageView();
        imageView.post(new Runnable() {
            @Override
            public void run() {
                updateImageView(request,bitmap);
            }
        });
    }

    protected void updateImageView(BitmapRequest request, Bitmap bitmap){
        ImageView imageView = request.getImageView();
        if(bitmap != null && imageView.getTag().equals(request.getImageUri())){
            imageView.setImageBitmap(bitmap);
        }
        if(bitmap == null && hasFailedPlaceHolder()){
            imageView.setImageResource(mDisplayConfig.getFailedImg());
        }
        if(request.getImageListener() != null){
            request.getImageListener().onComplete(imageView,bitmap,request.getImageUri());
        }
    }

    private void cacheBitmap(BitmapRequest request, Bitmap bitmap) {
        if(request != null && bitmap != null){
            synchronized (mBitmapCache){
                mBitmapCache.put(request,bitmap);
            }
        }
    }

    protected abstract Bitmap onLoad(BitmapRequest request);

    protected void showLoadingImage(BitmapRequest request){
        if(hasLoadingPlaceHolder()){
            final ImageView imageView = request.getImageView();
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(mDisplayConfig.getLoadingImg());
                }
            });
        }
    }

    protected boolean hasLoadingPlaceHolder() {
        return (mDisplayConfig != null && mDisplayConfig.loadingImg > 0) ;
    }

    protected boolean hasFailedPlaceHolder(){
        return (mDisplayConfig != null && mDisplayConfig.failedImg >0) ;
    }
}
