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

    public static List getForecaDataList(){
        List<String> forecaDataList = new ArrayList<>();
        String def = "-100";

        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.foreca.com/Russia/Magadan")
                    .userAgent("Chrome/77.0.3865.90 Safari/12.1.1")
                    .referrer("http://www.google.com")
                    .get();

        } catch (IOException e) {
            for (int i = 0; i < 4; i++) {
                forecaDataList.add(i, def);
            }
            return forecaDataList;
        }

        try {
            //достаем температуру
            Elements leftContent = doc.select("div.left strong");
            String temperature = leftContent.get(0).text().replaceAll("^[\\n]?[+]?[\\n]?", "");
            forecaDataList.add(0, temperature);
        } catch (NullPointerException e) {
            forecaDataList.add(0, def);
        }

        try {
            //достаем влажность
            Elements rightContent = doc.select("div.right strong");
            String humidity = rightContent.get(3).text().replaceAll("[\\n]?[%]?[\\n]?", "");
            //убрать все, что после запятой
            humidity = String.valueOf((int)Float.parseFloat(humidity));
            forecaDataList.add(humidity);

            //вычисляем текущее время
            LocalDateTime localTodayTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));

            //шаблон для подстановки времени
            String currentYearMonthDay = localTodayTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            //время рассвета
            String rising = rightContent.get(5).text();
            rising = currentYearMonthDay + "T" + rising + ":00.000000Z";
            forecaDataList.add(rising);

            //время заката
            String sunset = rightContent.get(6).text();
            sunset = currentYearMonthDay + "T" + sunset + ":00.000000Z";
            forecaDataList.add(sunset);

        } catch (NullPointerException e) {
            for(int i = 1; i < 4; i++){
                forecaDataList.add(i, def);
            }
        }

        return forecaDataList;
    }

    public static ForecaParser getInstance(){
        return  instance;
    }
}
