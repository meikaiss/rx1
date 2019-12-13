package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 研究zip的线程
 * Created by meikai on 2019/12/13.
 */
public class ZipThreadActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_zip_thread);


        findViewById(R.id.btn_zip_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zip();
            }
        });

    }

    ExecutorService executorService = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue(), new ThreadFactory() {

        private int count = 0;

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("self thread-" + count);
            count++;
            return thread;
        }
    }, new ThreadPoolExecutor.AbortPolicy());


    /**
     * 1、Schedulers.io()每次都会创建一个新的线程
     * 2、zip的各个子ovservable可以指定自己的调度线程
     * 3、Schedulers.from可以用自己的线程池来创建调度器
     */
    private void zip() {


        Observable observable1 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

                try {
                    Thread.sleep(10* 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.e("zip", "observable1.call , " + Thread.currentThread().getName());

                subscriber.onNext(1);

            }
        }).subscribeOn(Schedulers.from(executorService));

        Observable observable2 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

                Log.e("zip", "observable2 call, " + Thread.currentThread().getName());

//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                subscriber.onNext(100);
            }
        });

        Log.e("zip", "开始zip");

        Observable.zip(observable1, observable2, new Func2<Integer, Integer, String>() {
            @Override
            public String call(Integer integer, Integer integer2) {

                Log.e("zip", "zip.call, " + Thread.currentThread().getName());

                return "结果：" + integer + ", " + integer2;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String str) {
                        Log.e("onNext", str + ", " + Thread.currentThread().getName());
                    }
                });

    }

}
