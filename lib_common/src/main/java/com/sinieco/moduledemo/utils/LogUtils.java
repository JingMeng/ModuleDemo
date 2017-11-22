package com.sinieco.moduledemo.utils;

import android.util.Log;

import com.sinieco.module_app.BuildConfig;


/**
 * Created by BaiMeng on 2017/1/12.
 */
public class LogUtils {
    static String className;
    static String methodName;
    static int lineNumber;
    private LogUtils (){
    }
    public static boolean isDebuggable(){
        return BuildConfig.DEBUG;
    }
    private static String createLog(String log){
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }
    private static void getMethodNames(StackTraceElement[] sElements){
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }
    //错误日志
    public static void e (String message){
        //正式版不打印log
        if(!isDebuggable()){
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className,createLog(message));
    }
    //提示日志
    public static void i (String message){
        if(!isDebuggable()){
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className,createLog(message));
    }
    //警告日志
    public static void w (String message){
        if(!isDebuggable()){
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.w(className,createLog(message));
    }
    //调试日志
    public static void d (String message){
        if(!isDebuggable()){
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.d(className,createLog(message));
    }
    //啰嗦日志
    public static void v (String message){
        if(!isDebuggable()){
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.v(className,createLog(message));
    }
    public static void longLog( String msg){
        if(!isDebuggable()){
            return;
        }

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.i(className, msg);
        }else {
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                Log.i(className, logContent);
            }
            Log.i(className, msg);// 打印剩余日志
        }
    }
}
