package com.example.asus.moniti.utils;

public interface OnLisenter<T> {
    void onSuccess(T t);

    void onFailure(Throwable throwable);
}
