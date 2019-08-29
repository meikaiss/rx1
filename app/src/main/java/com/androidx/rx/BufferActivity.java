package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by meikai on 2019/08/28.
 */
public class BufferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);

        findViewById(R.id.btn_buffer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSingleBuffer2();
            }
        });
    }

    private void initNormalBuffer() {

        /**
         * buffer 为 源头 创建一个 缓冲池
         * 当满足条件时将池子内的事件，作为list列表发送给下游
         * 条件可以是 池子内事件数量达到某一数量 或 距上一次释放池子的时间
         */

        /**
         * buffer(1000, 200)
         *
         * 池子容量是1000，满容后触发一次批量发送
         *
         * 第二个参数表示：每个缓冲区的起始位置之间的距离
         * 当skip< count时: 新的缓冲池子中前(count-skip)个数据是 上一个缓冲区的 后(count-skip)个数据
         * 当skip= count时: 新的缓冲池子中即没有上一个池子的数据，也没有丢失数据
         * 当skip> count时: 新的缓冲池子中 会丢失 源头发射过来的 (skip-count)个数据
         *
         * 默认情况下，第二个参数等同于池子的大小，即满容后，会完全清空池子。
         * 池子满后，新事件会放到队尾，队头元素出队
         */

        /**
         * buffer(long timespan, TimeUnit unit)
         *
         * 将源头数据全部放入池子中，每达到 buffer 参数中指定的时间间隔，发射池子中的所有数据
         * 若达到时间间隔，但池子是空的，此时也会发送空数据list
         *
         */

        /**
         * buffer(long timespan, TimeUnit unit, int count)
         *
         * 池子数量 或 时间间隔 两个条件，有任意一个达到时，触发一次批量发送
         *
         */

        /**
         * buffer(long timespan, long timeshift, TimeUnit unit)
         *
         * 第二个参数表示：基于时间维度的，每次创建缓冲区时，新缓冲区的时间偏移
         * timespan = timeshift， 新旧时间缓冲区没有相互覆盖，不多不少
         * timespan < timeshift， 新缓存区会丢失 (timeshift-timespan) 这一段时间内的数据
         * timespan > timeshift， 新缓存区会存在与旧区 后(timespan-timeshift)时间段重复的数据
         *
         */
        Observable.interval(1, TimeUnit.MILLISECONDS)
//                .buffer(1, TimeUnit.SECONDS, 2000)
                .buffer(2000, 500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Long>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("buffer", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("buffer", "onError, " + e.toString());
                    }

                    @Override
                    public void onNext(List<Long> longs) {
                        int size = longs == null ? 0 : longs.size();

                        long last = -1;
                        if (longs != null || longs.size() > 0) {
                            last = longs.get(longs.size() - 1);
                        }

                        Log.e("buffer", "下游收到数据的个数=" + size + " ，最后一条数据=" + last);
                    }
                });

    }

    private void initSingleBuffer() {


        /**
         * 发射信号缓冲区
         * buffer(Observable<B> boundary)
         *
         * 每当 入参boundary 上发射 onNext 信号时，才发射 缓冲区中的所有值
         * 如果您让缓冲区等到在您准备好接受它们时才发射，那么使用信号缓冲来实现
         *
         */
        Observable.interval(1, TimeUnit.MILLISECONDS)
                .buffer(Observable.interval(250, TimeUnit.MILLISECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Long>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("buffer", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("buffer", "onError, " + e.toString());
                    }

                    @Override
                    public void onNext(List<Long> longs) {
                        int size = longs == null ? 0 : longs.size();

                        long last = -1;
                        if (longs != null || longs.size() > 0) {
                            last = longs.get(longs.size() - 1);
                        }

                        Log.e("buffer", "下游收到数据的个数=" + size + " ，最后一条数据=" + last);
                    }
                });

    }


    private void initSingleBuffer2() {

        /**
         * 开始接收、关闭接收 信号缓冲区
         * buffer(Observable<? extends TOpening> bufferOpenings, Func1<? super TOpening, ? extends Observable<? extends TClosing>> bufferClosingSelector)
         *
         * 入参1 是一个Observable，每当 入参1 bufferOpenings 上发射 onNext 信号时，就会创建一个新的缓冲区。
         *
         * 入参2 是一个函数，每当 入参2 bufferClosingSelector 上发射 onNext 信号时，就会关闭当前 的缓冲区，并发射缓冲区中的所有数据
         *
         * 注意：
         * 每当入参1 发一个信号时，同时它所发出的值也会传递给缓冲区ClosingSelector，它是一个函数。
         * 该函数使用该值创建一个新的可观察值，当它发出第一个onNext事件时，它将发出相应缓冲区的结束信号。
         *
         *
         * 延伸：
         * 用Interval创建的可观察对象不会立即发出值
         *
         */
        Observable.interval(1, TimeUnit.MILLISECONDS)
                .buffer(Observable.interval(250, TimeUnit.MILLISECONDS),
                        new Func1<Long, Observable<?>>() {
                            @Override
                            public Observable<?> call(Long aLong) {
                                Log.e("buffer", "入参2 call , " + aLong);
                                return Observable.timer(100, TimeUnit.MILLISECONDS);
                            }
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Long>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("buffer", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("buffer", "onError, " + e.toString());
                    }

                    @Override
                    public void onNext(List<Long> longs) {
                        int size = longs == null ? 0 : longs.size();

                        long last = -1;
                        if (longs != null || longs.size() > 0) {
                            last = longs.get(longs.size() - 1);
                        }

                        Log.e("buffer", "下游收到数据的个数=" + size + " ，最后一条数据=" + last);
                    }
                });
    }

}
