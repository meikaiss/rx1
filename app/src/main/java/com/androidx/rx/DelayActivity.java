package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by meikai on 2019/09/10.
 */
public class DelayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_delay);

        findViewById(R.id.btn_delay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delay();
            }
        });

        findViewById(R.id.btn_delay_func).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delayFunc();
            }
        });

        findViewById(R.id.btn_delaySubscription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delaySubscription();
            }
        });


    }

    private void delay() {

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

        observable1.delay(2, TimeUnit.SECONDS)
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

    private void delayFunc() {

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

        observable1.delay(new Func1<String, Observable>() {
            @Override
            public Observable call(String string) {

                //针对 每一项数据
                return Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("a1");
                        subscriber.onCompleted();
                    }
                });
            }
        })
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
