package com.androidx.rx;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by meikai on 2019/09/09.
 */
public class IntervalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_interval);


        findViewById(R.id.btn_interval).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

        findViewById(R.id.btn_timer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer();
            }
        });

    }

    private void init() {

        Observable observable = Observable.interval(0, 2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());

        Log.e("onNext", "已创建" + observable);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.e("onNext", "开始订阅" + observable);
                observable.subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e("onNext", "aLong=" + aLong);
                    }
                });

            }
        }, 5000);


    }


    private void timer() {

        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.e("timer", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("timer", "onError");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e("timer", "onNext, aLong=" + aLong);
                    }
                });

    }
}
