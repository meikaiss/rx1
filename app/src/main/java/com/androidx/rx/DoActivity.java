package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by meikai on 2019/09/10.
 */
public class DoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_do);

        findViewById(R.id.btn_doOnEach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnEach();
            }
        });

        findViewById(R.id.btn_doOnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnNext();
            }
        });

        findViewById(R.id.btn_delaySubscription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delaySubscription();
            }
        });


    }

    private void doOnEach() {

        Observable.unsafeCreate(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onError(new Exception("tt"));

            }
        }).doOnEach(new Action1<Notification<? super Integer>>() {
            @Override
            public void call(Notification<? super Integer> notification) {
                if (notification.getKind() == Notification.Kind.OnNext){
                    Log.e("doOnEach", "call, OnNext, value=" + notification.getValue());
                }else if (notification.getKind() == Notification.Kind.OnError){
                    Log.e("doOnEach", "call, OnError, value=" + notification.getThrowable().toString());
                }else if (notification.getKind() == Notification.Kind.OnCompleted){
                    Log.e("doOnEach", "call, OnCompleted, value=" + notification.getValue());
                }
            }
        })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return "map之后: " + integer;
                    }
                })
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("doOnNext", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("doOnNext", "onError," + e.toString());
                    }

                    @Override
                    public void onNext(String o) {
                        Log.e("doOnNext", "最终结果 : " + o);
                    }
                });


        /**
         *
        Observable.just(1, 2, 3, 4)
                .doOnEach(new Action1<Notification<? super Integer>>() {
                    @Override
                    public void call(Notification<? super Integer> notification) {
                        Log.e("doOnEach", "call, value=" + notification.getValue());
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return "map之后: " + integer;
                    }
                })
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("doOnNext", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("doOnNext", "onError," + e.toString());
                    }

                    @Override
                    public void onNext(String o) {
                        Log.e("doOnNext", "最终结果 : " + o);
                    }
                });
         */
    }

    private void doOnNext() {

        Observable.just(1, 2, 3)
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer item) {
                        Log.e("doOnNext", "call, " + item);
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return "map之后: " + integer;
                    }
                })
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("doOnNext", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("doOnNext", "onError," + e.toString());
                    }

                    @Override
                    public void onNext(String o) {
                        Log.e("doOnNext", "最终结果 : " + o);
                    }
                });
    }


    private void delaySubscription() {

        Observable observable1 = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onNext("4");
//                subscriber.onError(new Error("模拟发射error"));
                subscriber.onNext("5");
                subscriber.onNext("6");
                subscriber.onNext("7");

                subscriber.onCompleted();

            }
        });

        observable1.delaySubscription(2, TimeUnit.SECONDS)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("delay", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("delay", "onError," + e.toString());
                    }

                    @Override
                    public void onNext(String o) {
                        Log.e("delay", "最终结果 : " + o);
                    }
                });
    }


}
