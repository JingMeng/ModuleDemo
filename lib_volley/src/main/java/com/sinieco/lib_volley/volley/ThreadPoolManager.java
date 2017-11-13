package com.sinieco.lib_volley.volley;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by BaiMeng on 2017/11/3.
 */
public class ThreadPoolManager  {


    private ThreadPoolExecutor executor ;
    private LinkedBlockingQueue<Future<?>> queue = new LinkedBlockingQueue() ;
    private static ThreadPoolManager mInstance = new ThreadPoolManager();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true){
                FutureTask task = null ;
                try {
                    task  = (FutureTask) queue.take();
                    if (task!=null){
                        executor.execute(task);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private RejectedExecutionHandler handler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                queue.put(new FutureTask<Object>(r,null) {
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public static ThreadPoolManager getInstance(){
        return mInstance ;
    }
    private ThreadPoolManager (){
        executor = new ThreadPoolExecutor(4,10,10, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(4),handler);
        executor.execute(runnable);
    }

    public<T> void putTask(FutureTask<T> task ) throws InterruptedException {
        queue.put(task);
    }

    public<T> void excute(FutureTask<T> task) throws InterruptedException {
        putTask(task);
    }

}
