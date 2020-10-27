package com.stlanikstudio.forecastInflows.network;

import android.util.Log;

import com.stlanikstudio.forecastInflows.models.CurrentWeather;
import com.stlanikstudio.forecastInflows.models.TidesDay;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class Protocol {

    private static final String TIDES_SERVER_API_URL = "https://still-dusk-90773.herokuapp.com/api/v1.0/";
    private static final String GET_CURRENT_WEATHER = "getCurrentWeather";
    private static final String GET_TIDES_TABLE = "getTidesTable";

    private static RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }


    public static CurrentWeather getCurrentWeather() {
        try {
            return createRestTemplate().getForObject(TIDES_SERVER_API_URL + GET_CURRENT_WEATHER, CurrentWeather.class);
        } catch (Exception e) {
            Log.e("Protocol", e.getMessage(), e);
            return null;
        }

    }

    public static List<TidesDay> getTidesTable() {
        try {
            return Arrays.asList(createRestTemplate().getForObject(TIDES_SERVER_API_URL + GET_TIDES_TABLE, TidesDay[].class));
        } catch (Exception e) {
            Log.e("Protocol", e.getMessage(), e);
            return null;
        }
    }

}
