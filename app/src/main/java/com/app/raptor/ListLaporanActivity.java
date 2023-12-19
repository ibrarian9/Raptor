package com.app.raptor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListLaporanActivity extends AppCompatActivity {

    FloatingActionButton plusBtn;
    CalendarView calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_laporan);

        plusBtn = findViewById(R.id.fabAdd);
        calender = findViewById(R.id.cv);
        calender.setVisibility(View.GONE);
        System.out.println(calender.getVisibility());

        if (calender.getVisibility() == View.VISIBLE){
            plusBtn.setOnClickListener( v -> calender.setVisibility(View.GONE));
        } else if (calender.getVisibility() == View.GONE){
            plusBtn.setOnClickListener( v -> calender.setVisibility(View.VISIBLE));
        }

        System.out.println(calender.getVisibility());
    }
}