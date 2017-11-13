package com.sinieco.lib_volley.volley.download;

/**
 * @author BaiMeng on 2017/11/9.
 */

public enum  DownloadStatus {
    waitting(0),
    starting(1),
    downloading(2),
    pause(3),
    finish(4),
    failed(5);
    private int value ;
    private DownloadStatus(int value){
        this.value = value ;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value ;
    }
}
