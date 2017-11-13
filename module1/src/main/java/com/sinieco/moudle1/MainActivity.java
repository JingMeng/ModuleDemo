package com.sinieco.moudle1;


import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sinieco.lib_volley.volley.JsonDealListener;
import com.sinieco.lib_volley.volley.Volley;
import com.sinieco.lib_volley.volley.download.DownLoadItemInfo;
import com.sinieco.lib_volley.volley.download.FileDownManager;
import com.sinieco.lib_volley.volley.download.inter.IDownloadServiceCallback;
import com.sinieco.lib_volley.volley.inter.IDataListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaiMeng on 2017/11/2.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "  测试下载功能  ———>   ";
    private String url = "http://baobab.kaiyanapp.com/api/v4/discovery";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module1_activity_main);
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
//        Volley.sendRequest(url, null, HomeBean.class, new IDataListener<HomeBean>() {
//            @Override
//            public void onSuccess(HomeBean response) {
//                Log.e("成功",response.toString());
//            }
//
//            @Override
//            public void onFail(Exception e) {
//                Log.e("失败",e.toString());
//            }
//        });
    }

    public void down(View view){
        Log.e("下载","------------->  开始下载文件");
        FileDownManager downloadManager = new FileDownManager();
        downloadManager.down("http://gdown.baidu.com/data/wisegame/8be18d2c0dc8a9c9/WPSOffice_177.apk", new IDownloadServiceCallback() {
            @Override
            public void onDownloadStatusChanged(DownLoadItemInfo downloadItemInfo) {
                Log.e(TAG,"下载状态改变");
            }

            @Override
            public void onTotalLengthReceived(DownLoadItemInfo downloadItemInfo) {
                Log.e(TAG,"已接收大小"+downloadItemInfo.getCurrentLength());
            }

            @Override
            public void onCurrentSizeChanged(DownLoadItemInfo downloadItemInfo, double downLenth, long speed) {
                Log.e(TAG,"当前大小改变"+"downLength:"+downLenth+"   speed:"+speed);
            }

            @Override
            public void onDownloadSuccess(DownLoadItemInfo downloadItemInfo) {
                Log.e(TAG,"下载成功");
            }

            @Override
            public void onDownloadPause(DownLoadItemInfo downloadItemInfo) {
                Log.e(TAG,"下载暂停");
            }

            @Override
            public void onDownloadError(DownLoadItemInfo downloadItemInfo, int var2, String var3) {
                Log.e(TAG,"下载失败");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.one){
            startActivity(new Intent(this,ActivityOne.class));
        }else if (v.getId() == R.id.two){
            ARouter.getInstance().build("/two/b").navigation();
        }else if(v.getId() == R.id.three){
            ARouter.getInstance().build("/three/c").navigation();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
