package com.example.myapplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TidesForFishingParser {

    private static TidesForFishingParser instance = new TidesForFishingParser();
    private TidesForFishingParser(){}

    /**
     * Данный метод осуществляет подключение по адресу: https://tides4fishing.com/as/northeast-russia/nagaeva-bay-tauiskaya-bay
     * и возвращает список объектов "generalParametra":
     * 1) объект типа Document, хранящий html размеитку страницы;
     * 2) объект типа String, хранящий CSSQuery для выборки из таблицы строки, содержащией данные за вчера;
     * 3) объект типа String, хранящий CSSQuery для выборки из таблицы строки, содержащией данные за сегодня;
     * 4) объект типа String, хранящий CSSQuery для выборки из таблицы строки, содержащией данные за завтра;
     * @return список объектов "generalParametra"
     */
    private static List getGeneralParametra(){

        List<Object> generalParametra = new ArrayList<>();

        String fatalDef = "-200";

        Document doc;
        try {
            doc = Jsoup.connect("https://tides4fishing.com/as/northeast-russia/nagaeva-bay-tauiskaya-bay")
                    .userAgent("Chrome/77.0.3865.90 Safari/12.1.1")
                    .referrer("http://www.google.com")
                    .get();

            generalParametra.add(doc);

        } catch (IOException e) {
            generalParametra.add(fatalDef);
        }

        //получаем текущее время с учетом часового пояса +11
        ZoneId leavingZone = ZoneId.of("Asia/Magadan");
        LocalDateTime localDateTime = LocalDateTime.now(leavingZone);

        //автоматическое составление запросов к таблице
        int localDayMonth = localDateTime.getDayOfMonth();

        //если день месяца 1, то мы присваиваем 0 элементу списка значение -200 и возвращаем список
        if(localDayMonth == 1){
            generalParametra.add(fatalDef);
            return generalParametra;
        }

        int yesterdayID = (localDayMonth * 2);
        int todayID = (localDayMonth * 2) + 2;
        int tomorrowID = (localDayMonth * 2) + 4;
        String yesterdayCSSQuery = "#tabla_mareas > tbody > tr:nth-child(" + yesterdayID + ")";
        generalParametra.add(yesterdayCSSQuery);
        String todayCSSQuery = "#tabla_mareas > tbody > tr:nth-child(" + todayID + ")";
        generalParametra.add(todayCSSQuery);
        String tomorrowCSSQuery = "#tabla_mareas > tbody > tr:nth-child(" + tomorrowID + ")";
        generalParametra.add(tomorrowCSSQuery);

        return generalParametra;
    }

    public static List getCurrentTidesForFishingDataList(){
        String def = "-100";

        //список для время рассвета, заката, начала, высоты начала, конца, высоты конеца, true/false - прилив/отлив
        List<String> tidesParamArrayList = new ArrayList<>();
        List generalParametra = getGeneralParametra();

        Document doc;
        if(!generalParametra.get(0).equals("-200")){
            doc = (Document) generalParametra.get(0);
        } else {
            return tidesParamArrayList;
        }

        //map для таблицы время - высота
        Map<String, String> waveDataMap = new LinkedHashMap<>();

        //получаем текущее время с учетом часового пояса +11
        ZoneId leavingZone = ZoneId.of("Asia/Magadan");

        String yesterdayCSSQuery = (String) generalParametra.get(1);
        String todayCSSQuery = (String) generalParametra.get(2);
        String tomorrowCSSQuery = (String) generalParametra.get(3);

        //вчера, конец
        try {
            Elements dataContent = doc.select(yesterdayCSSQuery);
            String waveDataContent = dataContent.text().replaceAll("(h)?(m)?", "");
            waveDataContent = waveDataContent.replaceAll("[A-Za-z]?", "");
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
            //затестить это место

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
        } catch (Exception e){
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


    public static List  getTodayTidesForFishingDataList(){
        List generalParametra = getGeneralParametra();

        String todayCSSQuery = (String) generalParametra.get(2);

        String fatalDef = "-200";

        List<String> tidesParamArrayList = new ArrayList<>();

        Document doc = (Document) generalParametra.get(0);

        try {
            Elements dataContent = doc.select(todayCSSQuery);
            String waveDataContent = dataContent.text().replaceAll("(h)?(m)?", "");
            String[] waveDataArray = waveDataContent.split(" ");
            for(int i = 6; i <= 20; i+=2){
                if(!waveDataArray[i].equals("")){
                    tidesParamArrayList.add(waveDataArray[i]);
                }
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
        } catch (NullPointerException e){
            tidesParamArrayList.add(fatalDef);
            return tidesParamArrayList;
        }

        return tidesParamArrayList;
    }

    public static TidesForFishingParser getInstance(){
        return instance;
    }
}
