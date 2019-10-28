package com.androidx.rx.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidx.rx.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

/**
 * Created by meikai on 2019/10/28.
 */
public class ThreadPoolActivity extends AppCompatActivity {

    private static final String TAG = "RxThread";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_thread_pool);


        findViewById(R.id.btn_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intRxThread();
            }
        });


        findViewById(R.id.btn_thread_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intRxThread2();
            }
        });
    }


    private void intRxThread() {

        List<Observable<String>> observableList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Observable<String> observable = Observable.just(i)
                    .map(new Func1<Integer, String>() {
                        @Override
                        public String call(Integer integer) {

                            Log.e(TAG, "threadName1, integer=" + integer + ", " + Thread.currentThread().getName());

                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            return "s-" + integer;
                        }
                    })
                    .subscribeOn(Schedulers.io());

            observableList.add(observable);
        }

        Observable.zip(observableList, new FuncN<String>() {
            @Override
            public String call(Object... args) {

                Log.e(TAG, "threadName2， zip的结果集执行的线程=" + Thread.currentThread().getName());

                String result = "";
                for (int i = 0; i < args.length; i++) {
                    result += args[i].toString();
                }

                return result;
            }
        })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "threadName3, Observer执行的线程=" + Thread.currentThread().getName());
                        Log.e(TAG, "s=" + s);
                    }
                });

    }

    private void intRxThread2() {

        List<Observable<String>> observableList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Observable<String> observable = Observable.just(i)
                    .map(new Func1<Integer, String>() {
                        @Override
                        public String call(Integer integer) {

                            Log.e(TAG, "threadName1, integer=" + integer + ", " + Thread.currentThread().getName());

                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            return "s-" + integer;
                        }
                    });

            observableList.add(observable);
        }

        Observable.zip(observableList, new FuncN<String>() {
            @Override
            public String call(Object... args) {

                Log.e(TAG, "threadName2， zip的结果集执行的线程=" + Thread.currentThread().getName());

                String result = "";
                for (int i = 0; i < args.length; i++) {
                    result += args[i].toString();
                }

                return result;
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "threadName3, Observer执行的线程=" + Thread.currentThread().getName());
                        Log.e(TAG, "s=" + s);
                    }
                });

    }


}
