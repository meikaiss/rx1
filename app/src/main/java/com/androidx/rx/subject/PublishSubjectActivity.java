package com.androidx.rx.subject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidx.rx.R;

import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by meikai on 2019/09/27.
 */
public class PublishSubjectActivity extends AppCompatActivity {

    private static final String TAG = "PublishSubject";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_publish_subject);

        findViewById(R.id.btn_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
    }

    private void init(){

        PublishSubject<Integer> subject = PublishSubject.create();
        subject.onNext(1);
        subject.onNext(2);
        subject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG, "Action1= " + integer);
            }
        });
        subject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG, "另一个Action1= " + integer);
            }
        });
        subject.onNext(3);
        subject.onNext(4);
        subject.onNext(5);
        subject.onNext(6);

    }
}
