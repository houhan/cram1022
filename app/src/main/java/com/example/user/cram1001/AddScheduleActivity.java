package com.example.user.cram1001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity {
    private Spinner spinner;
    private ArrayAdapter<String> lunchList;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        spinner = (Spinner)findViewById(R.id.spinner);

        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        ArrayAdapter adapter = new ArrayAdapter (this,android.R.layout.simple_spinner_item,new String[]{"星期一","星期二","星期三","星期","星期五"});

        //設定下拉選單的樣式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

        // 获取当前的年、月、日、小时、分钟
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);


    }
}
