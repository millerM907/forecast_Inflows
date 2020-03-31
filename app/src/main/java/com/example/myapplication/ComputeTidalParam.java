package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ComputeTidalParam {


    private static Cursor userCursor;

    public static List getCurrentTidesForFishingDataList(DBHelper dbHelper){
        List<String> tidesParamArrayList = new ArrayList<>();

        //получаем запрос на чтение из БД
        SQLiteDatabase databaseTidesReadable = dbHelper.getReadableDatabase();

        //получаем пары значений: последнее за вчера, все за сегодня, первое за завтра
        Map<String, String> waveDataMap = addTomorrowFirstTidesDataInMap(databaseTidesReadable, addTodayAllTidesDataInMap(databaseTidesReadable, addYesterdayLastTidesDataInMap(databaseTidesReadable)));


        //вычисляем текущее время в миллисекундах
        LocalDateTime localTodayTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));
        Instant instant = localTodayTime.toInstant(ZoneOffset.UTC);
        long currentTimeNumber = Instant.ofEpochSecond(0L).until(instant, ChronoUnit.SECONDS);


        String def = "-100";

        String[] startAndEnd = new String[2];
        String previousValue2 = "";
        int i = 0;
        for (Map.Entry<String, String> pair : waveDataMap.entrySet()) {
            if(i == 2){
                //определяем прилив/отлив для первого временного промежутка
                if(Float.parseFloat(pair.getValue())  > Float.parseFloat(previousValue2)){
                    startAndEnd[0] = "true";
                } else {
                    startAndEnd[0] = "false";
                }
                //определяем прилив/отлив для последнего временного промежутка
                if(startAndEnd[0].equals("true") && (waveDataMap.size() % 2 == 0)){
                    startAndEnd[1] = "false";
                } else {
                    startAndEnd[1] = "true";
                }

            }
            previousValue2 = pair.getValue();
            i++;
        }


        String previousKey = "";
        String previousValue = "";
        for (Map.Entry<String, String> pair : waveDataMap.entrySet()) {
            String key = pair.getKey();

            //проверка текущего времени
            if(key.equals("-100") && previousKey.equals("") ){
                for(int y = 0; y < 4; y++){
                    tidesParamArrayList.add(def);
                }
                tidesParamArrayList.add(startAndEnd[0]);
            } else if (key.equals("-100") && !previousKey.equals("-100")){
                for(int y = 0; y < 4; y++){
                    tidesParamArrayList.add(def);
                }
                tidesParamArrayList.add(startAndEnd[1]);
            }  else {
                Instant timeStartCycleNew = Instant.parse(key.replaceAll(" ", ""));
                long timeNumber = Instant.ofEpochSecond(0L).until(timeStartCycleNew, ChronoUnit.SECONDS);

                if(timeNumber > currentTimeNumber){
                    tidesParamArrayList.add(previousKey);
                    tidesParamArrayList.add(previousValue);
                    tidesParamArrayList.add(pair.getKey());
                    tidesParamArrayList.add(pair.getValue());

                    //высота начала и конца цикла
                    float startHeight = Float.parseFloat(tidesParamArrayList.get(1));
                    float endHeight = Float.parseFloat(tidesParamArrayList.get(3));

                    //если отлив - false, прилив - true
                    if (startHeight > endHeight){
                        tidesParamArrayList.add("false");
                    } else {
                        tidesParamArrayList.add("true");
                    }
                    break;
                }
                previousKey = pair.getKey();
                previousValue = pair.getValue();
            }
        }

        return tidesParamArrayList;
    }


    //метод добавляет в Map последнюю пару значений за вчера
    private static Map addYesterdayLastTidesDataInMap(SQLiteDatabase databaseTidesReadable){
        Map<String, String> waveDataMap = new LinkedHashMap<>();

        String def = "-100";

        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));

        if(!(localDateTime.getDayOfMonth() == 1)){

            //получаем вчерашнее число
            String yesterdayOfMonth = String.valueOf(localDateTime.getDayOfMonth()-1);

            //готовим префикс и постфикс для даты
            LocalDateTime localYesterdayTime = localDateTime.minusDays(1);
            String preYesterdayYearMonthDay =  localYesterdayTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String afterYesterdayYearMonthDay = ":00.000000Z";


            //получаем объект типа Курсор с данными на вчерашнюю дату
            userCursor = databaseTidesReadable.query("scheduleTides", null, "_id = ?", new String[]{yesterdayOfMonth}, null, null, null);

            //получить два крайних значения из вчерашних данных
            if(userCursor.moveToFirst() && userCursor.getCount() >= 1) {
                do {
                    String s;
                    String tidesTime = null;
                    String tidesHeight = null;

                    int i  = 1;

                    while(i != 11){
                        s = userCursor.getString(i);
                        if(s.equals(" ")){
                            tidesTime = userCursor.getString(i-2).replaceAll(" ", "");
                            tidesHeight = userCursor.getString(i-1).replaceAll(" ", "");
                            break;
                        } else {
                            tidesTime = userCursor.getString(i-1).replaceAll(" ", "");
                            tidesHeight = userCursor.getString(i).replaceAll(" ", "");
                        }
                        i++;
                    }
                    if(tidesTime.length() == 4){
                        tidesTime = "0" + tidesTime;
                    }
                    waveDataMap.put(preYesterdayYearMonthDay + "T" + tidesTime + afterYesterdayYearMonthDay, tidesHeight);
                } while (userCursor.moveToNext());
            }

        } else {
            waveDataMap.put(def, def);
        }

        return waveDataMap;
    }

    //метод добавляет в Map все пары значений за сегодня
    private static Map addTodayAllTidesDataInMap(SQLiteDatabase databaseTidesReadable, Map waveDataMap ){

        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));

        //готовим префикс и постфикс для даты
        String preYesterdayYearMonthDay =  localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String afterYesterdayYearMonthDay = ":00.000000Z";

        //получаем сегодняшнее число
        String dayOfMonth = String.valueOf(localDateTime.getDayOfMonth());

        //получаем объект типа Курсор с данными на текущую дату
        userCursor = databaseTidesReadable.query("scheduleTides", null, "_id = ?", new String[]{dayOfMonth}, null, null, null);


        if(userCursor.moveToFirst() && userCursor.getCount() >= 1) {
            do {
                String s;
                String tidesTime;
                String tidesHeight;

                int i  = 4;

                while(!(i >= 11)){
                    s = userCursor.getString(i);
                    if(!s.equals(" ")){
                        tidesTime = userCursor.getString(i-1).replaceAll(" ", "");
                        tidesHeight = userCursor.getString(i).replaceAll(" ", "");

                        if(tidesTime.length() == 4){
                            tidesTime = "0" + tidesTime;
                        }

                        waveDataMap.put(preYesterdayYearMonthDay + "T" + tidesTime + afterYesterdayYearMonthDay, tidesHeight);
                    } else {
                        break;
                    }
                    i+=2;
                }
            } while (userCursor.moveToNext());
        }


        return waveDataMap;
    }

    //метод добавляет в Map первую пару значений за завтра
    private static Map addTomorrowFirstTidesDataInMap(SQLiteDatabase databaseTidesReadable, Map waveDataMap){

        String def = "-100";

        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));
        LocalDate localDate = LocalDate.now(ZoneId.of("Asia/Magadan"));

        //готовим префикс и постфикс для даты
        LocalDateTime localTomorrowTime = localDateTime.plusDays(1);
        String preTomorrowYearMonthDay =  localTomorrowTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String afterTomorrowYearMonthDay = ":00.000000Z";

        //получаем сегодняшнее число
        String tomorrowDayOfMonth = String.valueOf(localTomorrowTime.getDayOfMonth());

        if(localDateTime.getDayOfMonth() != localDate.lengthOfMonth()){

            //получаем объект типа Курсор с данными на завтра
            userCursor = databaseTidesReadable.query("scheduleTides", null, "_id = ?", new String[]{tomorrowDayOfMonth}, null, null, null);

            if(userCursor.moveToFirst() && userCursor.getCount() >= 1) {
                do {
                    String tidesTime = userCursor.getString(3).replaceAll(" ", "");
                    String tidesHeight = userCursor.getString(4).replaceAll(" ", "");

                    //если перед часом не стоит 0
                    if(tidesTime.length() == 4){
                        tidesTime = "0" + tidesTime;
                    }

                    waveDataMap.put(preTomorrowYearMonthDay + "T" + tidesTime + afterTomorrowYearMonthDay, tidesHeight);

                } while (userCursor.moveToNext());
            }


        } else {
            waveDataMap.put(def, def);
        }

        return waveDataMap;
    }


    public static List  getTodayTidesForFishingDataList(DBHelper dbHelper, int dayOffsetRelativeToday){
        List<String> tidesParamArrayList = new ArrayList<>();

        //получаем запрос на чтение из БД
        SQLiteDatabase databaseTidesReadable = dbHelper.getReadableDatabase();

        //вычисляем текущее время
        LocalDateTime localTodayTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));
        //шаблон для подстановки времени
        String prefixData = localTodayTime.plusDays(dayOffsetRelativeToday).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String postfixData = ":00.000000Z";

        String dayOfMonth = String.valueOf(localTodayTime.getDayOfMonth() + dayOffsetRelativeToday);

        //получаем объект типа Курсор с данными на дату = текущая + сдвиг
        userCursor = databaseTidesReadable.query("scheduleTides", null, "_id = ?", new String[]{dayOfMonth}, null, null, null);

        String risingValue = null;
        String sunsetValue = null;

        if(userCursor.moveToFirst() && userCursor.getCount() >= 1) {
            do {
                //получаем время рассвета из базы и приводим к необходимому формату
                risingValue = userCursor.getString(2).replaceAll(" ", "");
                if (risingValue.length() == 4){
                    risingValue = prefixData + "T0" + risingValue + postfixData;
                } else {
                    risingValue = prefixData + "T" + risingValue + postfixData;
                }


                //получаем время заката из базы и приводим к необходимому формату
               sunsetValue = userCursor.getString(3).replaceAll(" ", "");
                if(sunsetValue.length() == 4){
                    sunsetValue = prefixData + "T0" + sunsetValue + postfixData;
                } else {
                    sunsetValue = prefixData + "T" + sunsetValue + postfixData;
                }

                String param;
                int i = 3;
                while(!(i >= 11)){
                    param = userCursor.getString(i);
                    if(!param.equals(" ")){
                       tidesParamArrayList.add(param.replaceAll(" ", ""));
                    } else {
                        break;
                    }
                    i++;
                }
            } while (userCursor.moveToNext());

        }

        String[] waterState = {"Малая вода", "Полная вода"};

        String firstState;
        if(Float.parseFloat(tidesParamArrayList.get(1)) < Float.parseFloat(tidesParamArrayList.get(3))){
            firstState = waterState[0];
        } else {
            firstState = waterState[1];
        }

        //Записываем первое состояние в 0 элемент списка, со сдвигом +1
        tidesParamArrayList.add(0, firstState);

        //Записываем состояние воды в список на основании предыдущих состояний
        for(int i = 3; i < tidesParamArrayList.size(); i+=2){
            if(tidesParamArrayList.get(i-3).equals(waterState[0])){
                tidesParamArrayList.add(i, waterState[1]);
            } else {
                tidesParamArrayList.add(i, waterState[0]);
            }
            i+=1;
        }

        tidesParamArrayList.add(risingValue);
        tidesParamArrayList.add(sunsetValue);

        return tidesParamArrayList;
    }

}
