package com.sinieco.moudle1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * Created by BaiMeng on 2017/11/2.
 */
@Route(path = "/one/a")
public class ActivityOne extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moudle1_activity_one);
    }
}
