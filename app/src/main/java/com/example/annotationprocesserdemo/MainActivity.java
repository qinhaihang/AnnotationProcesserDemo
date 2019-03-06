package com.example.annotationprocesserdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.qhh_api.QhhButterKnife;

public class MainActivity extends AppCompatActivity {
    
    TextView tv_hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QhhButterKnife.inject(this);
    }
}
