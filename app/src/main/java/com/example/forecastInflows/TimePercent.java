package com.example.forecastInflows;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimePercent {
    public static int calculatePercentUntilEndCycle(String waterState, String previousTime, String nextTime){

        int percentTides = -100;

        if(previousTime.equals("-100") || nextTime.equals("-100")){
            return percentTides;
        } else {
            ZoneId leavingZone = ZoneId.of("Asia/Magadan");
            LocalDateTime localTime = LocalDateTime.now(leavingZone);

            //переводим предыдущее время в секунды
            OffsetDateTime dateStartCycleNew = OffsetDateTime.parse(previousTime);
            long previousTimeNumber = dateStartCycleNew.toEpochSecond();

            //переводим следующее время в секунды
            OffsetDateTime dateEndCycleNew = OffsetDateTime.parse(nextTime);
            long nextTimeNumber = dateEndCycleNew.toEpochSecond();

            //если nextTimeNumber - следующий день
            if(previousTimeNumber > nextTimeNumber){
                dateEndCycleNew = dateEndCycleNew.plusDays(1);
                nextTimeNumber = dateEndCycleNew.toEpochSecond();
            }

            //вычисляем текущее время в секундах
            long currentTimeNumber = localTime.toEpochSecond(ZoneOffset.UTC);

            currentTimeNumber -= previousTimeNumber;
            nextTimeNumber -= previousTimeNumber;

            percentTides = (int)((currentTimeNumber * 100) / nextTimeNumber);

            //если отлив - проценты идут в обратном порядке: от 100 к 0
            if(waterState.equals("false")){
                percentTides = 100 - percentTides;
            }
        }

        return percentTides;
    }
}