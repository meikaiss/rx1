package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by meikai on 2019/11/21.
 */
public class MergeActivity extends AppCompatActivity {

    private static final String TAG = "Merge";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_merge);


        findViewById(R.id.btn_merge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merge();
            }
        });

    }

    private void merge() {

        Observable observable1 = Observable.unsafeCreate(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i * 10);
                }

                for (int i = 100; i < 110; i++) {
                    subscriber.onNext(i * 10);
                }
            }
        });

        Observable observable2 = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("abc1");
                subscriber.onNext("abc2");
                subscriber.onNext("abc3");
                subscriber.onNext("abc4");
                subscriber.onError(new Exception("测试"));
                subscriber.onNext("abc5");

            }
        });

        Observable.mergeDelayError(observable1, observable2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError, e=" + e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.e(TAG, "onNext, o=" + o.toString());
                    }
                });

    }


}
