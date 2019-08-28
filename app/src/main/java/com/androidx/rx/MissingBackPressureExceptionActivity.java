package com.androidx.rx;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 模拟 背压异常
 * Created by meikai on 2019/08/27.
 */
public class MissingBackPressureExceptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_pressure);


        findViewById(R.id.btn_back_pressure)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        init();
                    }
                });

    }


    private void init() {


//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                Log.e("uncaughtException---", "" + e.getMessage());
//            }
//        });

        Subscriber<Long> subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                Log.e("backPressure---", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

                Log.e("backPressure---", "onError, " + e.toString());
            }

            @Override
            public void onNext(Long aLong) {
                //让消费者线程停顿1秒，这样消费的速度 就比 生产速度 慢， 当 生产过多的任务时，就会发生 MissingBackPressure
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                boolean isMainThread = Looper.getMainLooper() == Looper.myLooper();
                Log.e("backPressure---", "isMainThread=" + isMainThread + ", " + Thread.currentThread().getName()
                        +", aLong="+ aLong);

            }
        };

        Action1<Long> action1 = new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.e("backPressure---", "aLong="+ aLong);
            }
        };

        Log.e("backPressure----1", "init");

        Observable.interval(1,1, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()) //定义消费者在主线程
                .subscribe(subscriber);




    }
}
