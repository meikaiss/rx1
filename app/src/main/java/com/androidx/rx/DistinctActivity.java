package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by meikai on 2019/09/09.
 */
public class DistinctActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_distinct);


        findViewById(R.id.btn_distinct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distinct();
            }
        });

        findViewById(R.id.btn_distinct_key).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distinctKey();
            }
        });

        findViewById(R.id.btn_distinctUntilChanged).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distidistinctUntilChangednctKey();
            }
        });

    }

    private void distinct() {
        Observable.just(1, 2, 3, 1, 2, 4, 5, 4)
                .distinct()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("distinct", "onNext, " + integer);
                    }
                });
    }


    private void distinctKey() {
        Observable.just(1, 2, 3, 1, 2, 4, 5, 4)
                .distinct(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return "key:" + integer;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("distinct", "onNext, " + integer);
                    }
                });
    }

    // 只过滤 连续相邻 的后续数据
    private void distidistinctUntilChangednctKey() {
        Observable.just(1, 2, 3, 1, 1, 4, 4, 4)
                .distinctUntilChanged()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("distinct", "onNext, " + integer);
                    }
                });

    }

}
