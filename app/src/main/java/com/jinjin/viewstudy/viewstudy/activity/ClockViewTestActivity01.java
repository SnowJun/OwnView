package com.jinjin.viewstudy.viewstudy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jinjin.viewstudy.viewstudy.R;

/**
 * @since 1.0
 * <p/>
 * SnowJun  2017/2/9
 */
public class ClockViewTestActivity01 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clocktest01);
        Button button = (Button) findViewById(R.id.btn_test_clock02);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClockViewTestActivity01.this, ClockViewTestActivity02.class);
                startActivity(intent);
            }
        });
    }

}
