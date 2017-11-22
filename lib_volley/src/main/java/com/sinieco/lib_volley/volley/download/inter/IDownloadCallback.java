package com.sinieco.lib_volley.volley.download.inter;

import com.sinieco.lib_volley.volley.download.DownloadStatus;

/**
 * @author BaiMeng on 2017/11/20.
 */

public interface IDownloadCallback {
    /**
     * 新增下载任务
     * @param downloadId
     */
    void onDownloadInfoAdd(int downloadId);

    /**
     * 移除下载任务
     * @param downloadId
     */
    void onDownloadInfoRemove(int downloadId);

    /**
     * 下载任务的状态发生变化
     * @param downloadId
     * @param status
     */
    void onDownloadStatusChange(int downloadId, DownloadStatus status);

    /**
     * 获取到下载任务的总长度
     * @param downloadId
     * @param totalLength
     */
    void onReceivedTotalLength(int downloadId, long totalLength);

    /**
     * 下载进度监听
     * @param downloadId
     * @param downloadPercent
     * @param downloadSpeed
     */
    void onReceivedCurrentProgress(int downloadId, double downloadPercent , long downloadSpeed);


    void onDownloadSuccess(int downloadId);


    void onDownLoadError(int downloadId, int errorCode , String errorMsg);
}
