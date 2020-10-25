package com.stlanikstudio.forecastInflows.models;

public class TidesDay {


    private Integer id;
    private String sunriseTime;
    private String sunsetTime;

    private String tidesTimeFirst;
    private String tidesHeightFirst;

    private String tidesTimeSecond;
    private String getTidesHeightSecond;

    private String tidesTimeThird;
    private String getTidesHeightThird;

    private String tidesTimeFourth;
    private String getTidesHeightFourth;

    public TidesDay() {}

    public TidesDay(Integer id, String sunriseTime, String sunsetTime, String tidesTimeFirst, String tidesHeightFirst, String tidesTimeSecond, String getTidesHeightSecond, String tidesTimeThird, String getTidesHeightThird, String tidesTimeFourth, String getTidesHeightFourth) {
        this.id = id;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.tidesTimeFirst = tidesTimeFirst;
        this.tidesHeightFirst = tidesHeightFirst;
        this.tidesTimeSecond = tidesTimeSecond;
        this.getTidesHeightSecond = getTidesHeightSecond;
        this.tidesTimeThird = tidesTimeThird;
        this.getTidesHeightThird = getTidesHeightThird;
        this.tidesTimeFourth = tidesTimeFourth;
        this.getTidesHeightFourth = getTidesHeightFourth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public String getTidesTimeFirst() {
        return tidesTimeFirst;
    }

    public void setTidesTimeFirst(String tidesTimeFirst) {
        this.tidesTimeFirst = tidesTimeFirst;
    }

    public String getTidesHeightFirst() {
        return tidesHeightFirst;
    }

    public void setTidesHeightFirst(String tidesHeightFirst) {
        this.tidesHeightFirst = tidesHeightFirst;
    }

    public String getTidesTimeSecond() {
        return tidesTimeSecond;
    }

    public void setTidesTimeSecond(String tidesTimeSecond) {
        this.tidesTimeSecond = tidesTimeSecond;
    }

    public String getGetTidesHeightSecond() {
        return getTidesHeightSecond;
    }

    public void setGetTidesHeightSecond(String getTidesHeightSecond) {
        this.getTidesHeightSecond = getTidesHeightSecond;
    }

    public String getTidesTimeThird() {
        return tidesTimeThird;
    }

    public void setTidesTimeThird(String tidesTimeThird) {
        this.tidesTimeThird = tidesTimeThird;
    }

    public String getGetTidesHeightThird() {
        return getTidesHeightThird;
    }

    public void setGetTidesHeightThird(String getTidesHeightThird) {
        this.getTidesHeightThird = getTidesHeightThird;
    }

    public String getTidesTimeFourth() {
        return tidesTimeFourth;
    }

    public void setTidesTimeFourth(String tidesTimeFourth) {
        this.tidesTimeFourth = tidesTimeFourth;
    }

    public String getGetTidesHeightFourth() {
        return getTidesHeightFourth;
    }

    public void setGetTidesHeightFourth(String getTidesHeightFourth) {
        this.getTidesHeightFourth = getTidesHeightFourth;
    }
}

