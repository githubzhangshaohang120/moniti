package com.example.asus.moniti.inter;

import com.example.asus.moniti.bean.BaseBean;
import com.example.asus.moniti.bean.CartBean;
import com.example.asus.moniti.net.HttpUtils;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServerApi {
    //查询购物车
    @GET(HttpUtils.SELECTCAR_PATH_URL)
    public Flowable<CartBean> getCart(@Query("uid") String uid);

    //删除购物车
    @GET(HttpUtils.DELETECAR_PATH_URL)
    Observable<BaseBean> deleteGoodCar(@Query("uid") String uid, @Query("pid") String pid);
}
