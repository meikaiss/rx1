package com.androidx.rx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by meikai on 2019/08/06.
 */
public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rx_map);


        findViewById(R.id.btn_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

        findViewById(R.id.btn_map_just).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMapJust();
            }
        });

        /**
         *  官方解释：
         *  对Observable发射的数据都应用一个函数，然后再发射最后的结果集。最后map()方法返回一个新的Observable。
         *
         *  通俗理解：
         *  map用于将输入源应用一个函数，转换成另一种格式的输入源
         */
    }

    private void initMapJust() {
        Observable.just(
                "http://www.baidu.com/",
                "http://www.google.com/",
                "https://www.bing.com/")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {

                        try {
                            return s +"的ip="+ getIPByUrl(s);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG, s);
                    }
                });

    }

    private void init() {

        //把 字符数组 转成大写，再反序排列
        String[] source = {"This", "is", "RxJava"};

        long s = System.currentTimeMillis();

        Observable
                .from(source)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s.toUpperCase();
                    }
                })
                .toList()
                .map(new Func1<List<String>, List<String>>() {
                    @Override
                    public List<String> call(List<String> strings) {
                        Collections.reverse(strings);
                        return strings;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {

                        long e = System.currentTimeMillis();

                        Log.e(TAG, "耗时=" + (e - s));

                        for (int i = 0; i < strings.size(); i++) {
                            Log.e(TAG, strings.get(i));
                        }

                    }
                });


        long s1 = System.currentTimeMillis();
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < source.length; i++) {
            strings.add(source[i].toUpperCase());
        }
        Collections.reverse(strings);
        long e1 = System.currentTimeMillis();
        Log.e(TAG, "耗时=" + (e1 - s1));
        for (int i = 0; i < strings.size(); i++) {
            Log.e(TAG, strings.get(i));
        }

        /**
         * 一些简单的操作，Rx确实会将简单问题复杂化，并且消耗更多的时间
         */

    }

    private String getIPByUrl(String str) throws MalformedURLException, UnknownHostException {
        URL urls = new URL(str);
        String host = urls.getHost();
        String address = InetAddress.getByName(host).toString();
        int b = address.indexOf("/");
        return address.substring(b + 1);

    }

}