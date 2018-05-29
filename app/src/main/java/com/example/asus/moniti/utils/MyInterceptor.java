package com.example.asus.moniti.utils;

import java.io.IOException;
import java.net.URI;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MyInterceptor implements Interceptor {
    private String newUri;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        URI uri = request.url().uri();
        String query = uri.getQuery();
        String host = uri.getHost();
        String path = uri.getPath();
        if (query == null){
            newUri = "https://"+host + path +"?source=android";
        }else {
            newUri = "https://"+host + path +"?"+query+"&source=android";
        }
        Request request1 = request.newBuilder().url(newUri).build();
        Response response = chain.proceed(request1);

        return response;
    }
}