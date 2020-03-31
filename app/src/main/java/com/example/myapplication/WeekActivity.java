package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeekActivity extends Fragment  {

    Button but_today;
    Button but_day1;
    Button but_day2;
    Button but_day3;
    Button but_day4;
    Button but_day5;
    Button but_day6;
    Button but_day7;
    Button but_day8;
    Button but_day9;
    Button but_day10;
    Button but_day11;
    Button but_day12;

    TextView tv_day;

    TextView tv_month;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_week, container, false);

        ResourseID resourseID = new ResourseID(getActivity());

        List<String> monthOfEnglish = new ArrayList<>();
        Collections.addAll(monthOfEnglish,
                "JANUARY",
                "FEBRUARY",
                "MARCH",
                "APRIL",
                "MAY",
                "JUNE",
                "JULY",
                "AUGUST",
                "SEPTEMBER",
                "OCTOBER",
                "NOVEMBER",
                "DECEMBER");

        List<String> monthOfRussian = new ArrayList<>();
        Collections.addAll(monthOfRussian,
                "ЯНВАРЬ",
                "ФЕВРАЛЬ",
                "МАРТ",
                "АПРЕЛЬ",
                "МАЙ",
                "ИЮНЬ",
                "ИЮЛЬ",
                "АВГУСТ",
                "СЕНТЯБРЬ",
                "ОКТЯБРЬ",
                "НОЯБРЬ",
                "ДЕКАБРЬ");

        String currentMonthPlusOneDay = LocalDate.now(ZoneId.of("Asia/Magadan")).plusDays(1).getMonth().toString();

        tv_month = v.findViewById(R.id.tv_month);
        tv_month.setText(monthOfRussian.get(monthOfEnglish.indexOf(currentMonthPlusOneDay)));

        String[] dayOfWeek = {"ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};

        for(int i = 1; i <= 12; i++){
            String date;
            but_today = v.findViewById(resourseID.but_dayResourseID(i));
            tv_day = v.findViewById(resourseID.tv_dayResourseID(i));
            switch (DayOfWeek.from(LocalDate.now(ZoneId.of("Asia/Magadan"))).plus(i)){
                case MONDAY:
                    date = String.valueOf((LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth());
                    but_today.setText(date);
                    tv_day.setText(dayOfWeek[0]);
                    break;

                case TUESDAY:
                    date = String.valueOf((LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth());
                    but_today.setText(date);
                    tv_day.setText(dayOfWeek[1]);
                    break;

                case WEDNESDAY:
                    date = String.valueOf((LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth());
                    but_today.setText(date);
                    tv_day.setText(dayOfWeek[2]);
                    break;

                case THURSDAY:
                    date = String.valueOf((LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth());
                    but_today.setText(date);
                    tv_day.setText(dayOfWeek[3]);
                    break;

                case FRIDAY:
                    date = String.valueOf((LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth());
                    but_today.setText(date);
                    tv_day.setText(dayOfWeek[4]);
                    break;

                case SATURDAY:
                    date = String.valueOf((LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth());
                    but_today.setText(date);
                    tv_day.setText(dayOfWeek[5]);
                    break;

                case SUNDAY:
                    date = String.valueOf((LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth());
                    but_today.setText(date);
                    tv_day.setText(dayOfWeek[6]);
                    break;
            }

        }

        String currentMonth = LocalDate.now(ZoneId.of("Asia/Magadan")).getMonth().toString();

        //если дата на кнопке относится к след месяцу, то мы устанавливаем ей неактивный фон
        for(int i = 1; i <= 12; i++){
            but_today = v.findViewById(resourseID.but_dayResourseID(i));
            if(!currentMonth.equals(String.valueOf((LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getMonth()))){
                but_today.setBackgroundResource(R.drawable.my_bottom2);
            }
        }

        but_day1 = v.findViewById(R.id.but_day1);
        but_day2 = v.findViewById(R.id.but_day2);
        but_day3 = v.findViewById(R.id.but_day3);
        but_day4 = v.findViewById(R.id.but_day4);
        but_day5 = v.findViewById(R.id.but_day5);
        but_day6 = v.findViewById(R.id.but_day6);
        but_day7 = v.findViewById(R.id.but_day7);
        but_day8 = v.findViewById(R.id.but_day8);
        but_day9 = v.findViewById(R.id.but_day9);
        but_day10  = v.findViewById(R.id.but_day10);
        but_day11  = v.findViewById(R.id.but_day11);
        but_day12  = v.findViewById(R.id.but_day12);

        but_day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(1)){
                    startIntent(1, but_day1.getText().toString());
                }
            }
        });

        but_day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(2)){
                    startIntent(2, but_day2.getText().toString());
                }

            }
        });

        but_day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(3)){
                    startIntent(3, but_day3.getText().toString());
                }
            }
        });

        but_day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(4)){
                    startIntent(4, but_day4.getText().toString());
                }
            }
        });

        but_day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(5)){
                    startIntent(5, but_day5.getText().toString());
                }
            }
        });

        but_day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(6)){
                    startIntent(6, but_day6.getText().toString());
                }
            }
        });

        but_day7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(7)){
                    startIntent(7, but_day7.getText().toString());
                }
            }
        });

        but_day8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(8)){
                    startIntent(8, but_day8.getText().toString());
                }
            }
        });

        but_day9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(9)){
                    startIntent(9, but_day9.getText().toString());
                }
            }
        });


        but_day10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(10)){
                    startIntent(10, but_day10.getText().toString());
                }
            }
        });

        but_day11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(11)){
                    startIntent(11, but_day11.getText().toString());
                }
            }
        });

        but_day12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getEqualsMonth(12)){
                    startIntent(12, but_day12.getText().toString());
                }
            }
        });

        return v;
    }

    private void startIntent(int key, String butText){
        Intent intent = new Intent(getContext(), DayActivity.class);
        // передача объекта с ключом "keyDay" и значением key
        intent.putExtra("keyDay", key);
        intent.putExtra("nameDay", butText);
        // запуск SecondActivity
        startActivity(intent);
    }

    private boolean getEqualsMonth(int dayIndex){
        boolean equals = false;
        if(LocalDateTime.now(ZoneId.of("Asia/Magadan")).plusDays(dayIndex).getMonth().equals(LocalDateTime.now(ZoneId.of("Asia/Magadan")).getMonth())){
            equals = true;
        }
        return equals;
    }
}