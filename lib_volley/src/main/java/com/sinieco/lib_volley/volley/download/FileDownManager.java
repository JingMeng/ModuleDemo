package com.sinieco.lib_volley.volley.download;

import android.database.sqlite.SQLiteDoneException;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.sinieco.lib_db.BaseDao;
import com.sinieco.lib_db.BaseDaoFactory;
import com.sinieco.lib_volley.volley.Httptask;
import com.sinieco.lib_volley.volley.RequestHolder;
import com.sinieco.lib_volley.volley.ThreadPoolManager;
import com.sinieco.lib_volley.volley.download.en.DownloadStopMode;
import com.sinieco.lib_volley.volley.download.en.Priority;
import com.sinieco.lib_volley.volley.download.inter.IDownListener;
import com.sinieco.lib_volley.volley.download.inter.IDownloadCallback;
import com.sinieco.lib_volley.volley.download.inter.IDownloadServiceCallback;
import com.sinieco.lib_volley.volley.inter.IHttpListener;
import com.sinieco.lib_volley.volley.inter.IHttpService;
import com.sinieco.moduledemo.utils.LogUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static android.content.ContentValues.TAG;

/**
 * @author BaiMeng on 2017/11/9.
 */

public class FileDownManager implements IDownloadServiceCallback {
    private byte [] lock  = new byte [0] ;
    private String mPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"xiazai";
    DownLoadDao mDownLoadDao = (DownLoadDao) BaseDaoFactory.getInstance().getDataHelper(DownLoadDao.class,DownLoadItemInfo.class);
    private final List<IDownloadCallback> downListeners = new CopyOnWriteArrayList<IDownloadCallback>();
    private static List<DownLoadItemInfo> downloadFileTaskList = new CopyOnWriteArrayList<DownLoadItemInfo>();
    private Handler handler = new Handler(Looper.getMainLooper());
    public DownLoadItemInfo reallyDown(DownLoadItemInfo downLoadItemInfo){
        synchronized (lock){
            String fileName = downLoadItemInfo.getDisplayName() ;
            File fileParent = new File(mPath);
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }
            File file = new File(fileParent,fileName);
            RequestHolder requestHolder = new RequestHolder();
            requestHolder.setUrl(downLoadItemInfo.getmUrl());
            IHttpService httpService = new FileDownService();
            Map<String, String> header = httpService.getHttpHeadMap();
            downLoadItemInfo.setmStatus(DownloadStatus.downloading.getValue());
            IHttpListener httpListener = new DownLoadListener(downLoadItemInfo,this,httpService);
            requestHolder.setHttpListener(httpListener);
            requestHolder.setHttpService(httpService);
            Httptask httptask = new Httptask(requestHolder);
            try{
                ThreadPoolManager.getInstance().excute(new FutureTask<Object>(httptask,null));
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return downLoadItemInfo;
    }

    public int download(String url){
        String[] splits = url.split("/");
        return download(url,mPath+File.separator+splits[splits.length-1]);
    }

    public int download(String url , String filePath){
        String[] splits = url.split("/");
        return download(url,filePath,splits[splits.length-1]);
    }

    public int download(String url , String filePath , String displayName){
        return download(url ,filePath , displayName ,Priority.middle);
    }

    public int download(String url , String filePath, String displayName , Priority priority){
        if(priority == null){
            priority = Priority.low;
        }
        File file = new File(filePath);
        DownLoadItemInfo downLoadItemInfo = null ;
        downLoadItemInfo = mDownLoadDao.findRecord(url,filePath);
        //之前没有添加任务，数据库中也没有下载记录
        if(downLoadItemInfo == null) {
            List<DownLoadItemInfo> samesFile = mDownLoadDao.findRecord(filePath);
            //有开始下载，但是没有下载完成的文件
            if (samesFile.size() > 0) {
                DownLoadItemInfo sameDown = samesFile.get(0);
                //如果文件当前的长度等于文件的总长度
                if (sameDown.getCurrentLength().equals(sameDown.getTotalLength())) {
                    synchronized (downListeners) {
                        for (IDownloadCallback downListener : downListeners) {
                            downListener.onDownLoadError(sameDown.getId(), 2, "当前任务已下载完成");
                        }
                    }
                }
            }

            //添加一条记录，只有当添加成功时返回值不为null，如果数据库已有记录，返回值为null
            downLoadItemInfo = mDownLoadDao.addRecord(url, filePath, displayName, priority.getValue());
            if (downLoadItemInfo != null) {
                synchronized (downListeners) {
                    for (IDownloadCallback downListener : downListeners) {
                        downListener.onDownloadInfoAdd(downLoadItemInfo.getId());
                    }
                }
            } else {
                downLoadItemInfo = mDownLoadDao.findRecord(url, filePath);
            }

        }
        if(isDowning(file.getAbsolutePath())){
            synchronized (downListeners){
                for (IDownloadCallback downListener : downListeners) {
                    downListener.onDownLoadError(downLoadItemInfo.getId(),4,"文件正在排队下载中");
                }
            }
            return downLoadItemInfo.getId();
        }

        if(downLoadItemInfo != null){
            downLoadItemInfo.setPriority(priority.getValue());
            if(downLoadItemInfo.getStatus() != DownloadStatus.finish.getValue()){
                if(downLoadItemInfo.getTotalLength() == 0L || file.length() == 0L){
                    downLoadItemInfo.setmStatus(DownloadStatus.failed.getValue());
                }
                if(downLoadItemInfo.getTotalLength() == file.length() && downLoadItemInfo.getTotalLength() != 0L){
                    downLoadItemInfo.setmStatus(DownloadStatus.finish.getValue());
                    synchronized (downListeners){
                        for (IDownloadCallback downListener : downListeners) {
                            try{
                                downListener.onDownLoadError(downLoadItemInfo.getId(),4,"文件已经下载完成");
                            }catch(Exception e){

                            }
                        }
                    }
                }
            }
            mDownLoadDao.updateRecord(downLoadItemInfo);
        }

        if(downLoadItemInfo.getStatus() == DownloadStatus.finish.getValue()){
            final int downId = downLoadItemInfo.getId();
            synchronized (downLoadItemInfo){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (IDownloadCallback downListener : downListeners) {
                            downListener.onDownloadStatusChange(downId,DownloadStatus.finish);
                        }
                    }
                });
            }
            //从下载任务集合中移除
            mDownLoadDao.removeRecordById(downId);
            return downLoadItemInfo.getId();
        }
        List<DownLoadItemInfo> allDowning = downloadFileTaskList;
        if(priority != Priority.high){
            for (DownLoadItemInfo downing : allDowning) {
                downing = mDownLoadDao.findSigleRecord(downing.getFilePath());
                //如果此任务优先级为最高级，且正在下载。
                if(downLoadItemInfo != null && downLoadItemInfo.getPriority()==Priority.high.getValue()){
                    if(downLoadItemInfo.getFilePath().equals(downing.getFilePath())){
                        for (IDownloadCallback downListener : downListeners) {
                            downListener.onDownLoadError(downLoadItemInfo.getId(),5,"此任务正在下载，请勿重复添加");
                        }
                        return downLoadItemInfo.getId();
                    }
                   }
            }
        }
        DownLoadItemInfo newDownloadInfo = reallyDown(downLoadItemInfo);
        //遍历正在下载的任务，暂停优先级较低的任务。
        if(priority == Priority.high || priority == Priority.middle){
            synchronized (allDowning){
                for (DownLoadItemInfo loadItemInfo : allDowning) {
                    if(!downLoadItemInfo.getFilePath().equals(loadItemInfo.getFilePath())){
                        DownLoadItemInfo downingInfo = mDownLoadDao.findSigleRecord(loadItemInfo.getFilePath());
                        if(downingInfo != null){
                            pause(downLoadItemInfo.getId(), DownloadStopMode.auto);
                        }
                    }
                }
            }
            return downLoadItemInfo.getId();
        }
        return -1 ;
    }

    public void pause(Integer downloadItemId ){
        pause(downloadItemId,DownloadStopMode.hand);
    }

    /**
     * 暂停任务
     * @param downloadItemId
     * @param mode
     */
    private void pause(Integer downloadItemId, DownloadStopMode mode) {
        if(mode == null){
            mode = DownloadStopMode.auto ;
        }
        DownLoadItemInfo record = mDownLoadDao.findRecord(downloadItemId);
        if(record != null){
            if(record != null){
                record.setStopMode(mode.getValue());
                record.setmStatus(DownloadStatus.pause.getValue());
                mDownLoadDao.updateRecord(record);
            }
            for (DownLoadItemInfo downing : downloadFileTaskList) {
                if(downloadItemId.equals(downing.getId()) ){
                    downing.getHttptask().pause();
                }
            }

        }

    }

    private boolean isDowning(String absolutePath) {
        for (DownLoadItemInfo downLoadItemInfo : downloadFileTaskList) {
            if(downLoadItemInfo.getFilePath().equals(absolutePath)){
                return true ;
            }
        }
        return false ;
    }

    public void addDownloadListener(IDownloadCallback listener){
        synchronized (downListeners){
            if(!downListeners.contains(listener)){
                downListeners.add(listener);
            }
        }
    }

    @Override
    public void onDownloadStatusChanged(DownLoadItemInfo downloadItemInfo) {
        Log.e("------------>>>>>>","下载状态改变"+downloadItemInfo.toString());
        mDownLoadDao.updateRecord(downloadItemInfo);
    }

    @Override
    public void onTotalLengthReceived(DownLoadItemInfo downloadItemInfo) {
    }

    @Override
    public void onCurrentSizeChanged(DownLoadItemInfo downloadItemInfo, double downLenth, long speed) {
        Log.e("------------>>>>>>","接收size改变"+downloadItemInfo.toString());
        mDownLoadDao.updateRecord(downloadItemInfo);
    }

    @Override
    public void onDownloadSuccess(DownLoadItemInfo downloadItemInfo) {
        Log.e("------------>>>>>>","下载成功"+downloadItemInfo.toString());
        mDownLoadDao.updateRecord(downloadItemInfo);
    }

    @Override
    public void onDownloadPause(DownLoadItemInfo downloadItemInfo) {
        LogUtils.e("下载暂停");
        Log.e("------------>>>>>>","下载暂停"+downloadItemInfo.toString());
        mDownLoadDao.updateRecord(downloadItemInfo);
    }

    @Override
    public void onDownloadError(DownLoadItemInfo downloadItemInfo, int var2, String var3) {
        mDownLoadDao.updateRecord(downloadItemInfo);
    }
}
