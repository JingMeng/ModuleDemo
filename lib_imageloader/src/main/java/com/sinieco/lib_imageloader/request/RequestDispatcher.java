package com.sinieco.lib_imageloader.request;


import android.util.Log;

import com.sinieco.lib_imageloader.loader.Loader;
import com.sinieco.lib_imageloader.loader.LoaderManager;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class RequestDispatcher extends Thread {
    private PriorityBlockingQueue<BitmapRequest> mQueue ;
    public RequestDispatcher(PriorityBlockingQueue<BitmapRequest> mBlockingQueue) {
        this.mQueue = mBlockingQueue ;
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            try {
                BitmapRequest request = mQueue.take();
                String schema = parseSchema(request.getImageUri());
                Loader loader = LoaderManager.getInstance().getLoader(schema);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String parseSchema(String imageUri) {
        if(imageUri.contains("://")){
            return imageUri.split("://")[0];
        }else {
            Log.e("RequestDispatcher","图片的Uri为"+imageUri);
        }
        return null ;
    }
}
