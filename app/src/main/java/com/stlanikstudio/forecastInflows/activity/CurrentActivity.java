package com.stlanikstudio.forecastInflows.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.stlanikstudio.forecastInflows.AppAlertDialog;
import com.stlanikstudio.forecastInflows.ComputeTidalParam;
import com.stlanikstudio.forecastInflows.CurrentWeather;
import com.stlanikstudio.forecastInflows.db.DBHelper;
import com.stlanikstudio.forecastInflows.ForecaParser;
import com.stlanikstudio.forecastInflows.GismeteoParser;
import com.stlanikstudio.forecastInflows.NetworkManager;
import com.stlanikstudio.forecastInflows.R;
import com.stlanikstudio.forecastInflows.TimePercent;
import com.stlanikstudio.forecastInflows.WeatherDataFormatter;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.stlanikstudio.forecastInflows.R.layout.*;

public class CurrentActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    TextView tvRemainingTimeTide;
    TextView tv_waterTime_1_1;
    TextView tv_waterTime_1_2;
    TextView tv_waterHeight_4_1;
    TextView tv_waterHeight_4_2;
    TextView tv_windStrength_6_2;
    TextView tv_windSide_3_2;

    TextView tv_temperature_2_2;
    TextView tv_humidity_5_2;

    TextView tvState;

    TextView tv_remaining_time;

    ImageView iv_crab;

    Context thisContext;

    Handler handlerRemainingTimeTide;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Object[] dataTaskObjectArray;
    private int countExecuteAsyncTask;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(activity_current, container, false);

        thisContext = getActivity();

        dataTaskObjectArray = new Object[]{v};

        countExecuteAsyncTask = 0;

        //запускаем поток по отрисовке процентов и передаем в него пременную типа ResourseID
        DataTask dataTask = new DataTask();
        dataTask.execute(dataTaskObjectArray);

        //запускаем второй поток
        /*DataTaskTwo dataTaskTwo = new DataTaskTwo();
        dataTaskTwo.execute(dataTaskObjectArray);*/

        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.execute(dataTaskObjectArray);

        mSwipeRefreshLayout = v.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        handlerRemainingTimeTide = new Handler();

        return v;
    }

    @Override
    public void onRefresh() {

        if(NetworkManager.isNetworkAvailable(thisContext)){
            mSwipeRefreshLayout.setRefreshing(true);

            //запускаем поток по отрисовке процентов и передаем в него пременную типа ResourseID
            DataTask dataTask = new DataTask();
            dataTask.execute(dataTaskObjectArray);

            //запускаем второй поток
            /*DataTaskTwo dataTaskTwo = new DataTaskTwo();
              dataTaskTwo.execute(dataTaskObjectArray);
            */
            HttpRequestTask httpRequestTask = new HttpRequestTask();
            httpRequestTask.execute(dataTaskObjectArray);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    Runnable showRemainingTime = new Runnable() {

        public void run() {
            //получаем текущее время с учетом часового пояса +11
            LocalDateTime currentLocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));

            Instant instantCurrentTime = currentLocalDateTime.toInstant(ZoneOffset.UTC);

            long currentTimeNumber = Instant.ofEpochSecond(0L).until(instantCurrentTime, ChronoUnit.SECONDS);

            String time = currentLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+ "T" + tv_waterTime_1_2.getText() + ":00.000000Z";

            Instant instantEndCycleTime = Instant.parse(time);
            long endCycleTimeNumber = Instant.ofEpochSecond(0L).until(instantEndCycleTime, ChronoUnit.SECONDS);
            long differenceTime;
            if(endCycleTimeNumber - currentTimeNumber < 0){
                time = LocalDateTime.now(ZoneId.of("Asia/Magadan")).plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+ "T" + tv_waterTime_1_2.getText() + ":00.000000Z";
                instantEndCycleTime = Instant.parse(time);
                endCycleTimeNumber = Instant.ofEpochSecond(0L).until(instantEndCycleTime, ChronoUnit.SECONDS);
                differenceTime = endCycleTimeNumber - currentTimeNumber;
            } else {
                differenceTime = endCycleTimeNumber - currentTimeNumber;
            }

            String output = timeToString(differenceTime);

            tvRemainingTimeTide.setText(output);

            // планирует сам себя через 1000 мсек
            handlerRemainingTimeTide.postDelayed(showRemainingTime, 1000);
        }
    };

    private  String timeToString(long secs) {
        long hour = secs / 3600;
        long min = secs / 60 % 60;
        //long sec = secs - hour * 3600 - min * 60;

        return String.format("%01d:%02d", hour, min);
    }

    @SuppressLint("StaticFieldLeak")
    class DataTask extends AsyncTask<Object, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {
            return new Object[]{dataTaskObjectArray[0]};
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Object[] objectsArray) {

            //работаем дальше
            DBHelper dbHelper = new DBHelper(thisContext);

            List<String> currentTidesDataList = ComputeTidalParam.getCurrentTidesForFishingDataList(dbHelper);
            View view = (View) objectsArray[0];

            //вычисление процента и присвоение переменной percent
            String percent = String.valueOf(TimePercent.calculatePercentUntilEndCycle(currentTidesDataList.get(4), currentTidesDataList.get(0), currentTidesDataList.get(2)));

            //поиск textView для времени
            tv_waterTime_1_2 = view.findViewById(R.id.tv_waterTime_1_2);

            //поиск textView для высоты прилива/отлива
            tv_waterHeight_4_2 = view.findViewById(R.id.tv_waterHeight_4_2);

            //поиск textView для вывода осталось до
            tvRemainingTimeTide = view.findViewById(R.id.tv_remainingTimeTide);

            //поиск textView для вывода подписи к высоте
            tv_waterHeight_4_1 = view.findViewById(R.id.tv_waterHeight_4_1);

            //если процент вычислить не удалось он равен -100, иначе если удалось
            if(percent.equals("-100")){
                iv_crab = view.findViewById(R.id.iv_crab);
                iv_crab.setVisibility(View.VISIBLE);
                tv_waterHeight_4_1.setText("Высота воды");
                //вместо времени и высоты устанавливаем прочерки, слово прилив/отлив не выводим
                tv_waterTime_1_2.setText("-");
                tv_waterHeight_4_2.setText("-");
            } else {

                //устанавливаем время до конца цикла

                //получаем текущее время с учетом часового пояса +11
                LocalDateTime currentLocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));

                Instant instantCurrentTime = currentLocalDateTime.toInstant(ZoneOffset.UTC);
                long currentTimeNumber = Instant.ofEpochSecond(0L).until(instantCurrentTime, ChronoUnit.SECONDS);

                Instant instantEndCycleTime = Instant.parse(currentTidesDataList.get(2));
                long endCycleTimeNumber = Instant.ofEpochSecond(0L).until(instantEndCycleTime, ChronoUnit.SECONDS);

                long differenceTime = endCycleTimeNumber - currentTimeNumber;

                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern ( "HH:mm" );
                String output = timeToString(differenceTime);

                tvRemainingTimeTide = view.findViewById(R.id.tv_remainingTimeTide);

                //установка времени, оставшегося до конца цикла
                tvRemainingTimeTide.setText(output);

                tv_remaining_time = view.findViewById(R.id.tv_remaining_time);

                //выводим состояние прилив/отлив
                String[] state = new String[]{"ПРИЛИВ", "ОТЛИВ", "Полная вода", "Малая вода", "полной", "малой", "до полной воды", "до малой воды"};
                tvState = view.findViewById(R.id.tv_state);
                tv_waterTime_1_1 = view.findViewById(R.id.tv_waterTime_1_1);
                if(currentTidesDataList.get(4).equals("true")){
                    tv_remaining_time.setText(state[6]); //постоянно вылетает
                    tv_waterHeight_4_1.setText(getString(R.string.tide_now, state[4]));
                    tvState.setText(state[0]);
                    tv_waterTime_1_1.setText(state[2]);
                } else if (currentTidesDataList.get(4).equals("false")){
                    tv_remaining_time.setText(state[7]);
                    tv_waterHeight_4_1.setText(getString(R.string.tide_now, state[5]));
                    tvState.setText(state[1]);
                    tv_waterTime_1_1.setText(state[3]);
                }

                //устанавливаем время конца цикла
                tv_waterTime_1_2.setText(OffsetDateTime.parse(currentTidesDataList.get(2)).format(DateTimeFormatter.ofPattern("HH:mm")));

                //устанавливаем высоту воды
                tv_waterHeight_4_2.setText(getString(R.string.ma_water_height, currentTidesDataList.get(3)));

                //запускаем поток обновления времени, оставшегося до конца приливного цикла
                handlerRemainingTimeTide.post(showRemainingTime);
            }
        }
    }

    /**
     * HttpRequestTask this class get current weather with server
     * and set this information in view (Current Activity).
     * */
    private class HttpRequestTask extends AsyncTask<Object, Void, Object[]> {
        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {
            try {
                final String url = "https://still-dusk-90773.herokuapp.com/api/v1.0/getCurrentWeather";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                CurrentWeather currentWeather = restTemplate.getForObject(url, CurrentWeather.class);
                return new Object[]{dataTaskObjectArray[0], currentWeather};
            } catch (Exception e) {
                Log.e("CurrentActivity", e.getMessage(), e);
            }

            return new Object[]{dataTaskObjectArray[0], null};
        }

        @Override
        protected void onPostExecute(Object[] objectsArray) {

            View view = (View) objectsArray[0];

            if(objectsArray[1] != null) {
                CurrentWeather currentWeather = (CurrentWeather) objectsArray[1];

                //устанавливаем температуру
                tv_temperature_2_2 = view.findViewById(R.id.tv_temperature_2_2);
                tv_temperature_2_2.setText(getString(R.string.ma_temperature, currentWeather.getTemperature()));

                //устанавливаем влажность
                tv_humidity_5_2 = view.findViewById(R.id.tv_humidity_5_2);
                tv_humidity_5_2.setText(getString(R.string.ma_humidity, currentWeather.getHumidity()));

                //устанавливаем силу ветра
                tv_windStrength_6_2 = view.findViewById(R.id.tv_windStrength_6_2);
                tv_windStrength_6_2.setText(getString(R.string.ma_wind, currentWeather.getWind_force()));

                //направление ветра
                tv_windSide_3_2 = view.findViewById(R.id.tv_windSide_3_2);
                tv_windSide_3_2.setText(currentWeather.getWind_direction());

            } else {

                //устанавливаем температуру
                tv_temperature_2_2 = view.findViewById(R.id.tv_temperature_2_2);
                tv_temperature_2_2.setText(getString(R.string.ma_temperature, "-"));

                //устанавливаем влажность
                tv_humidity_5_2 = view.findViewById(R.id.tv_humidity_5_2);
                tv_humidity_5_2.setText(getString(R.string.ma_humidity, "-"));

                //устанавливаем силу ветра
                tv_windStrength_6_2 = view.findViewById(R.id.tv_windStrength_6_2);
                tv_windStrength_6_2.setText(getString(R.string.ma_wind, "-"));

                //направление ветра
                tv_windSide_3_2 = view.findViewById(R.id.tv_windSide_3_2);
                tv_windSide_3_2.setText("-");
            }

            //после выполнения обновления убираем кругляк обновления
            mSwipeRefreshLayout.setRefreshing(false);
            if(!mSwipeRefreshLayout.isRefreshing() && (countExecuteAsyncTask != 0)){
                Toast.makeText(thisContext, R.string.refresh_finished, Toast.LENGTH_SHORT).show();
            }
            countExecuteAsyncTask++;

            MainActivity.getIm_view_start_screen().setVisibility(View.GONE);
            MainActivity.getIm_view_2().setVisibility(View.GONE);
        }
    }

    /*@SuppressLint("StaticFieldLeak")
    class DataTaskTwo extends AsyncTask<Object, Void, Object[]>{

        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {
            return new Object[]{ForecaParser.getForecaWeatherDataList(), GismeteoParser.getGismeteoWeatherDataList(), dataTaskObjectArray[0]};
        }

        @Override
        protected void onPostExecute(Object[] objectsArray) {
            List<String> forecaParserList = (List<String>) objectsArray[0];
            List<String> gismeteoParserList = (List<String>) objectsArray[1];
            View view = (View) objectsArray[2];

            //устанавливаем температуру
            String temperature =  WeatherDataFormatter.calculationTemperatureAverages(forecaParserList.get(0), gismeteoParserList.get(0));
            tv_temperature_2_2 = view.findViewById(R.id.tv_temperature_2_2);
            tv_temperature_2_2.setText(getString(R.string.ma_temperature, temperature));

            //устанавливаем влажность
            String humidity = WeatherDataFormatter.сalculationAverageHumidity(forecaParserList.get(1), gismeteoParserList.get(3));
            tv_humidity_5_2 = view.findViewById(R.id.tv_humidity_5_2);
            tv_humidity_5_2.setText(getString(R.string.ma_humidity, humidity));

            //устанавливаем силу ветра
            String windStrength = gismeteoParserList.get(1);
            tv_windStrength_6_2 = view.findViewById(R.id.tv_windStrength_6_2);
            tv_windStrength_6_2.setText(getString(R.string.ma_wind, windStrength));

            //направление ветра
            String windDirection = gismeteoParserList.get(2);
            tv_windSide_3_2 = view.findViewById(R.id.tv_windSide_3_2);
            tv_windSide_3_2.setText(windDirection);

            //после выполнения обновления убираем кругляк обновления
            mSwipeRefreshLayout.setRefreshing(false);
            if(!mSwipeRefreshLayout.isRefreshing() && (countExecuteAsyncTask != 0)){
                Toast.makeText(thisContext, R.string.refresh_finished, Toast.LENGTH_SHORT).show();
                //AppToast.makeText(thisContext, R.string.refresh_finished, Toast.LENGTH_SHORT).show();
            }
            countExecuteAsyncTask++;

            MainActivity.getIm_view_start_screen().setVisibility(View.GONE);
            MainActivity.getIm_view_2().setVisibility(View.GONE);
        }
    }*/
}
