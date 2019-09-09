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
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_first);


        findViewById(R.id.btn_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first();
            }
        });


        findViewById(R.id.btn_first_func).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstFunc();
            }
        });


        findViewById(R.id.btn_first_func_or_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstFuncOrDefault();
            }
        });


        findViewById(R.id.btn_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                single();
            }
        });

    }

    private void first() {

        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .first()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e("first", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("first", "onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("first", "integer = " + integer);
                    }
                });
    }

    private void firstFunc() {

        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .first(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
//                        int test = 3;
                        int test = 30;
                        return integer % test == 0;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e("first", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("first", "onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("first", "integer = " + integer);
                    }
                });
    }

    private void firstFuncOrDefault() {

        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .firstOrDefault(-1, new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
//                        int test = 3;
                        int test = 30;
                        return integer % test == 0;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e("first", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("first", "onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("first", "integer = " + integer);
                    }
                });
    }

    private void single() {

        Observable.just(1, 2)
                .single()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e("first", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("first", "onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("first", "integer = " + integer);
                    }
                });
    }


}
