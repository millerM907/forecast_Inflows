package com.example.myapplication;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WeatherAverages {

    public static String calculationTemperatureAverages(String firstTemp, String secondTemp) {
        String averageTemp = "-";
        if (!(firstTemp.equals("-100") && secondTemp.equals("-100"))) {
            int firstValueTemp = Integer.parseInt(firstTemp);
            int secondValueTemp = Integer.parseInt(secondTemp);
            int averageValueTemp = (firstValueTemp + secondValueTemp) / 2;
            if (averageValueTemp > 0) {
                averageTemp = "+" + averageValueTemp;
            } else {
                averageTemp = String.valueOf(averageValueTemp);
            }
        } else if (firstTemp.equals("-100") && !secondTemp.equals("-100")) {
            if (Integer.parseInt(secondTemp) > 0) {
                secondTemp = "+" + secondTemp;
            }
            return secondTemp;
        } else if (!firstTemp.equals("-100") && secondTemp.equals("-100")) {
            if (Integer.parseInt(firstTemp) > 0) {
                firstTemp = "+" + firstTemp;
            }
            return firstTemp;
        }

        return averageTemp;
    }

    public static String сalculationAverageHumidity(String firstHumidity, String secondHumidity) {
        String averageHumidity = "-";
        if (!(firstHumidity.equals("-100") && secondHumidity.equals("-100"))) {
            int firstValueHumidity = Integer.parseInt(firstHumidity);
            int secondValueHumidity = Integer.parseInt(secondHumidity);
            averageHumidity = String.valueOf((firstValueHumidity + secondValueHumidity) / 2);

        } else if (firstHumidity.equals("-100") && !secondHumidity.equals("-100")) {
            return secondHumidity;
        } else if (!firstHumidity.equals("-100") && secondHumidity.equals("-100")) {
            return firstHumidity;
        }

        return averageHumidity;
    }

    public static String calculationMeanSunriseTime(String firstTime, String secondTime, String thirdTime) {
        String meanDawnTime = "-";
        if (!(firstTime.equals("-100") && secondTime.equals("-100"))) {

            OffsetDateTime firstDateRising = OffsetDateTime.parse(firstTime);
            long firstTimeValue = firstDateRising.toEpochSecond();

            OffsetDateTime secondDateRising = OffsetDateTime.parse(secondTime);
            long secondTimeValue = secondDateRising.toEpochSecond();

            long meanDawnTimeNumber = (firstTimeValue + secondTimeValue) / 2;
            LocalDateTime localDateTime = Instant.ofEpochSecond(meanDawnTimeNumber).atZone(ZoneId.of("UTC")).toLocalDateTime();
            meanDawnTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        } else if (firstTime.equals("-100") && !secondTime.equals("-100")) {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(secondTime);
            meanDawnTime = offsetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            return meanDawnTime;

        } else if (!firstTime.equals("-100") && secondTime.equals("-100")) {

            OffsetDateTime offsetDateTime = OffsetDateTime.parse(firstTime);
            meanDawnTime = offsetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            return meanDawnTime;

        } else if (!thirdTime.equals("-100")) {

            OffsetDateTime offsetDateTime = OffsetDateTime.parse(thirdTime);
            meanDawnTime = offsetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            return meanDawnTime;
        }

        return meanDawnTime;
    }


    public static String calculationMeanSunsetTime(String firstTime, String secondTime, String thirdTime) {
        String meanSunsetTime = "-";

        if (!(firstTime.equals("-100") && secondTime.equals("-100"))) {

            OffsetDateTime firstDateSunset = OffsetDateTime.parse(firstTime);
            long firstSunsetValue = firstDateSunset.toEpochSecond();

            OffsetDateTime secondDateSunset = OffsetDateTime.parse(secondTime);
            long secondSunsetValue = secondDateSunset.toEpochSecond();

            long meanSunsetTimeNumber = (firstSunsetValue + secondSunsetValue) / 2;
            LocalDateTime localDateTime = Instant.ofEpochSecond(meanSunsetTimeNumber).atZone(ZoneId.of("UTC")).toLocalDateTime();
            meanSunsetTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else if (firstTime.equals("100") && !secondTime.equals("-100")) {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(secondTime);
            meanSunsetTime = offsetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            return meanSunsetTime;
        } else if (!firstTime.equals("-100") && secondTime.equals("-100")) {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(firstTime);
            meanSunsetTime = offsetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            return meanSunsetTime;
        } else if (!thirdTime.equals("-100")) {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(thirdTime);
            meanSunsetTime = offsetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            return meanSunsetTime;
        }

        return meanSunsetTime;
    }
}
