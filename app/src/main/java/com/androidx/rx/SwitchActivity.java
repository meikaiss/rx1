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

/**
 * Created by meikai on 2019/09/10.
 */
public class SwitchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_swith);

        findViewById(R.id.btn_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchOnNext();
            }
        });

    }

    private void switchOnNext() {

        /**
         * 场景:
         * 每2秒发射一个数据
         * 此数据经过map的处理，变化 为一个 Observable(被观察者)
         * 这个被观察者 每50ms 发射一条数据
         *
         * 备注:
         * 以此模拟每1秒创建一个开始发射数据的 Observable，
         * 当有新的observable开始发射数据时，旧的正在发射数据的observable将被废弃
         *
         */
        Observable observable = Observable.interval(0,1, TimeUnit.SECONDS)
                .map(new Func1<Long, Observable>() {
                    @Override
                    public Observable call(Long l1) {

                        return Observable.interval(0,200, TimeUnit.MILLISECONDS)
                                .map(new Func1<Long, String>() {
                                    @Override
                                    public String call(Long l2) {

                                        return "" + l1 + ": " + l2;
                                    }
                                });
                    }
                });

        Observable.switchOnNext(observable)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("switch", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("switch", "onError");
                    }

                    @Override
                    public void onNext(String o) {
                        Log.e("switch", "onNext, o= " + o);
                    }
                });


    }


}
