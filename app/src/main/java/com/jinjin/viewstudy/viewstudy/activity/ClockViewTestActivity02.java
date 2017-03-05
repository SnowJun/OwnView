package com.jinjin.viewstudy.viewstudy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jinjin.viewstudy.viewstudy.R;
import com.jinjin.viewstudy.viewstudy.view.DemoClockView01;

import java.util.Calendar;

/**
 * @since 1.0
 * <p/>
 * SnowJun  2017/2/9
 */
public class ClockViewTestActivity02 extends AppCompatActivity implements View.OnClickListener {

    private DemoClockView01 mDemoClockView01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clocktest02);

        mDemoClockView01 = (DemoClockView01) findViewById(R.id.dv_clock);

        Button button0 = (Button) findViewById(R.id.btn_set0);
        Button button1 = (Button) findViewById(R.id.btn_set1);
        Button button2 = (Button) findViewById(R.id.btn_set2);
        Button button3 = (Button) findViewById(R.id.btn_set3);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_set0://设置时间为00:00:00
                mDemoClockView01.setTime(Calendar.getInstance());
                break;
            case R.id.btn_set1://设置时间为00:00:00
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                mDemoClockView01.setTime(calendar);
                break;
            case R.id.btn_set2://设置时间为03:02:30
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR,03);
                calendar1.set(Calendar.MINUTE,02);
                calendar1.set(Calendar.SECOND,30);
                mDemoClockView01.setTime(calendar1);
                break;
            case R.id.btn_set3://设置时间为11:11:11
                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.HOUR,11);
                calendar2.set(Calendar.MINUTE,11);
                calendar2.set(Calendar.SECOND,11);
                mDemoClockView01.setTime(calendar2);
                break;
        }
    }
}
