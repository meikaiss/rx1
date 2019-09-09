package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

/**
 * Created by meikai on 2019/09/09.
 */
public class GroupByActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_groupby);


        findViewById(R.id.btn_groupby).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                groupby();
            }
        });
    }

    private void groupby() {

        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .groupBy(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        if (integer % 2 == 0) {
                            return "偶数";
                        } else {
                            return "奇数";
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupedObservable<String, Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GroupedObservable<String, Integer> stringIntegerGroupedObservable) {
                        Log.e("groupby", "key="+stringIntegerGroupedObservable.getKey());

                        if ("偶数".equals(stringIntegerGroupedObservable.getKey())){
                            stringIntegerGroupedObservable.subscribe(new Subscriber<Integer>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(Integer integer) {
                                    Log.e("groupby", "偶数："+ integer);
                                }
                            });

                        }else{

                            stringIntegerGroupedObservable.subscribe(new Subscriber<Integer>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(Integer integer) {
                                    Log.e("groupby", "寄数："+ integer);
                                }
                            });
                        }

                    }
                });

    }

}
