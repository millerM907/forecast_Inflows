package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

public class WeekActivity extends Fragment  {

    Button but_day;
    Button but_day1;
    Button but_day2;
    Button but_day3;
    Button but_day4;
    Button but_day5;
    Button but_day6;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_week, container, false);

        ResourseID resourseID = new ResourseID(getActivity());

        String[] dayOfWeek = {"ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};

        for(int i = 1; i < 7; i++){
            but_day = v.findViewById(resourseID.but_dayResourseID(i));
            switch (DayOfWeek.from(LocalDate.now(ZoneId.of("Asia/Magadan"))).plus(i)){
                case MONDAY:
                    but_day.setText(dayOfWeek[0]);
                    break;

                case TUESDAY:
                    but_day.setText(dayOfWeek[1]);
                    break;

                case WEDNESDAY:
                    but_day.setText(dayOfWeek[2]);
                    break;

                case THURSDAY:
                    but_day.setText(dayOfWeek[3]);
                    break;

                case FRIDAY:
                    but_day.setText(dayOfWeek[4]);
                    break;

                case SATURDAY:
                    but_day.setText(dayOfWeek[5]);
                    break;

                case SUNDAY:
                    but_day.setText(dayOfWeek[6]);
                    break;
            }

        }

        but_day1 = v.findViewById(R.id.but_day1);
        but_day2 = v.findViewById(R.id.but_day2);
        but_day3 = v.findViewById(R.id.but_day3);
        but_day4 = v.findViewById(R.id.but_day4);
        but_day5 = v.findViewById(R.id.but_day5);
        but_day6 = v.findViewById(R.id.but_day6);

        but_day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DayActivity.class);
                // передача объекта с ключом "hello" и значением "Hello World"
                intent.putExtra("keyDay", "1");
                // запуск SecondActivity
                startActivity(intent);
            }
        });

        but_day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(1);
            }
        });

        but_day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(2);
            }
        });

        but_day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(3);
            }
        });

        but_day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(4);
            }
        });

        but_day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(5);
            }
        });

        but_day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(6);
            }
        });

        return v;
    }

    private void startIntent(int key){
        Intent intent = new Intent(getContext(), DayActivity.class);
        // передача объекта с ключом "keyDay" и значением key
        intent.putExtra("keyDay", key);
        // запуск SecondActivity
        startActivity(intent);
    }

}
