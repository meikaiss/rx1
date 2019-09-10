package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.functions.Func2;

/**
 * Created by meikai on 2019/09/10.
 */
public class CombineLastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_combine_last);

        findViewById(R.id.btn_combine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                combine();
            }
        });

    }

    private void combine() {

        Observable observable1 = Observable.interval(2, TimeUnit.SECONDS);
        Observable observable2 = Observable.interval(3, TimeUnit.SECONDS);

        Log.e("combine", "combine");

        Observable.combineLatest(observable1, observable2, new Func2<Long, Long, String>() {
            @Override
            public String call(Long aLong, Long aLong2) {
                return "l1=" + aLong + " , l2=" + aLong2;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String o) {
                Log.e("combine", "最终结果 : " + o);
            }
        });


    }


}
