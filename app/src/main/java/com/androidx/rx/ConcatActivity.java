package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by meikai on 2019/08/06.
 */
public class ConcatActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rx_concat);


        findViewById(R.id.btn_concat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

        /**
         * concat连接一组 Observable，有序的发射这一组Observable，不会交叉
         * 1、配合 fisrt 操作，取第一个 有onNext的数据，并中止后续未发射的 Observable
         *    若这一组所有 Observable 都未发射数据，则会 调用 下游的 onError 方法
         *
         * 2、配合 takeFirst，实现 即使这一组都没有发射数据，也不会 error
         */
    }

    private void init() {

        final long[] goodId = {19l};

        Observable<String> sellerInfoObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                goodId[0] = goodId[0] + 100;

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                subscriber.onNext("seller info");
                subscriber.onCompleted();  //必不可少，否则后续的 observable 不知道何时执行

            }
        }).subscribeOn(Schedulers.io());

        Observable<String> goodsInfoObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                subscriber.onNext("goods info");
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.io());


        Observable<String> recommendInfoObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                subscriber.onNext("recommend info");
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.io());

        Log.e(TAG, "开始订阅 , " + System.currentTimeMillis());

        Observable.concat(sellerInfoObservable, goodsInfoObservable, recommendInfoObservable)
                .takeFirst(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                        Log.e(TAG, s + " , " + System.currentTimeMillis());

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        Log.e(TAG, "error , " + System.currentTimeMillis() +", "+throwable.getMessage());
                    }
                }, new Action0() {
                    @Override
                    public void call() {

                        Log.e(TAG, "complete , " + System.currentTimeMillis());
                    }
                });

    }

}
