package com.sinieco.lib_imageloader.loader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author BaiMeng on 2017/11/24.
 */

public class LoaderManager {
    private static volatile LoaderManager mInstance = new LoaderManager();
    private Map<String,Loader> loaders = new HashMap<String,Loader>();
    private NullLoader mNullLoader = new NullLoader() ;
    private LoaderManager(){
        register("http",new UrlLoader());
        register("https",new UrlLoader());
        register("file",new LocalLoader());
    }

    private void register(String schema, Loader loader) {
        loaders.put(schema,loader);
    }

    public static LoaderManager getInstance( ){
        return mInstance;
    }



    public Loader getLoader(String schema){
        if(loaders.containsKey(schema)){
            return loaders.get(schema);
        }
        return mNullLoader ;
    }

}
