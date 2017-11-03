package com.sinieco.module_app;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sinieco.moduledemo.base.BaseApplication;

/**
 * Created by BaiMeng on 2017/11/2.
 */
public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG){
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
    }
}
