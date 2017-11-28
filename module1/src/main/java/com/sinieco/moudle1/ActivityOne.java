package com.sinieco.moudle1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sinieco.lib_imageloader.cache.DoubleCache;
import com.sinieco.lib_imageloader.config.ImageLoaderConfig;
import com.sinieco.lib_imageloader.core.SimpleImageLoader;
import com.sinieco.lib_imageloader.policy.SerialPolicy;

/**
 * Created by BaiMeng on 2017/11/2.
 */
@Route(path = "/one/a")
public class ActivityOne extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moudle1_activity_one);
        ImageView img = (ImageView) findViewById(R.id.img);
        ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                .setThreadCount(4)
                .setCacheConfig(new DoubleCache(this))
                .setLoaderConfig(new SerialPolicy())
                .setLoadingImage(R.drawable.a)
                .setFailedImage(R.drawable.b)
                .build();
        String url = "http://img.my.csdn.net/uploads/201407/26/1406383165_7197.jpg";
        SimpleImageLoader.getInstance(config).displayImage(img,url);
    }
}
