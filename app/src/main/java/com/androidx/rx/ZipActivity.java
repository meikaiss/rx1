package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * Created by meikai on 2019/08/06.
 */
public class ZipActivity extends AppCompatActivity {

    private static final String TAG = "Zip";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rx_zip);

        findViewById(R.id.btn_zip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zip();
                zip2();
            }
        });

        /**
         * 使用一个函数组合多个Observable发射的数据集合，然后再发射这个结果
         *
         * 注：
         * 1、如果组合的observable中有一个没有发射数据，则Subscriber中的onNext也不会执行
         * 2、如果组合的observable中有一个发射了onError，则Subscriber中的onError会立即执行，并且不会再触发onNext和onCompleted
         */

    }

    private void zip() {

        Observable.zip(Observable.range(1, 100), Observable.range(10000, 11000),
                new Func2<Integer, Integer, String>() {
                    @Override
                    public String call(Integer integer, Integer integer2) {
                        return "a=" + integer + ", b=" + integer2;
                    }
                }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("zip1", "s=" + s);
            }
        });

    }

    private void zip2() {
        Observable observable1 = Observable.unsafeCreate(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

                for (int i = 1; i <= 5; i++) {
                    subscriber.onNext(i);
                }

                subscriber.onNext(51);
                subscriber.onNext(52);

            }
        }).onErrorReturn(new Func1<Throwable, Integer>() {
            @Override
            public Integer call(Throwable throwable) {
                return 0;
            }
        });

        Observable.zip(observable1, Observable.range(10000, 10010),
                new Func2<Integer, Integer, String>() {
                    @Override
                    public String call(Integer integer, Integer integer2) {
                        return "a=" + integer + ", b=" + integer2;
                    }
                }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e("zip2", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("zip2", "onError," + e.toString());
            }

            @Override
            public void onNext(String s) {
                Log.e("zip2", "s=  " + s);
            }
        });

    }


    private void init() {

        final long[] goodId = {19l};

        Observable<String> sellerInfoObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                goodId[0] = goodId[0] + 100;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                subscriber.onNext("seller info");
                subscriber.onCompleted();  //必不可少，否则后续的 observable 不知道何时执行

            }
        }).subscribeOn(Schedulers.io());

        Observable<String> goodsInfoObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                subscriber.onNext("goods info");
//                subscriber.onCompleted();
                subscriber.onError(new Exception("测试"));

            }
        }).subscribeOn(Schedulers.io());


        Observable<String> recommendInfoObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                subscriber.onNext("recommend info");
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.io());

        Log.e(TAG, "开始订阅 , " + System.currentTimeMillis());

        Observable.zip(sellerInfoObservable, goodsInfoObservable, recommendInfoObservable,
                new Func3<String, String, String, Picture>() {
                    @Override
                    public Picture call(String s, String s2, String s3) {
                        Picture picture = new Picture();
                        picture.result1 = s;
                        picture.result2 = s2;
                        picture.result3 = s3;
                        return picture;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Picture>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted, " + System.currentTimeMillis());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError, " + System.currentTimeMillis());
                    }

                    @Override
                    public void onNext(Picture picture) {
                        Log.e(TAG, "onNext, picture=" + picture.toString() + " , " + System.currentTimeMillis());
                    }
                });

    }

    private static class Picture {
        public String result1;
        public String result2;
        public String result3;

        @NonNull
        @Override
        public String toString() {
            return String.format(" s1=%s, s2=%s, s3=%s", result1, result2, result3);
        }

    }


}
