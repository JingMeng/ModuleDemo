package com.sinieco.lib_imageloader.policy;

import com.sinieco.lib_imageloader.request.BitmapRequest;

/**
 * @author BaiMeng on 2017/11/22.
 */

public interface LoaderPolicy {
    int compareTo(BitmapRequest bitmapRequest, BitmapRequest another);
}
