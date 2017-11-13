package com.sinieco.lib_volley.volley.download;

import com.sinieco.lib_volley.volley.Httptask;

/**
 * @author BaiMeng on 2017/11/9.
 */

public class DownLoadItemInfo extends BaseEntity<DownLoadItemInfo> {
    private String mUrl ;
    private String mFilePath ;
    private DownloadStatus mStatus ;
    private long mCurrentLength ;
    private long mTotalLength ;
    private transient Httptask mHttptask ;

    public void setHttptask(Httptask httptask){
        this.mHttptask = httptask ;
    }

    public Httptask getHttptask(){
        return mHttptask ;
    }

    public void setTotalLength (long totalLength){
        this.mTotalLength = totalLength ;
    }

    public long getTotalLength(){
        return mTotalLength ;
    }

    public void setCurrentLength (long currentLength){
        this.mCurrentLength = currentLength ;
    }

    public long getCurrentLength(){
        return mCurrentLength ;
    }

    public void setStatus(DownloadStatus status){
        this.mStatus = status ;
    }

    public DownloadStatus getStatus (){
        return mStatus ;
    }

    public DownLoadItemInfo(String url, String filePath) {
        this.mUrl = url;
        this.mFilePath = filePath;
    }

    public String getFilePath(){
        return mFilePath ;
    }
}
