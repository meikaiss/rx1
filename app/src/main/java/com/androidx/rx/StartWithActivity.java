package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rx.Observable;
import rx.Observer;

/**
 * Created by meikai on 2019/09/10.
 */
public class StartWithActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_startwith);

        findViewById(R.id.btn_start_with).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWith();
            }
        });

    }

    private void startWith() {

        Observable.range(1, 5)
                .startWith(Observable.range(100, 10))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e("startWith", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("startWith", "onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("startWith", "integer=" + integer);
                    }
                });

    }


}
