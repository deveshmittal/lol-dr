package com.ouchadam.loldr;

import android.util.Log;

import rx.Subscriber;

public class LogSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.e("!!!", "Something went wrong", e);
    }

    @Override
    public void onNext(T t) {

    }
}
