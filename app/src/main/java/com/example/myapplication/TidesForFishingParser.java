package com.example.myapplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TidesForFishingParser {
    private static TidesForFishingParser instance = new TidesForFishingParser();
    private TidesForFishingParser(){}

    public static List getTidesForFishingDataList(){
        String def = "-100";
        //список для время рассвета, заката, начала, высоты начала, конца, высоты конеца, true/false - прилив/отлив
        List<String> tidesParamArrayList = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect("https://tides4fishing.com/as/northeast-russia/nagaeva-bay-tauiskaya-bay")
                    .userAgent("Chrome/77.0.3865.90 Safari/12.1.1")
                    .referrer("http://www.google.com")
                    .get();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            for(int i = 0; i < 7; i++){
                tidesParamArrayList.add(def);
            }
            return tidesParamArrayList;
        }

        //map для таблицы время - высота
        Map<String, String> waveDataMap = new LinkedHashMap<>();

        //получаем текущее время с учетом часового пояса +11
        ZoneId leavingZone = ZoneId.of("Asia/Magadan");
        LocalDateTime localDateTime = LocalDateTime.now(leavingZone);

        //автоматическое составление запросов к таблице
        int localDayMonth = localDateTime.getDayOfMonth();
        int yesterdayID = (localDayMonth * 2);
        int todayID = (localDayMonth * 2) + 2;
        int tomorrowID = (localDayMonth * 2) + 4;
        String yesterdayCSSQuery = "#tabla_mareas > tbody > tr:nth-child(" + yesterdayID + ")";
        String todayCSSQuery = "#tabla_mareas > tbody > tr:nth-child(" + todayID + ")";
        String tomorrowCSSQuery = "#tabla_mareas > tbody > tr:nth-child(" + tomorrowID + ")";

        //вчера, конец
        try {
            Elements dataContent = doc.select(yesterdayCSSQuery);
            String waveDataContent = dataContent.text().replaceAll("(h)?(m)?", "");
            String[] waveDataArray = waveDataContent.split(" ");

            //получили вчера
            LocalDateTime localYesterdayTime = LocalDateTime.now(leavingZone);
            localYesterdayTime = localYesterdayTime.minusDays(1);

            //вытаскиваем шаблон для строки
            String preYesterdayYearMonthDay =  localYesterdayTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String afterYesterdayYearMonthDay = ":00.000000Z";

            for(int i = waveDataArray.length-3; i > 0; i--) {
                if (!waveDataArray[i].equals("null") && !waveDataArray[i].equals("")) {
                    waveDataMap.put(preYesterdayYearMonthDay + "T" + waveDataArray[i - 2] + afterYesterdayYearMonthDay, waveDataArray[i]);
                    break;
                }
            }
        } catch (NullPointerException e){
            waveDataMap.put(def, def);
        }


        //сегодня
        try{
            Elements dataContent = doc.select(todayCSSQuery);
            String waveDataContent = dataContent.text().replaceAll("(h)?(m)?", "");
            String[] waveDataArray = waveDataContent.split(" ");

            //вычисляем текущее время
            LocalDateTime localTodayTime = LocalDateTime.now(leavingZone);

            //шаблон для подстановки времени
            String currentYearMonthDay = localTodayTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            //подставляем времени рассвета текущую дату
            String risingValue = waveDataArray[2];
            if (risingValue.length() == 4){
                risingValue = currentYearMonthDay + "T0" + risingValue + ":00.000000Z";
            } else {
                risingValue = currentYearMonthDay + "T" + risingValue + ":00.000000Z";
            }

            //подставляем времени заката текущаю дату
            String sunsetValue = waveDataArray[4];
            if(sunsetValue.length() == 4){
                sunsetValue = currentYearMonthDay + "T0" + sunsetValue + ":00.000000Z";
            } else {
                sunsetValue = currentYearMonthDay + "T" + sunsetValue + ":00.000000Z";
            }

            tidesParamArrayList.add(risingValue);
            tidesParamArrayList.add(sunsetValue);

            //Добавляем время сразу в формате, если значение не null
            for(int i = 6; i <= waveDataArray.length-6; i+=4){ //выходит за границу массива
                if(!waveDataArray[i].equals("null")){
                    if(waveDataArray[i].length() == 4){
                        waveDataArray[i] = "0" + waveDataArray[i];
                    }
                    waveDataArray[i] = currentYearMonthDay + "T" + waveDataArray[i] + ":00.000000Z";
                    waveDataMap.put(waveDataArray[i], waveDataArray[i+2]);
                }
            }

            //почистить от null
            for (Map.Entry<String, String> pair: waveDataMap.entrySet()) {
                if(pair.getKey().equals("null")){
                    waveDataMap.remove(pair.getKey(), pair.getValue());
                }
            }
        } catch(NullPointerException e) {
            //исключение, что недоступно
        }

        //получили первое значение для завтра и добавили в Map
        try{
            Elements tomorrowDataContent = doc.select(tomorrowCSSQuery);
            String waveTomorrowDataContent = tomorrowDataContent.text().replaceAll("(h)?(m)?", "");
            String[] waveTomorrowDataArray = waveTomorrowDataContent.split(" ");

            LocalDateTime localTomorrowTime = LocalDateTime.now(leavingZone);
            localTomorrowTime = localTomorrowTime.plusDays(1);
            String preTomorrowYearMonthDay =  localTomorrowTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String afterYesterdayYearMonthDay = ":00.000000Z";

            if(waveTomorrowDataArray[6].length() == 4){
                waveTomorrowDataArray[6] = "0" + waveTomorrowDataArray[6];
            }

            waveDataMap.put(preTomorrowYearMonthDay +  "T" + waveTomorrowDataArray[6] + afterYesterdayYearMonthDay, waveTomorrowDataArray[8]);
        } catch (NullPointerException e){
            waveDataMap.put(def, def);
        }

        //вычисляем текущее время в миллисекундах
        LocalDateTime localTodayTime = LocalDateTime.now(leavingZone);
        Instant instant = localTodayTime.toInstant(ZoneOffset.UTC);
        long currentTimeNumber = Instant.ofEpochSecond(0L).until(instant, ChronoUnit.SECONDS);


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
                Instant timeStartCycleNew = Instant.parse(key);
                long timeNumber = Instant.ofEpochSecond(0L).until(timeStartCycleNew, ChronoUnit.SECONDS);

                if(timeNumber > currentTimeNumber){
                    tidesParamArrayList.add(previousKey);
                    tidesParamArrayList.add(previousValue);
                    tidesParamArrayList.add(pair.getKey());
                    tidesParamArrayList.add(pair.getValue());

                    //высота начала и конца цикла
                    float startHeight = Float.parseFloat(tidesParamArrayList.get(3));
                    float endHeight = Float.parseFloat(tidesParamArrayList.get(5));

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

        return  tidesParamArrayList;
    }

    public static TidesForFishingParser getInstance(){
        return instance;
    }
}
