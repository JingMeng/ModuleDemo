package com.sinieco.lib_volley.volley.download;

import android.os.Environment;
import android.util.Log;

import com.sinieco.lib_db.BaseDao;
import com.sinieco.lib_db.BaseDaoFactory;
import com.sinieco.lib_volley.volley.Httptask;
import com.sinieco.lib_volley.volley.RequestHolder;
import com.sinieco.lib_volley.volley.ThreadPoolManager;
import com.sinieco.lib_volley.volley.download.en.Priority;
import com.sinieco.lib_volley.volley.download.inter.IDownloadServiceCallback;
import com.sinieco.lib_volley.volley.inter.IHttpListener;
import com.sinieco.lib_volley.volley.inter.IHttpService;

import java.io.File;
import java.util.Map;
import java.util.concurrent.FutureTask;

/**
 * @author BaiMeng on 2017/11/9.
 */

public class FileDownManager {
    private byte [] lock  = new byte [0] ;
    private String mPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"xiazai";
    DownLoadDao mDownLoadDao = (DownLoadDao) BaseDaoFactory.getInstance().getDataHelper(DownLoadDao.class,DownLoadItemInfo.class);
    public void down(String url,IDownloadServiceCallback callback){
        synchronized (lock){
            String[] strs = url.split("/");
            String fileName = strs[strs.length-1];
            File fileParent = new File(mPath);
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }
            File file = new File(fileParent,fileName);
//            if(file.exists()){
//                file.delete();
//            }
            DownLoadItemInfo downLoadItemInfo = new DownLoadItemInfo(url,file.getAbsolutePath());
            RequestHolder requestHolder = new RequestHolder();
            requestHolder.setUrl(url);
            IHttpService httpService = new FileDownService();
            Map<String, String> header = httpService.getHttpHeadMap();
            IHttpListener httpListener = new DownLoadListener(downLoadItemInfo,callback,httpService);
            requestHolder.setHttpListener(httpListener);
            requestHolder.setHttpService(httpService);
            Httptask httptask = new Httptask(requestHolder);
            try{
                ThreadPoolManager.getInstance().excute(new FutureTask<Object>(httptask,null));
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
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
        downLoadItemInfo =
    }
}
