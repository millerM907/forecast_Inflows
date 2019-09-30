package com.example.myapplication;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimePercent {
    private String waterState, previousTime,  nextTime;

    public TimePercent(String waterState, String previousTime, String nextTime){
        this.waterState = waterState;
        this.previousTime = previousTime;
        this.nextTime = nextTime;
    }

    public static int calculatePercentUntilEndCycle(String waterState, String previousTime, String nextTime){

        //добавляем 0, если дата в формате h:MM
        if(previousTime.length() == 4){
            previousTime = "0" + previousTime;
        }
        if(nextTime.length() == 4){
            nextTime = "0" + nextTime;
        }

        OffsetDateTime today = OffsetDateTime.now(ZoneId.of("Asia/Magadan"));
        String currentYearMonthDay = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String dateStartCycle = currentYearMonthDay + "T" + previousTime + ":00.000000000+11:00";
        String dateEndCycle = currentYearMonthDay + "T" + nextTime + ":00.000000000+11:00";

        //вычисляем предыдущее время в миллисекундах
        OffsetDateTime dateStartCycleNew = OffsetDateTime.parse(dateStartCycle);
        long previousTimeNumber = dateStartCycleNew.toEpochSecond();

        //вычисляем следующее время в миллисекундах
        OffsetDateTime dateEndCycleNew = OffsetDateTime.parse(dateEndCycle);
        long nextTimeNumber = dateEndCycleNew.toEpochSecond();

        //если nextTimeNumber - следующий день
        if(previousTimeNumber > nextTimeNumber){
            dateEndCycleNew = dateEndCycleNew.plusDays(1);
            nextTimeNumber = dateEndCycleNew.toEpochSecond();
        }

        //вычисляем текущее время в миллисекундах
        long currentTimeNumber = today.toEpochSecond();

        currentTimeNumber -= previousTimeNumber;
        nextTimeNumber -= previousTimeNumber;

        int percentTides = (int)((currentTimeNumber * 100) / nextTimeNumber);

        //если отлив - проценты идут в обратном порядке: от 100 к 0
        if(waterState.equals("low_water")){
            percentTides = 100 - percentTides;
        }

        return percentTides;
    }
}