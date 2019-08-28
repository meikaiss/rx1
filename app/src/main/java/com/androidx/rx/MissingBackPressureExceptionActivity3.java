package com.androidx.rx;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 尝试使用 Flowable 解决 背压问题
 * 结论：rx1.x 版本并没有提供 Flowable
 * Created by meikai on 2019/08/27.
 */
public class MissingBackPressureExceptionActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_pressure);

        findViewById(R.id.btn_back_pressure)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        init();
                    }
                });
    }

    private void init() {


    }
}
