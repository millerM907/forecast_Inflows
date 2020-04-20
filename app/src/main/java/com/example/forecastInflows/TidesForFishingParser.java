package com.example.forecastInflows;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TidesForFishingParser {

    private static TidesForFishingParser instance = new TidesForFishingParser();
    private TidesForFishingParser(){}

    /**
     * Данный метод осуществляет подключение по адресу: https://tides4fishing.com/as/northeast-russia/nagaeva-bay-tauiskaya-bay
     * и возвращает список объектов "generalParametra":
     * 1) объект типа Document, хранящий html размеитку страницы;
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

        String postficsTime = "-01T00:10:00.000000Z";

        LocalDateTime currentLocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));
        String prefixCurrentDate = currentLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String time = prefixCurrentDate + postficsTime;

        Instant instantConstTime = Instant.parse(time);
        long constTimeNumber = Instant.ofEpochSecond(0L).until(instantConstTime, ChronoUnit.SECONDS);

        Instant instantCurrentTime = currentLocalDateTime.toInstant(ZoneOffset.UTC);
        long currentTimeNumber = Instant.ofEpochSecond(0L).until(instantCurrentTime, ChronoUnit.SECONDS);


        //если день месяца 1, и текущее время меньше 00 ч 10 мин, то мы присваиваем 0 элементу списка значение -200 и возвращаем список
        if(currentTimeNumber < constTimeNumber){
            generalParametra.add(fatalDef);
            return generalParametra;
        }

        return generalParametra;
    }


    /**
     * Данный метод получает на вход список объектов, который содержит объект типа документ.
     * Метод парсит документ: выстаскивает данные для каждого дня месяца из таблицы приливов
     * и записывает их в список в следующей последовательности:
     * (число, время восхода, время захода и 3 или 4 пары - время прилива/отлива, его высота).
     * Метод возвращает список с именем tidesTable типа ArrayList.
     * **/
    public static List getTidesTableDataList() {

        List<String> tidesTable = new ArrayList<>();
        List generalParametra = getGeneralParametra();

        Document doc;
        if (!generalParametra.get(0).equals("-200")) {
            doc = (Document) generalParametra.get(0);
        } else {
            tidesTable.add("-200");
            return tidesTable;
        }

        LocalDate local = LocalDate.now(ZoneId.of("Asia/Magadan"));
        int maxIndexOfTable = (local.lengthOfMonth() + 4) * 2;

        for(int i = 4; i <= maxIndexOfTable; i+=2){
            String cssQuery = "#tabla_mareas > tbody > tr:nth-child(" + i + ")";
            Elements dataContent = doc.select(cssQuery);

            String waveDataContent = dataContent.text().replaceAll("(h)?(m)?", "");
            waveDataContent = waveDataContent.replaceAll("[A-Za-z]?", "");
            String[] waveDataArray = waveDataContent.split("  ");

            //записываем поэлементно в список значения массива строк (строка из таблицы)
            for(int y = 0; y < waveDataArray.length-1; y++){
                tidesTable.add(waveDataArray[y].replaceAll(" ", ""));
            }

            //если у нас 3 цикла в день, то добавляем две пустые строки
            if(waveDataArray.length == 10) {
                tidesTable.add("");
                tidesTable.add("");
            }
        }

        return tidesTable;
    }

    public static TidesForFishingParser getInstance(){
        return instance;
    }
}
