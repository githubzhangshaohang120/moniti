package com.example.asus.moniti.module;

import com.example.asus.moniti.bean.CartBean;
import com.example.asus.moniti.utils.OnLisenter;
import com.example.asus.moniti.utils.RetrofitHelper;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BaseModel implements IBaseModel {
    @Override
    public void getCard(String uid, final OnLisenter onListiner) {
        Flowable<CartBean> cart = RetrofitHelper.getApi().getCart(uid);
        cart.doOnSubscribe(new Consumer<Subscription>() {
            @Override
            public void accept(Subscription subscription) throws Exception {

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CartBean>() {
                    @Override
                    public void accept(CartBean cartBean) throws Exception {
                        onListiner.onSuccess(cartBean);
                    }
                })  ;
    }
}
