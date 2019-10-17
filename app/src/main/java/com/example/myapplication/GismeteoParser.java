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

public class GismeteoParser {
    private static GismeteoParser instance = new GismeteoParser();
    private GismeteoParser(){}

    public static List getGismeteoDataList(){
        List<String> gismeteoDataList = new ArrayList<>();
        String def = "-100";

        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.gismeteo.ru/weather-magadan-4063/now/")
                    .userAgent("Chrome/77.0.3865.90 Safari/12.1.1")
                    .referrer("http://www.google.com")
                    .get();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            for (int i = 0; i < 6; i++){
                gismeteoDataList.add(i, def);
            }
            return gismeteoDataList;
        }

        try {
            //Температура в градусах Цельсия
            Elements tempContent = doc.select("span.js_value");
            String temperature = tempContent.get(0).text().replaceAll("^[\\n]?[+]?[\\n]?", "");
            String tempValue = temperature.replace(",", ".");
            tempValue = tempValue.replaceAll("^[−]?", "-");
            tempValue = String.valueOf((int)Float.parseFloat(tempValue));
            gismeteoDataList.add(tempValue);
        } catch (NullPointerException e) {
            gismeteoDataList.add(def);
        }

        try {
            Elements infoContent = doc.select("div.now__info.nowinfo");

            try {
                Elements windContent = infoContent.select("div.nowinfo__item.nowinfo__item_wind");

                //Скорость ветра в м/с
                String windValue = def;
                try {
                    Elements windSpeedContent = windContent.select("div.nowinfo__value");
                    windValue = windSpeedContent.get(0).text().replaceAll("[\\n]?", "");
                } catch (NullPointerException e) {
                } catch (java.lang.NumberFormatException e) {
                    String[] windDataArray = windValue.split("-");
                    short firstWindValueSh = Short.parseShort(windDataArray[0]);
                    short secondWindValueSh = Short.parseShort(windDataArray[1]);
                    int averageWindValueI = (firstWindValueSh + secondWindValueSh) / 2;
                    windValue = String.valueOf(averageWindValueI);
                } finally {
                    gismeteoDataList.add(windValue);
                }

                //Направление ветра
                String windDirection = def;
                try {
                    Elements windDirectionContent = windContent.select("div.nowinfo__measure.nowinfo__measure_wind");
                    windDirection = windDirectionContent.get(0).text().replaceAll("(м/с)?", "").replaceAll(" ", "");

                } catch (NullPointerException e) {
                } finally {
                    gismeteoDataList.add(windDirection);
                }


            } catch (NullPointerException e) {
                gismeteoDataList.add(1, def);
                gismeteoDataList.add(2, def);
            }

            //Влажность в %
            String humidityValue = def;
            try {
                Elements humidityContent = infoContent.select("div.nowinfo__item.nowinfo__item_humidity");
                Elements humidityValueContent = humidityContent.select("div.nowinfo__value");
                humidityValue = humidityValueContent.get(0).text().replaceAll("[\\n]", "");
            } catch (NullPointerException e) {
            } finally {
                gismeteoDataList.add(humidityValue);
            }
        } catch (NullPointerException e) {
            gismeteoDataList.add(1, def);
            gismeteoDataList.add(2, def);
            gismeteoDataList.add(3, def);
        }

        //Время рассвета/заката
        try {
            Elements sunContent = doc.select("div.nowastro__time");
            String rising = sunContent.get(0).text().replaceAll("[\\n]?", "");
            String sunset = sunContent.get(1).text().replaceAll("[\\n]?", "");
            if(rising.length() == 4){
                rising = "0" + rising;
            }
            if(sunset.length() == 4){
                sunset = "0" + sunset;
                String exchange = sunset;
                sunset = rising;
                rising = exchange;
            }
            //вычисляем текущее время
            LocalDateTime localTodayTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));

            //шаблон для подстановки времени
            String currentYearMonthDay = localTodayTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            rising = currentYearMonthDay + "T" + rising + ":00.000000Z";
            sunset = currentYearMonthDay + "T" + sunset + ":00.000000Z";

            gismeteoDataList.add(rising);
            gismeteoDataList.add(sunset);

        } catch (NullPointerException e) {
            gismeteoDataList.add(def);
            gismeteoDataList.add(def);
        }

        return gismeteoDataList;
    }

    public static GismeteoParser getInstance(){
        return instance;
    }
}
