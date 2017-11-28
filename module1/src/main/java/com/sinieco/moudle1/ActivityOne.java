package com.sinieco.moudle1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sinieco.lib_imageloader.core.SimpleImageLoader;

/**
 * Created by BaiMeng on 2017/11/2.
 */
@Route(path = "/one/a")
public class ActivityOne extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moudle1_activity_one);
        ImageView img = (ImageView) findViewById(R.id.img_first);
        String url = "http://img02.tooopen.com/images/20160122/tooopen_sy_155242698349.jpg";
        SimpleImageLoader.getInstance().displayImage(img,url);
    }
}
