package com.sinieco.lib_imageloader.config;

import com.sinieco.lib_imageloader.cache.BitmapCache;
import com.sinieco.lib_imageloader.cache.NoCache;
import com.sinieco.lib_imageloader.policy.LoaderPolicy;
import com.sinieco.lib_imageloader.policy.SerialPolicy;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class ImageLoaderConfig {
    //缓存策略
    private BitmapCache cachePolicy = new NoCache();
    //加载策略（顺序加载还是倒序加载）
    private LoaderPolicy loaderPolicy = new SerialPolicy();
    //线程数默认是CPU核心数
    private int threadCount = Runtime.getRuntime().availableProcessors();
    //图片加载中和加载失败的占为图
    private DisplayConfig displayConfig = new DisplayConfig();
    private ImageLoaderConfig(){};
    public static class Builder{
        ImageLoaderConfig config ;
        public Builder(){
            config = new ImageLoaderConfig();
        }

        public Builder setCacheConfig(BitmapCache cachePolicy){
            config.cachePolicy = cachePolicy ;
            return this ;
        }

        public Builder setLoaderConfig(LoaderPolicy loaderPolicy){
            config.loaderPolicy = loaderPolicy ;
            return this ;
        }

        public Builder setThreadCount(int threadCount){
            config.threadCount = threadCount ;
            return this ;
        }

        public Builder setLoadingImage(int resId){
            config.displayConfig.setLoadingImg(resId);
            return this ;
        }

        public Builder setFailedImage(int resId){
            config.displayConfig.setFailedImg(resId);
            return this ;
        }

        public ImageLoaderConfig build(){
            return config ;
        }
    }

    public BitmapCache getCachePolicy() {
        return cachePolicy;
    }

    public LoaderPolicy getLoaderPolicy() {
        return loaderPolicy;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }
}
