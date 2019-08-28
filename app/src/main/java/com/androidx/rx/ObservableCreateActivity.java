package com.androidx.rx;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by meikai on 2019/08/06.
 */
public class ObservableCreateActivity extends AppCompatActivity {

    private static final String TAG = "create";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_observable_create);


        findViewById(R.id.btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

    }


    private void init() {

        Log.e(TAG, "1, " + System.currentTimeMillis());
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e(TAG, "3, " + System.currentTimeMillis());

                boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();

                for (int i = 0; i < 10; i++) {
                    Log.e(TAG, "2, Emit=" + i + ", " + System.currentTimeMillis() +"，"+isMainThread);
                    subscriber.onNext("" + i);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                /**
                 * 如果subscribeOn和observeOn是在同一个线程，则所有onNext执行完后，才会执行消费者的call
                 * 如果subscribeOn和observeOn是在不同的线程，则类似多线程并发
                 */

                /**
                 * 总结：
                 * Subscriber是Observable.create(Observable.OnSubscribe)参数回调和Observable.subscribe(Action1,[Action1,Action0])参数回调的通信桥梁.
                 *
                 * 需要你注意的是：如果调用了void onError(Throwable e)方法，那么onNext和onCompleted都不会执行。
                 */
            }
        });

        Log.e(TAG, "2, " + System.currentTimeMillis());
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                        boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();
                        Log.e(TAG, "4, s=" + s + ", " + System.currentTimeMillis() +"，"+isMainThread);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }, new Action0() {
                    @Override
                    public void call() {

                    }
                });

        /**
         * 总结几句话:
         * create操作符创建Observable，Observable通过构造方法 保存了我们传进来的OnSubscribe 说白了就是Action1.
         * 当调用了subscribe方法 Observable.OnSubscribe的call方法就会被执行
         */

        /**
         * 总结：
         * 1. 只有当Observable被订阅OnSubscribe的call(subscriber)方法才会被执行
         * 2. onCompleted方法里会把Subscription取消订阅（unsubscribe）
         * 3. 如果调用了void onError(Throwable e)方法，那么onNext和onCompleted都不会执行。会在onError调用之前，把Subscription取消注册。
         * 4. 整个事件流不管是正常结束（onComplete）还是出现了异常（onError），Subscription都会被取消注册（unsubscribe）。
         *    但是，由于我们可能执行一些耗时操作，界面又被关闭了，所以还需要把subscription取消注册
         * 5. Subscriber是Observable.create(Observable.OnSubscribe)参数回调和Observable.subscribe(Action1,[Action1,Action0])参数回调的通信桥梁.
         *
         */

    }
}

