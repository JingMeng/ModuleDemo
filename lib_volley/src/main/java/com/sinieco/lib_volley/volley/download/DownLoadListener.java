package com.sinieco.lib_volley.volley.download;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.sinieco.lib_volley.BuildConfig;
import com.sinieco.lib_volley.volley.download.inter.IDownListener;
import com.sinieco.lib_volley.volley.download.inter.IDownloadServiceCallback;
import com.sinieco.lib_volley.volley.inter.IHttpService;
import com.sinieco.moduledemo.utils.LogUtils;

import org.apache.http.HttpEntity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author BaiMeng on 2017/11/9.
 */

public class DownLoadListener implements IDownListener {

    private DownLoadItemInfo mDownLoadItmeInfo ;
    private File mFile ;
    protected String mUrl ;
    private long mBreakPoint ;
    private IDownloadServiceCallback mIDownloadServiceCallback ;
    private IHttpService mHttpService ;
    private Handler handler = new Handler(Looper.getMainLooper());

    public DownLoadListener(DownLoadItemInfo downLoadItmeInfo, IDownloadServiceCallback downloadServiceCallback, IHttpService httpService) {
        this.mDownLoadItmeInfo = downLoadItmeInfo;
        this.mIDownloadServiceCallback = downloadServiceCallback;
        this.mHttpService = httpService;
        this.mFile = new File(downLoadItmeInfo.getFilePath());
        this.mBreakPoint = mFile.length();
    }

    @Override
    public void onSuccess(HttpEntity entity) {
        mDownLoadItmeInfo.setTotalLength(entity.getContentLength());
        InputStream is = null ;
        try {
            is = entity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long startTime = System.currentTimeMillis() ;
        long speed = 0L;
        long useTime = 0L ;
        long getLen = 0L ;
        long receiveLen = 0L ;
        boolean bufferLen = false ;
        long dataLength = entity.getContentLength();
        long calcSpeedLen = 0L ;
        long totalLength = this.mBreakPoint + dataLength ;
        this.receviceTotalLength (totalLength);
        this.downloadStatusChange(DownloadStatus.downloading);
        byte[] buffer = new byte[1024] ;
        int count = 0 ;
        long currentTime = System.currentTimeMillis() ;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null ;

        try {
            fos = new FileOutputStream(mFile, true);
            bos = new BufferedOutputStream(fos);
            int length = 1;
            while ((length = is.read(buffer)) != -1) {
                if (this.mHttpService.isCancle()) {
                    mDownLoadItmeInfo.setmStatus(DownloadStatus.failed.getValue());
                    mIDownloadServiceCallback.onDownloadError(mDownLoadItmeInfo, 1, "用户取消了");
                    return;
                }

                if (this.mHttpService.isPause()) {
                    mDownLoadItmeInfo.setmStatus(DownloadStatus.pause.getValue());
                    mIDownloadServiceCallback.onDownloadError(mDownLoadItmeInfo, 2, "用户暂停了");
                    return;
                }

                bos.write(buffer, 0, length);
                getLen += (long) length;
                receiveLen += (long) length;
                calcSpeedLen += (long) length;
                ++count;
                if (receiveLen * 100L / totalLength >= 1L || count >= 5000) {
                    currentTime = System.currentTimeMillis();
                    useTime = currentTime - startTime;
                    startTime = currentTime;
                    speed = 1000L * calcSpeedLen / useTime;
                    count = 0;
                    calcSpeedLen = 0L;
                    receiveLen = 0L;
                    this.downloadLengthChange(this.mBreakPoint + getLen, totalLength, speed);
                }
            }
            bos.close();
            is.close();
            if (dataLength != getLen) {
                mDownLoadItmeInfo.setmStatus(DownloadStatus.failed.getValue());
                mIDownloadServiceCallback.onDownloadError(mDownLoadItmeInfo, 3, "下载长度不相等");
            } else {
                mDownLoadItmeInfo.setmStatus(DownloadStatus.finish.getValue());
                this.downloadLengthChange(this.mBreakPoint + getLen, totalLength, speed);
                this.mIDownloadServiceCallback.onDownloadSuccess(mDownLoadItmeInfo.copy());
            }
            LogUtils.e("流读写完成");
            mDownLoadItmeInfo.setmStatus(DownloadStatus.finish.getValue());
            mIDownloadServiceCallback.onDownloadSuccess(mDownLoadItmeInfo);

        } catch (IOException ioException) {
            LogUtils.e("IO异常    "+ioException.getMessage());
            if (this.mHttpService != null) {
//                this.getHttpService().abortRequest();
            }
            return;
        } catch (Exception e) {
            LogUtils.e("发生异常    "+e.getMessage());
            if (this.mHttpService != null) {
//                this.getHttpService().abortRequest();
            }
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }

                if (entity != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    private void downloadLengthChange(final long downLength, final long totalLength, final long speed) {
        mDownLoadItmeInfo.setCurrentLength(downLength);
        if(mIDownloadServiceCallback != null){
            DownLoadItemInfo copyLoadItemInfo = mDownLoadItmeInfo.copy();
            synchronized (this.mIDownloadServiceCallback){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mIDownloadServiceCallback.onCurrentSizeChanged(mDownLoadItmeInfo,downLength/totalLength,speed);
                    }
                });
            }
        }
    }

    private void downloadStatusChange(DownloadStatus downloading) {
        mDownLoadItmeInfo.setmStatus(downloading.getValue());
        final DownLoadItemInfo copyLoadItemInfo = mDownLoadItmeInfo.copy();
        if(mIDownloadServiceCallback != null){
            synchronized (this.mIDownloadServiceCallback){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mIDownloadServiceCallback.onDownloadStatusChanged(copyLoadItemInfo);
                    }
                });
            }
        }
    }

    private void receviceTotalLength(long totalLength) {
        mDownLoadItmeInfo.setCurrentLength(totalLength);
        final DownLoadItemInfo copyLoadItmeInfo = mDownLoadItmeInfo.copy();
        if(mIDownloadServiceCallback != null){
            synchronized (this.mIDownloadServiceCallback){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mIDownloadServiceCallback.onTotalLengthReceived(copyLoadItmeInfo);
                    }
                });
            }
        }
    }

    @Override
    public void onFail() {

    }

    @Override
    public void addHeader(Map headerMap) {
        long length = mFile.length();
        Log.e("addHeader","------------->>>>>>>>>>"+length);
        if(length > 0L ){
            headerMap.put("RANGE","bytes="+length+"-");
        }
    }

    @Override
    public void setHttpService(IHttpService httpService) {

    }

    @Override
    public void setCancleCalle() {

    }

    @Override
    public void setPauseCallble() {

    }
}
