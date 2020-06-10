package com.stlanikstudio.forecastInflows;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherDataFormatter {

    public static String calculationTemperatureAverages(String firstTemp, String secondTemp) {
        String averageTemp = "-";
        if (!(firstTemp.equals("-100") || secondTemp.equals("-100"))) {
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
        if (!(firstHumidity.equals("-100") || secondHumidity.equals("-100"))) {
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

    public static String parseSunriseTime(String firstTime) {
        String dawnTime = "-";

        if(!firstTime.equals("-100")){
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(firstTime);
            dawnTime = offsetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            return dawnTime;
        }

        return dawnTime;
    }


    public static String parseSunsetTime(String firstTime) {
        String sunsetTime = "-";

        if(!firstTime.equals("-100")){
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(firstTime);
            sunsetTime = offsetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            return sunsetTime;
        }

        return sunsetTime;
    }
}
