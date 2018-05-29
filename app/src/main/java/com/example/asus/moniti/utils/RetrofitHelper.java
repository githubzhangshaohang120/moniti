package com.example.asus.moniti.utils;

import com.example.asus.moniti.inter.ServerApi;
import com.example.asus.moniti.net.HttpUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private static OkHttpClient client;
    private static ServerApi api;

    static {
        initOkHttp();
    }

    private static void initOkHttp() {
        if (client == null) {
            synchronized (OkHttpClient.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder()
                            .addInterceptor(new MyInterceptor())
                            .build();
                }
            }
        }
    }
    public static ServerApi getApi(){
        if (api == null){
            synchronized (ServerApi.class){
                if (api==null){
                    api = RetrofitHelper.create(ServerApi.class, HttpUtils.BASE_URL);
                }
            }
        }
        return api;
    }

    private static <T> T create(Class<T> tClass, String baseUrl) {
        Retrofit re = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return re.create(tClass);
    }
}
