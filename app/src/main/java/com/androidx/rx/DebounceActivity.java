package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class DebounceActivity extends AppCompatActivity {

    @BindView(R.id.btn_debounce)
    public Button btnDebounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debounce);
        ButterKnife.bind(this);

        findViewById(R.id.btn_debounce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDebounce(v);
            }
        });
    }

    /**
     * 1、发送onNext时，所有 onNext都会延迟3秒生效，若在3秒内有新onNext，则会覆盖之前的所有onNext;
     * 2、发送onComplete时， 会立即执行且只会执行一次
     * 3、发送完onComplete后，后续发送的onNext都不会收到
     * @param view
     */
    @OnClick(R.id.btn_debounce)
    public void onClickDebounce(View view) {
        final PublishSubject publishSubject = PublishSubject.create();

        Observable observable = publishSubject.debounce(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread());

        Observer observer = new Observer() {
            @Override
            public void onCompleted() {
                Log.e("debounce", "4.onComplete");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                Log.e("debounce", "3.收到onNext消息，值=" + o);
                Toast.makeText(DebounceActivity.this, o.toString(), Toast.LENGTH_SHORT).show();

            }
        };

        Log.e("debounce", "1.observable.subscribe start");
        observable.subscribe(observer);
        Log.e("debounce", "2.observable.subscribe end");

        findViewById(R.id.btn_debounce_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("debounce", "onNext 点击");
                publishSubject.onNext(1);
            }
        });

        findViewById(R.id.btn_debounce_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishSubject.onCompleted();
            }
        });
    }


}
