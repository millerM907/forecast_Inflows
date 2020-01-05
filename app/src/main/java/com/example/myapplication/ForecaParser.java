package com.example.myapplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ForecaParser {
    private static ForecaParser instance = new ForecaParser();
    private ForecaParser(){}

    private static List getGeneralParametra(){
        List<Object> generalParametra = new ArrayList<>();
        String def = "-200";

        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.foreca.com/Russia/Magadan")
                    .userAgent("Chrome/77.0.3865.90 Safari/12.1.1")
                    .referrer("http://www.google.com")
                    .get();

        } catch (IOException e) {
            generalParametra.add(def);
            return generalParametra;
        }

        generalParametra.add(doc);
        return generalParametra;
    }

    //метод возвращает влажность и температуру
    public static List getForecaWeatherDataList(){
        List generalParametra = getGeneralParametra();
        List<String> forecaWeatherDataList = new ArrayList<>();

        String def = "-100";

        Document doc;
        if(!generalParametra.get(0).equals("-200")){
            doc = (Document) generalParametra.get(0);
        } else {
            for(int i = 0; i < 4; i++){
                forecaWeatherDataList.add(i, def);
            }
            return forecaWeatherDataList;
        }

        try {
            //достаем температуру
            Elements leftContent = doc.select("div.left strong");
            String temperature = leftContent.get(0).text().replaceAll("^[\\n]?[+]?[\\n]?", "");
            forecaWeatherDataList.add(0, temperature);
        } catch (NullPointerException e) {
            forecaWeatherDataList.add(0, def);
        }

        try {
            //достаем влажность
            Elements rightContent = doc.select("div.right strong");
            String humidity = rightContent.get(3).text().replaceAll("[\\n]?[%]?[\\n]?", "");
            //убрать все, что после запятой
            humidity = String.valueOf((int)Float.parseFloat(humidity));
            forecaWeatherDataList.add(humidity);

        } catch (NullPointerException e) {
            forecaWeatherDataList.add(1, def);
        }

        return forecaWeatherDataList;
    }

    //метод возвращает время рассвета и заката
    public static List getForecaSunActivityDataList(){
        List generalParametra = getGeneralParametra();
        List<String> forecaSunActivityDataList = new ArrayList<>();

        String def = "-100";

        Document doc;
        if(!generalParametra.get(0).equals("-200")){
            doc = (Document) generalParametra.get(0);
        } else {
            for(int i = 0; i < 2; i++){
                forecaSunActivityDataList.add(i, def);
            }
            return forecaSunActivityDataList;
        }

        try{
            Elements rightContent = doc.select("div.right strong");

            //вычисляем текущее время
            LocalDateTime localTodayTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));

            //шаблон для подстановки времени
            String currentYearMonthDay = localTodayTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            //время рассвета
            String rising = rightContent.get(5).text();
            rising = currentYearMonthDay + "T" + rising + ":00.000000Z";
            forecaSunActivityDataList.add(rising);

            //время заката
            String sunset = rightContent.get(6).text();
            sunset = currentYearMonthDay + "T" + sunset + ":00.000000Z";
            forecaSunActivityDataList.add(sunset);

        } catch (NullPointerException ignored){
            for(int i = 0; i < 2; i++){
                forecaSunActivityDataList.add(i, def);
            }
        }
        return forecaSunActivityDataList;
    }

    public static ForecaParser getInstance(){
        return  instance;
    }
}
