package com.sinieco.lib_imageloader.policy;

import com.sinieco.lib_imageloader.request.BitmapRequest;

/**
 * @author BaiMeng on 2017/11/22.
 */

public class SerialPolicy implements LoaderPolicy {
    @Override
    public int compareTo(BitmapRequest current, BitmapRequest another) {
        return current.getSerialNo() - another.getSerialNo();
    }
}
