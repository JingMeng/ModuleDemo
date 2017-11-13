package com.sinieco.lib_volley.volley.download.inter;

import com.sinieco.lib_volley.volley.download.DownLoadItemInfo;

/**
 * @author BaiMeng on 2017/11/9.
 */

public interface IDownloadServiceCallback {
    void onDownloadStatusChanged(DownLoadItemInfo downloadItemInfo);

    void onTotalLengthReceived(DownLoadItemInfo downloadItemInfo);

    void onCurrentSizeChanged(DownLoadItemInfo downloadItemInfo, double downLenth, long speed);

    void onDownloadSuccess(DownLoadItemInfo downloadItemInfo);

    void onDownloadPause(DownLoadItemInfo downloadItemInfo);

    void onDownloadError(DownLoadItemInfo downloadItemInfo, int var2, String var3);
}
