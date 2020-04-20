package com.example.forecastInflows;

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

    private static List getGeneralParametra(){
        List<Object> generalParametra = new ArrayList<>();
        String def = "-200";

        Document doc;
        try {
            doc = Jsoup.connect("https://www.gismeteo.ru/weather-magadan-4063/now/")
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



    public static List getGismeteoWeatherDataList(){
        List generalParametra = getGeneralParametra();
        List<String> gismeteoWeatherDataList = new ArrayList<>();
        String def = "-100";
        String def2 = "-";

        Document doc;
        if(!generalParametra.get(0).equals("-200")){
            doc = (Document) generalParametra.get(0);
        } else {
            //температура
            gismeteoWeatherDataList.add(0, def);
            //скорость ветра
            gismeteoWeatherDataList.add(1, def2);
            //направление ветра
            gismeteoWeatherDataList.add(2, def2);
            //влажность
            gismeteoWeatherDataList.add(3, def);

            return gismeteoWeatherDataList;
        }

        try {
            //Температура в градусах Цельсия
            Elements tempContent = doc.select("span.js_value");
            String temperature = tempContent.get(0).text().replaceAll("^[\\n]?[+]?[\\n]?", "");
            String tempValue = temperature.replace(",", ".");
            tempValue = tempValue.replaceAll("[−]", "-");
            System.out.println(tempValue);
            tempValue = String.valueOf((int)Float.parseFloat(tempValue));
            gismeteoWeatherDataList.add(tempValue);
        } catch (NullPointerException e) {
            gismeteoWeatherDataList.add(def);
        }

        try {
            Elements infoContent = doc.select("div.now__info.nowinfo");

            try {
                Elements windContent = infoContent.select("div.nowinfo__item.nowinfo__item_wind");

                //Скорость ветра в м/с
                String windValue = "-";
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
                    gismeteoWeatherDataList.add(windValue);
                }

                //Направление ветра
                String windDirection = "-";
                try {
                    Elements windDirectionContent = windContent.select("div.nowinfo__measure.nowinfo__measure_wind");
                    windDirection = windDirectionContent.get(0).text().replaceAll("(м/с)?", "").replaceAll(" ", "");
                    windDirection = "СЗ";
                    String[] directionShortName = {"С", "B", "З", "Ю"};
                    switch (windDirection){
                        case "Северный":
                            windDirection = directionShortName[0];
                            break;
                        case "Восточный":
                            windDirection = directionShortName[1];
                            break;
                        case "Западаный":
                            windDirection = directionShortName[2];
                            break;
                        case "Южный":
                            windDirection = directionShortName[3];
                            break;
                        default:
                            break;
                    }
                } catch (NullPointerException e) {
                } finally {
                    gismeteoWeatherDataList.add(windDirection);
                }


            } catch (NullPointerException e) {
                gismeteoWeatherDataList.add(1, def2);
                gismeteoWeatherDataList.add(2, def2);
            }

            //Влажность в %
            String humidityValue = def;
            try {
                Elements humidityContent = infoContent.select("div.nowinfo__item.nowinfo__item_humidity");
                Elements humidityValueContent = humidityContent.select("div.nowinfo__value");
                humidityValue = humidityValueContent.get(0).text().replaceAll("[\\n]", "");
            } catch (NullPointerException e) {
            } finally {
                gismeteoWeatherDataList.add(humidityValue);
            }
        } catch (NullPointerException e) {
            gismeteoWeatherDataList.add(1, def2);
            gismeteoWeatherDataList.add(2, def2);
            gismeteoWeatherDataList.add(3, def);
        }

        return gismeteoWeatherDataList;
    }

    public static List getGismeteoSunActivityDataList(){

        List generalParametra = getGeneralParametra();
        List<String> gismeteoSunActivityDataList = new ArrayList<>();

        String def = "-100";

        Document doc;
        if(!generalParametra.get(0).equals("-200")){
            doc = (Document) generalParametra.get(0);
        } else {
            for(int i = 0; i < 2; i++){
                gismeteoSunActivityDataList.add(i, def);
            }
            return gismeteoSunActivityDataList;
        }

        //Парсинг времени рассвета и заката
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

            gismeteoSunActivityDataList.add(rising);
            gismeteoSunActivityDataList.add(sunset);

        } catch (NullPointerException e) {
            gismeteoSunActivityDataList.add(def);
            gismeteoSunActivityDataList.add(def);
        }

        return gismeteoSunActivityDataList;
    }

    public static GismeteoParser getInstance(){
        return instance;
    }
}
