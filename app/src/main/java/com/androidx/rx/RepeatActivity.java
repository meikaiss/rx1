package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by meikai on 2019/09/09.
 */
public class RepeatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_repeat);


        findViewById(R.id.btn_repeat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeatWhen();
            }
        });
    }

    private void repeat() {

        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .repeat()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e("repeat", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("repeat", "onError=" + e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("repeat", "integer=" + integer);
                    }
                });

    }


    private void repeatWhen() {

        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        //传递被观察者的事件
                        //制定轮询时间，每一秒钟轮询一次
                        //当发送error或者empty时间，轮询被终止
                        // Observable.empty();
                        //  Observable.error(new NullPointerException());
                        return observable.delay(3, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e("repeat", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("repeat", "onError=" + e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("repeat", "integer=" + integer);
                    }
                });


    }
}
