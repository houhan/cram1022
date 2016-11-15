package com.example.user.cram1001;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home_teacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);

        Button buttonbillboardteacher = (Button) findViewById(R.id.billboardteacher);//取得按鈕
        buttonbillboardteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Home_teacherActivity.this, Billboard_teacherActivity.class);
                Home_teacherActivity.this.startActivity(intent);
            }
        });//將這個Listener綑綁在這個Button

        Button buttoncheck = (Button) findViewById(R.id.attendanceteacher);//取得按鈕
        buttoncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Home_teacherActivity.this, CheckActivity.class);
                Home_teacherActivity.this.startActivity(intent);
            }
        });
    }
}