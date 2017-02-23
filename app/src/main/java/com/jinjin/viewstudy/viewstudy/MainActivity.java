package com.jinjin.viewstudy.viewstudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jinjin.viewstudy.viewstudy.activity.ClockViewTestActivity01;
import com.jinjin.viewstudy.viewstudy.activity.ImageViewTestActivity01;
import com.jinjin.viewstudy.viewstudy.activity.TextViewTestActivity01;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        Button buttonText01 = (Button) findViewById(R.id.btn_test_text01);
        Button buttonImage01 = (Button) findViewById(R.id.btn_test_image01);
        Button buttonClock01 = (Button) findViewById(R.id.btn_test_clock01);
        buttonText01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TextViewTestActivity01.class);
                startActivity(intent);
            }
        });
        buttonImage01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageViewTestActivity01.class);
                startActivity(intent);
            }
        });
        buttonClock01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClockViewTestActivity01.class);
                startActivity(intent);
            }
        });
    }


}
