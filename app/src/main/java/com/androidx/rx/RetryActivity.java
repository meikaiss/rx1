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
import rx.functions.Func2;

/**
 * Created by meikai on 2019/09/09.
 */
public class RetryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_retry);


        findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                retry();

                retryWhen();
            }
        });
    }

    private void retry() {

        // retry  当onError时，不会发射error，而是重试n次，完全从源头重新开始发射

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onError(new Exception("test"));
                subscriber.onNext(4);
                subscriber.onNext(5);

            }
        }).retry(3)
                .retry(new Func2<Integer, Throwable, Boolean>() {
                    @Override
                    public Boolean call(Integer integer, Throwable throwable) {

                        //入参1 ： 重试的次数
                        //入参2 ： 上游onError方法抛弃的 Throwable， 这里

                        return null; //true:重新订阅; false:不重新订阅，任由此Throwable发射到下游
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e("retry", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("retry", "onError=" + e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("retry", "integer=" + integer);
                    }
                });

    }

    private void retryWhen() {

        // retryWhen  当onError时，不会发射error，而是重试n次，完全从源头重新开始发射

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onError(new Exception("test"));
                subscriber.onNext(4);
                subscriber.onNext(5);

            }
        }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {

                // 当onError时，不会发射error，而是延迟 1 秒后，完全从源头重新开始发射，无限循环

                return observable.delay(1, TimeUnit.SECONDS);
            }
        })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e("retry", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("retry", "onError=" + e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("retry", "integer=" + integer);
                    }
                });

    }

}
