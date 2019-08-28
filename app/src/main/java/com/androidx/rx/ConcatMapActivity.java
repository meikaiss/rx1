package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

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
public class ConcatMapActivity extends AppCompatActivity {

    private static final String TAG = "concatmap";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rx_contactmap);

        findViewById(R.id.btn_comtactmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

        /**
         * concatMap和flatMap最大的区别是concatMap发射的数据集是有序的，flatMap发射的数据集是无序的。
         */

        /**
         * 但是出现了一个奇怪的问题：上篇博客我们使用flatMap在多个线程完成任务，有时候顺序是乱的。
         * 但是concatMap一开始也是使用一个线程来完成任务，只有先调用flatMap多线程完成任务，然后再调用concatMap才会有多线程。
         * 实验结果表明：如果RxJava有了多个线程，concatMap才会使用多个线程，如果Rxjava里只有一个缓存的线程，
         * concatMap只是用一个线程来执行任务，尽管加上了.subscribeOn(Schedulers.io())代码。
         * 然而如果是flatMap加上.subscribeOn(Schedulers.io())代码，每次调用都是多个线程的。这也是flatMap和concatMap的又一个区别。
         */
    }

    private void init(){
        Observable.just(
                "http://www.baidu.com/",
                "http://www.google.com/",
                "https://www.bing.com/",
                "https://www.2345.com/",
                "https://www.hao123.com/")
                .concatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return createIpObservable(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String o) {

                        Log.e(TAG, "onNext, " + o + ",  ThreadName=" + Thread.currentThread().getName());

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "onError");
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Log.e(TAG, "onCompleted");
                    }
                });
    }



    //根据主机获取ip
    private Observable<String> createIpObservable(String url) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                try {
                    String ip = getIPByUrl(url);

                    Log.e(TAG, "call, ThreadName=" + Thread.currentThread().getName());

                    subscriber.onNext(url+" 的ip=" + ip);
                    subscriber.onNext("有一条ip解析完毕");

                } catch (Exception e) {

                    subscriber.onNext(null);
                }

                //这里是针对 单条数据的衍生 出 多条数据，因 onCompleted 无效
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()); // 每一个 子任务 在单独的线程中执行
    }

    private String getIPByUrl(String str) throws MalformedURLException, UnknownHostException {
        URL urls = new URL(str);
        String host = urls.getHost();
        String address = InetAddress.getByName(host).toString();
        int b = address.indexOf("/");
        return address.substring(b + 1);

    }

}
