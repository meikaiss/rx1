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
import rx.functions.Func2;

/**
 * Created by meikai on 2019/09/10.
 */
public class CatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_catch);

        findViewById(R.id.btn_onErrorReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorReturn();
            }
        });

        findViewById(R.id.btn_onErrorResumeNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorResumeNext();
            }
        });

        findViewById(R.id.btn_onExceptionResumeNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExceptionResumeNext();
            }
        });

    }

    private void onErrorReturn() {

        Observable observable1 = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onNext("4");
                subscriber.onError(new Error("模拟发射error"));
                subscriber.onNext("5");
                subscriber.onNext("6");
                subscriber.onNext("7");

            }
        });

        observable1
                .onErrorReturn(new Func1<Throwable, String>() {
                    @Override
                    public String call(Throwable throwable) {
                        //碰到发射 onError 时，将 error转换为此函数的返回值
                        return throwable.toString();
                    }
                })
                .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e("onErrorReturn", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("onErrorReturn", "onError");
            }

            @Override
            public void onNext(String o) {
                Log.e("onErrorReturn", "最终结果 : " + o);
            }
        });
    }

    private void onErrorResumeNext() {

        Observable observable1 = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onNext("4");
                subscriber.onError(new Error("模拟发射error"));
                subscriber.onNext("5");
                subscriber.onNext("6");
                subscriber.onNext("7");

            }
        });

        observable1
                .onErrorResumeNext(Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {

                        subscriber.onNext("error后-1");
                        subscriber.onNext("error后-2");
                        subscriber.onNext("error后-3");
                        subscriber.onNext("error后-4");
                        subscriber.onError(new Exception("模拟发射error"));
                        subscriber.onNext("error后-5");
                        subscriber.onNext("error后-6");
                    }
                }))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("onErrorReturn", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onErrorReturn", "onError");
                    }

                    @Override
                    public void onNext(String o) {
                        Log.e("onErrorReturn", "最终结果 : " + o);
                    }
                });
    }



    private void onExceptionResumeNext() {

        Observable observable1 = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onNext("4");
                subscriber.onError(new Error("模拟发射error"));
                subscriber.onNext("5");
                subscriber.onNext("6");
                subscriber.onNext("7");

            }
        });

        observable1
                .onExceptionResumeNext(Observable.just("源头发生了一处error，转换为此消息1","源头发生了一处error，转换为此消息2") )
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("onErrorReturn", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onErrorReturn", "onError");
                    }

                    @Override
                    public void onNext(String o) {
                        Log.e("onErrorReturn", "最终结果 : " + o);
                    }
                });
    }




}
