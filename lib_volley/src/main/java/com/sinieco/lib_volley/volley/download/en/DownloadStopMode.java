package com.sinieco.lib_volley.volley.download.en;

/**
 * @author BaiMeng on 2017/11/17.
 */

public enum DownloadStopMode {
    auto(0),
    hand(1);
    int value ;
    DownloadStopMode(int value){
        this.value = value ;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value){
        this.value = value ;
    }

    public static DownloadStopMode getInstance(int value){
        for (DownloadStopMode mode : DownloadStopMode.values()) {
            if(value == mode.getValue()){
                return mode ;
            }
        }
        return DownloadStopMode.auto ;
    }
}
