package com.sinieco.lib_imageloader.request;

import android.widget.ImageView;

import com.sinieco.lib_imageloader.config.DisplayConfig;
import com.sinieco.lib_imageloader.config.ImageLoaderConfig;
import com.sinieco.lib_imageloader.core.SimpleImageLoader;
import com.sinieco.lib_imageloader.policy.LoaderPolicy;
import com.sinieco.lib_imageloader.utils.MD5Utils;

import java.lang.ref.SoftReference;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class BitmapRequest implements Comparable<BitmapRequest>{
    private LoaderPolicy loaderPolicy = SimpleImageLoader.getInstance().getConfig().getLoaderPolicy();
    private SoftReference<ImageView> imageViewSoft ;
    //请求编号用于在队列中排队
    private int serialNo ;
    private String imageUri ;
    private DisplayConfig displayConfig = SimpleImageLoader.getInstance().getConfig().getDisplayConfig();
    //图片Uri经过MD5加密后的名字，防止图片中有非法字符不能缓存。
    private String imageUriMD5 ;
    private SimpleImageLoader.ImageListener listener ;
    private SimpleImageLoader.ImageListener imageListener;

    public BitmapRequest(ImageView img, String uri, DisplayConfig displayConfig, SimpleImageLoader.ImageListener listener) {
        this.imageViewSoft = new SoftReference<ImageView>(img);
        //设置tag防止显示错乱
        img.setTag(uri);
        this.imageUri = uri ;
        this.imageUriMD5 = MD5Utils.toMD5(imageUri);
        if(displayConfig != null){
            this.displayConfig = displayConfig ;
        }
        this.listener = listener ;
    }

    @Override
    public int compareTo(BitmapRequest another) {
        //由于会有不同的加载策略，就交给具体加载策略去排队
        return loaderPolicy.compareTo(this,another);
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * 判断如果两个对象的缓存策略相同，编号相同就任务是一个对象
     * 重写equals的套路
     * 1.判断equals的对象是否为null
     * 2.判断equals的对象与当前对象是否一样（==）
     * 3.判断equals的对象是否与当前对象的类型一致
     * 4.强转equals对象，判断二者某些成员变量是否相同
     */

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false ;
        }
        if(this == obj){
            return true ;
        }
        if( !(obj instanceof BitmapRequest)){
            return false;
        }
        BitmapRequest other = (BitmapRequest)obj;
        if(loaderPolicy == null){
            if(other.loaderPolicy != null){
                return false ;
            }
        }else if(!loaderPolicy.equals(other.loaderPolicy)){
            return false ;
        }
        if(serialNo != other.serialNo){
            return false ;
        }
        return true ;
    }

    @Override
    public int hashCode() {
        final int prime = 31 ;
        int result = 1 ;
        result = prime * result +((loaderPolicy == null)?0:loaderPolicy.hashCode());
        result = prime*result+serialNo ;
        return result ;
    }

    public ImageView getImageView(){
        return imageViewSoft.get();
    }

    public String getImageUriMD5() {
        return imageUriMD5;
    }

    public SimpleImageLoader.ImageListener getImageListener() {
        return imageListener;
    }

}
