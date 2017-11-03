package com.sinieco.lib_volley.volley;

import com.sinieco.lib_volley.volley.inter.IHttpListener;
import com.sinieco.lib_volley.volley.inter.IHttpService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


/**
 * Created by BaiMeng on 2017/11/3.
 */
public class JsonHttpService implements IHttpService {
    private HttpClient httpClient=new DefaultHttpClient();
    private HttpPost httpPost ;
    private String url ;
    private byte[] requestData ;
    private IHttpListener httpListener ;
    private HttpResponseHandler httpResponseHandler = new HttpResponseHandler();
    @Override
    public void setUrl (String url){
        this.url = url ;
    }
    @Override
    public void setRequestData(byte[] request){
        this.requestData = request ;
    }

    @Override
    public void setHttpListener(IHttpListener listener){
        this.httpListener = listener ;
    }
    @Override
    public void excute() {
        httpPost = new HttpPost(url);
//        if (requestData == null){
//            try {
//                throw new IllegalArgumentException("请求参数为空");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(requestData);
        httpPost.setEntity(byteArrayEntity);
        try {
            httpClient.execute(httpPost,httpResponseHandler);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private class HttpResponseHandler implements ResponseHandler {

        @Override
        public Object handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
            if(200 == httpResponse.getStatusLine().getStatusCode()){
                HttpEntity entity = httpResponse.getEntity();
                httpListener.onSuccess(entity);
            }else {
                httpListener.onFail();
            }
            return null;
        }
    }
}
