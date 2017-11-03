package com.sinieco.moudle1;


import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sinieco.lib_volley.volley.JsonDealListener;
import com.sinieco.lib_volley.volley.Volley;
import com.sinieco.lib_volley.volley.inter.IDataListener;

/**
 * Created by BaiMeng on 2017/11/2.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String url = "http://baobab.kaiyanapp.com/api/v4/discovery";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module1_activity_main);
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        Volley.sendRequest(url, null, HomeBean.class, new IDataListener<HomeBean>() {
            @Override
            public void onSuccess(HomeBean response) {
                Log.e("成功",response.toString());
            }

            @Override
            public void onFail(Exception e) {
                Log.e("失败",e.toString());
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
}
