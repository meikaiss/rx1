package com.androidx.rx;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by meikai on 2019/08/19.
 */
public class ThrottleLast2Activity extends AppCompatActivity {


    private PublishSubject publishSubject;

    int index = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throttlelast_2);

        findViewById(R.id.btn_throttlelast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

        publishSubject = PublishSubject.create();
        publishSubject.throttleLast(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                        boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();

                        Log.e("throttleLast", System.currentTimeMillis() + ", ---接到一次onNext, " + o.toString() +" , " + isMainThread);

                    }
                });


    }


    private void init() {

        int emitValue = index++;
        Log.e("throttleLast", System.currentTimeMillis() + ", 执行一次onNext, "+ emitValue);
        publishSubject.onNext(emitValue);

    }


}
