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
public class FlatMapActivity extends AppCompatActivity {

    private static final String TAG = "flatmap";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rx_flatmap);


        findViewById(R.id.btn_flatmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });


        /**
         * flatmap的官方解释
         *
         * 对Observable发射的数据都应用(apply)一个函数，这个函数返回一个Observable，
         * 然后合并这些Observables，并且发送（emit）合并的结果。
         * flatMap和map操作符很相像，flatMap发送的是合并后的Observables，map操作符发送的是应用函数后返回的结果集
         *
         * 通俗解释:
         * 针对输入源中的每一条数据，按flatmap创建的Observable 的规则，衍生出多条数据， 并将多条数据发射出去
         *
         *
         */
    }


    private void init() {

        Observable.just(
                "http://www.baidu.com/",
                "http://www.google.com/",
                "https://www.bing.com/",
                "https://www.2345.com/",
                "https://www.hao123.com/")
                .flatMap(new Func1<String, Observable<String>>() {
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
