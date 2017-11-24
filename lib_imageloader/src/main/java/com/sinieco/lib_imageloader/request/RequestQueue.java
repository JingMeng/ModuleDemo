package com.sinieco.lib_imageloader.request;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class RequestQueue {
    private int threadCount ;
    //优先级阻塞队列
    private PriorityBlockingQueue<BitmapRequest> mBlockingQueue = new PriorityBlockingQueue<>();
    private AtomicInteger ai = new AtomicInteger(0);
    private RequestDispatcher [] mDispatcher ;

    public RequestQueue(int threadCount) {
        this.threadCount = threadCount;
    }


    public void addRequst(BitmapRequest request) {
        if(!mBlockingQueue.contains(request)){
            request.setSerialNo(ai.incrementAndGet());
            mBlockingQueue.add(request);
            Log.e("RequestQueue","添加请求"+request.getSerialNo());
        }else {
            Log.e("RequestQueue","请求已经存在"+request.getSerialNo());
        }
    }

    public void start() {
        stop();
        startDispatcher();
    }

    /**
     * 初始化RequestDispatcher数组，所有的RequestDispatcher共享一个阻塞队列
     */
    private void startDispatcher() {
        mDispatcher = new RequestDispatcher[threadCount];
        for(int i = 0 ; i < threadCount ; i++){
            mDispatcher[i] = new RequestDispatcher(mBlockingQueue);
            mDispatcher[i].start();
        }
    }

    /**
     *打断线程
     */
    private void stop() {
        if(mDispatcher != null && mDispatcher.length > 0){
            for (RequestDispatcher requestDispatcher : mDispatcher) {
                requestDispatcher.interrupt();
            }
        }
    }
}
