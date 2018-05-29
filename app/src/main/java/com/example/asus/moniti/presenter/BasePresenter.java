package com.example.asus.moniti.presenter;

import com.example.asus.moniti.IMainActivity;
import com.example.asus.moniti.bean.CartBean;
import com.example.asus.moniti.module.BaseModel;
import com.example.asus.moniti.module.IBaseModel;
import com.example.asus.moniti.utils.OnLisenter;

public class BasePresenter {
    private IMainActivity iMainActivity;
    private IBaseModel iBaseModel;

    public BasePresenter(IMainActivity iMainActivity) {
        this.iMainActivity = iMainActivity;
        iBaseModel = new BaseModel();
    }
    public void getCarts(){
        iBaseModel.getCard("71", new OnLisenter() {
            @Override
            public void onSuccess(Object o) {
                iMainActivity.onShow((CartBean) o);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.getMessage();
            }
        });
    }
}