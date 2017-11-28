package com.sinieco.lib_imageloader.cache;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.sinieco.lib_imageloader.disk.DiskLruCache;
import com.sinieco.lib_imageloader.disk.IOUtil;
import com.sinieco.lib_imageloader.request.BitmapRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class DiskCache implements BitmapCache {
    private static DiskCache mDiskCache ;
    private String mCacheDir = "image_cache" ;
    private static final int MB = 1024*1024 ;
    //支持Lru算法的硬盘缓存，jake wharton出品
    private DiskLruCache mDiskLruCache ;

    private DiskCache(Context context){
        initDiskCache(context);
    }

    private void initDiskCache(Context context) {
        File directory = getDiskCacheDir(mCacheDir,context);
        if(!directory.exists()){
            directory.mkdirs();
        }
        try {
            mDiskLruCache =  DiskLruCache.open(directory,getAppVersion(context),1,50*MB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DiskCache getInstance(Context context){
        if(mDiskCache == null){
            synchronized (DiskCache.class){
                if(mDiskCache == null){
                    mDiskCache = new DiskCache(context);
                }
            }
        }
        return mDiskCache ;
    }

    private int getAppVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo("com.sinieco.myjnideom",0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private File getDiskCacheDir(String mCacheDir, Context context) {
        String cachePath = null ;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath() ;
        }

        return new File(cachePath+File.separator+mCacheDir);
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        InputStream is = null ;
        try {
            //快照
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(request.getImageUriMD5());
            if(snapshot != null){
                is = snapshot.getInputStream(0);
                return BitmapFactory.decodeStream(is);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtil.closeQuietly(is);
        }
        return null;
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        DiskLruCache.Editor editor = null ;
        OutputStream os = null ;
        try {
            editor = mDiskLruCache.edit(request.getImageUriMD5());
            os = editor.newOutputStream(0);
            if(persistBitmap2Disk(bitmap, os)){
                editor.commit();
            }else {
                editor.abort();
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtil.closeQuietly(os);
        }
    }

    private boolean persistBitmap2Disk(Bitmap bitmap, OutputStream os) {
        BufferedOutputStream bos = null ;
        try {
            bos = new BufferedOutputStream(os);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false ;
        }finally {
            IOUtil.closeQuietly(bos);
        }
        return true;

    }

    @Override
    public void remove(BitmapRequest request) {
        try {
            mDiskLruCache.remove(request.getImageUriMD5());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCacheDir(String cacheDir){
        this.mCacheDir = cacheDir ;
    }
}
