package com.example.annotationprocesserdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.qhh_annotation.BindView;
import com.example.qhh_api.QhhButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_hello)
    TextView tv_hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QhhButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("qhhqhh","textview tv_hello = " +tv_hello);
        tv_hello.setText("Hello Annotation");
    }
}


















