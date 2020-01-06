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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_week, container, false);

        ResourseID resourseID = new ResourseID(getActivity());

        String[] dayOfWeek = {"ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};
        String point = ",  ";

        for(int i = 1; i < 10; i++){
            String date;
            but_today = v.findViewById(resourseID.but_dayResourseID(i));
            switch (DayOfWeek.from(LocalDate.now(ZoneId.of("Asia/Magadan"))).plus(i)){
                case MONDAY:
                    date = dayOfWeek[0] + point + (LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth();
                    but_today.setText(date);
                    break;

                case TUESDAY:
                    date = dayOfWeek[1] + point + (LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth();
                    but_today.setText(date);
                    break;

                case WEDNESDAY:
                    date = dayOfWeek[2] + point + (LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth();
                    but_today.setText(date);
                    break;

                case THURSDAY:
                    date = dayOfWeek[3] + point + (LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth();
                    but_today.setText(date);
                    break;

                case FRIDAY:
                    date = dayOfWeek[4] + point + (LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth();
                    but_today.setText(date);
                    break;

                case SATURDAY:
                    date = dayOfWeek[5] + point + (LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth();
                    but_today.setText(date);
                    break;

                case SUNDAY:
                    date = dayOfWeek[6] + point + (LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getDayOfMonth();
                    but_today.setText(date);
                    break;
            }

        }

        String[] monthOfYear = {"  янв", "  фев", "  мар", "  апр", "  май", "  июнь", "  июль", "  авг", "  сен", "  окт", "  нояб", "  дек"};


        String currentMonth = LocalDate.now(ZoneId.of("Asia/Magadan")).getMonth().toString();

        for(int i = 1; i < 10; i++){
            String date;
            but_today = v.findViewById(resourseID.but_dayResourseID(i));
            switch ((LocalDate.now(ZoneId.of("Asia/Magadan"))).plusDays(i).getMonth()){
                case JANUARY:
                   if(!currentMonth.equals("JANUARY")){
                       but_today.setBackgroundResource(R.drawable.my_bottom2);
                   }
                   date = but_today.getText() + monthOfYear[0];
                   but_today.setText(date);
                   break;
                case FEBRUARY:
                    if(!currentMonth.equals("FEBRUARY")){
                        but_today.setBackgroundResource(R.drawable.my_bottom2);
                    }
                    date = but_today.getText() + monthOfYear[1];
                    but_today.setText(date);
                    break;
                case MARCH:
                    if(!currentMonth.equals("MARCH")){
                        but_today.setBackgroundResource(R.drawable.my_bottom2);
                    }
                    date = but_today.getText() + monthOfYear[2];
                    but_today.setText(date);
                    break;
                case APRIL:
                    if(!currentMonth.equals("APRIL")){
                        but_today.setBackgroundResource(R.drawable.my_bottom2);
                    }
                    date = but_today.getText() + monthOfYear[3];
                    but_today.setText(date);
                    break;
                case MAY:
                    if(!currentMonth.equals("MAY")){
                        but_today.setBackgroundResource(R.drawable.my_bottom2);
                    }
                    date = but_today.getText() + monthOfYear[4];
                    but_today.setText(date);
                    break;
                case JUNE:
                    if(!currentMonth.equals("JUNE")){
                        but_today.setBackgroundResource(R.drawable.my_bottom2);
                    }
                    date = but_today.getText() + monthOfYear[5];
                    but_today.setText(date);
                    break;
                case JULY:
                    if(!currentMonth.equals("JULY")){
                        but_today.setBackgroundResource(R.drawable.my_bottom2);
                    }
                    date = but_today.getText() + monthOfYear[6];
                    but_today.setText(date);
                    break;
                case AUGUST:
                    if(!currentMonth.equals("AUGUST")){
                        but_today.setBackgroundResource(R.drawable.my_bottom2);
                    }
                    date = but_today.getText() + monthOfYear[7];
                    but_today.setText(date);
                    break;
                case SEPTEMBER:
                    if(!currentMonth.equals("SEPTEMBER")){
                        but_today.setBackgroundResource(R.drawable.my_bottom2);
                    }
                    date = but_today.getText() + monthOfYear[8];
                    but_today.setText(date);
                    break;
                case OCTOBER:
                    if(!currentMonth.equals("OCTOBER")){
                        but_today.setBackgroundResource(R.drawable.my_bottom2);
                    }
                    date = but_today.getText() + monthOfYear[9];
                    but_today.setText(date);
                    break;
                case NOVEMBER:
                    if(!currentMonth.equals("NOVEMBER")){
                        but_today.setBackgroundResource(R.drawable.my_bottom2);
                    }
                    date = but_today.getText() + monthOfYear[10];
                    but_today.setText(date);
                    break;
                case DECEMBER:
                    if(!currentMonth.equals("DECEMBER")){
                        but_today.setBackgroundResource(R.drawable.my_bottom2);
                    }
                    date = but_today.getText() + monthOfYear[11];
                    but_today.setText(date);
                    break;
            }
        }

        but_today = v.findViewById(R.id.but_today);
        but_day1 = v.findViewById(R.id.but_day1);
        but_day2 = v.findViewById(R.id.but_day2);
        but_day3 = v.findViewById(R.id.but_day3);
        but_day4 = v.findViewById(R.id.but_day4);
        but_day5 = v.findViewById(R.id.but_day5);
        but_day6 = v.findViewById(R.id.but_day6);
        but_day7 = v.findViewById(R.id.but_day7);
        but_day8 = v.findViewById(R.id.but_day8);
        but_day9 = v.findViewById(R.id.but_day9);

        but_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
