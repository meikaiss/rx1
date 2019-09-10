package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by meikai on 2019/09/10.
 */
public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_join);

        findViewById(R.id.btn_join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join();
            }
        });

    }

    private void join() {

        Observable observable1 = Observable.interval(2, TimeUnit.SECONDS);
        Observable observable2 = Observable.interval(4, TimeUnit.SECONDS);

        Log.e("join", "join");

        observable1.join(observable2,
                new Func1<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Long aLong) {
                        return Observable.timer(5, TimeUnit.SECONDS);
                    }
                }, new Func1<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Long aLong) {
                        return Observable.timer(10, TimeUnit.SECONDS);
                    }
                },
                new Func2<Long, Long, String>() {
                    @Override
                    public String call(Long aLong, Long aLong2) {
                        Log.e("join", "call");
                        return "aLong1 = " + aLong + " , aLong2 = " + aLong2;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("join", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("join", "onError");
                    }

                    @Override
                    public void onNext(String o) {
                        Log.e("join", "最终结果 : " + o);
                    }
                });


    }


}
