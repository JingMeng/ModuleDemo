package com.sinieco.lib_imageloader.config;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class DisplayConfig {
    public int loadingImg = -1 ;
    public int failedImg = -1 ;

    public int getLoadingImg() {
        return loadingImg;
    }

    public void setLoadingImg(int loadingImg) {
        this.loadingImg = loadingImg;
    }

    public int getFailedImg() {
        return failedImg;
    }

    public void setFailedImg(int failedImg) {
        this.failedImg = failedImg;
    }
}
