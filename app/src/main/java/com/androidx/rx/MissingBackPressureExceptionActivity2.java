package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 模拟 Subscriber 多线程 连续发射多个事件 引发的 背压异常
 * Created by meikai on 2019/08/27.
 */
public class MissingBackPressureExceptionActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_pressure);


        findViewById(R.id.btn_back_pressure)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        init();
                    }
                });

    }


    private void init() {

        Subscriber subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                Log.e("OnSubscribe", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("OnSubscribe", "onError, " + e.toString());
            }

            @Override
            public void onNext(Long aLong) {
                Log.e("OnSubscribe", "aLong=" + aLong);
            }
        };

        Action1<Long> action1 = new Action1<Long>() {
            @Override
            public void call(Long aLong) {

                Log.e("backPressure---", "aLong="+ aLong);
            }
        };


        try{
            /**
             * 已废弃的最基本的创建 Observable 的方法
             *
             */
            Observable.create(new Observable.OnSubscribe<Long>() {
                @Override
                public void call(Subscriber<? super Long> subscriber) {

                    if (subscriber.isUnsubscribed()) {
                        //订阅者是一种增加型的观察者，订阅者可以取消订阅，而观察者不行
                        return;
                    }

                    /**
                     * 这一段代码，会在observable.subscribe()时执行。
                     * 它的职责主要是给观察者发射事件
                     *
                     * 在未通过subscribe()方法注册前，生产者中的此call方法不会被执行。
                     */


                    try{
                        for (long i = 1; i < 20; i++) {
                            subscriber.onNext(i);
                        }
                        subscriber.onCompleted();
                    }catch (Exception e){
                        //MissingBackpressureException 异常无法捕获
                        Log.e("subscribe---2", "Exception="+ e.getMessage());
                    }
                }
            })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);

        }catch (Exception e){
            //MissingBackpressureException 异常无法捕获
            Log.e("subscribe---", "Exception="+ e.getMessage());
        }





    }
}
