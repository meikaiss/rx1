package com.androidx.rx.scheduler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidx.rx.R;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Schedulers.from可以指定 Observable 的上游执行线程，达到自己控制线程池大小的目的.
 * Schedulers.io() 是一个 无数量上限的线程池
 *
 * 线程池 无数量上限 和 有数量上限 都会对用户造成一定的影响
 * 1、无数量上限：容易导致OOM及整个app的卡顿、电量消耗过多且是在无用功
 * 2、有数量上限：不会OOM，但达到上限后的任务需要等待的时间会更长
 *
 * 结论：等待更长比oom更容易接受一点，
 * Created by meikai on 2019/10/14.
 */
public class SchedulerActivity extends AppCompatActivity {

    private static final String TAG = "Schedulers.from";

    private Executor executor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_scheduler);

        executor = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("自己创建的线程-" + UUID.randomUUID());
                        return thread;
                    }
                }, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            }
        });


        findViewById(R.id.btn_schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnScheduleClick();

                /**
                 * 执行两遍，以观察多线程的场景
                 * 两个Observable都是执行在同一Scheduler上，这个Schedulers是通过一个自定义的线程池来创建的
                 */
                btnScheduleClick();
            }
        });
    }


    private void btnScheduleClick() {

        Observable.unsafeCreate(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {

                Log.e(TAG, "create, " + Thread.currentThread().getName());

                subscriber.onNext(1l);
                sleep();

                subscriber.onNext(2l);
                sleep();

                subscriber.onNext(3l);
                sleep();

                subscriber.onNext(4l);
                sleep();

            }
        })
                .subscribeOn(Schedulers.from(executor))
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        Log.e(TAG, "map=" + aLong + ", " + Thread.currentThread().getName());
                        return 100 * aLong;
                    }
                })
                .take(1000)
                .observeOn(AndroidSchedulers.mainThread())
                .forEach(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.e(TAG, "call=" + aLong + ", " + Thread.currentThread().getName());
                    }
                });

    }


    private void sleep() {
        try {
            //模拟耗时任务
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
