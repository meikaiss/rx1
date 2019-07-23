package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;


/**
 * TODO
 * Created by meikai on 2019/07/22.
 */
public class ThrottleLastActivity extends AppCompatActivity {

    private PublishSubject publishSubject;

    //模拟节流时 发射的数据
    private int throttleValue = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throttlelast);

        findViewById(R.id.btn_throttlelast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnThrottlelast();
            }
        });
    }

    /**
     * Throttle:节流阀
     * PublishSubject:与普通的Subject不同，在订阅时并不立即触发订阅事件，而是允许我们在任意时刻手动调用onNext,onError(),onCompleted来触发事件。
     *
     *
     */
    public void btnThrottlelast() {

        if (publishSubject == null) {
            publishSubject = PublishSubject.create();
            publishSubject.
                    throttleLast(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber() {
                        @Override
                        public void onCompleted() {
                            Log.e("throttleLast", "4.onComplete");
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Object o) {
                            Log.e("throttleLast", "3.收到onNext消息，值=" + o);

                        }
                    });
        }

        findViewById(R.id.btn_throttlelast_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = throttleValue++;
                Log.e("throttleLast", "onNext 点击，发射值=" + value);
                publishSubject.onNext(value);
            }
        });

        findViewById(R.id.btn_throttlelast_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("throttleLast", "onCompleted 点击");
                publishSubject.onCompleted();
            }
        });
    }


}
