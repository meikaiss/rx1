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
public class Map2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map_2);


        findViewById(R.id.btn_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map();
            }
        });

        findViewById(R.id.btn_cast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cast();
            }
        });
    }

    private void map() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return "电量等级:" + integer;
                    }
                }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e("map", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("map", "onError");
            }

            @Override
            public void onNext(String s) {
                Log.e("map", "onNext, s=" + s);
            }
        });
    }

    private void cast() {

        Observable.just(1, 2, 3L, 4.0, 5, 6, 7, 8)
                .cast(Integer.class) // 只能将父类对象 转成 它的子类 类型
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e("cast", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("cast", "onError");
                    }

                    @Override
                    public void onNext(Integer s) {
                        Log.e("cast", "onNext, s=" + s);
                    }
                });

    }


}
