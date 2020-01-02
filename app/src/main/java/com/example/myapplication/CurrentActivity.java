package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class CurrentActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    TextView tvPercentTide;
    TextView tv_waterTime_1_1;
    TextView tv_waterTime_1_2;
    TextView tv_waterHeight_4_2;
    TextView tv_windStrength_6_2;
    TextView tv_windSide_3_2;

    TextView tv_temperature_2_2;
    TextView tv_humidity_5_2;

    TextView tvState;

    Context thiscontext;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Object[] dataTaskObjectArray;
    private int countExecuteAsycnkTask;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_current, container, false);

        thiscontext = getActivity();


        dataTaskObjectArray = new Object[]{thiscontext, v};

        countExecuteAsycnkTask = 0;
        //запускаем поток по отрисовке процентов и передаем в него пременную типа ResourseID
        DataTask dataTask = new DataTask();
        dataTask.execute(dataTaskObjectArray);

        //запускаем второй поток
        DataTaskTwo dataTaskTwo = new DataTaskTwo();
        dataTaskTwo.execute(dataTaskObjectArray);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return v;
    }

    @Override
    public void onRefresh() {

        //запускаем поток по отрисовке процентов и передаем в него пременную типа ResourseID
        DataTask dataTask = new DataTask();
        dataTask.execute(dataTaskObjectArray);

        //запускаем второй поток
        DataTaskTwo dataTaskTwo = new DataTaskTwo();
        dataTaskTwo.execute(dataTaskObjectArray);
    }

    @SuppressLint("StaticFieldLeak")
    class DataTask extends AsyncTask<Object, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {
            return new Object[]{TidesForFishingParser.getCurrentTidesForFishingDataList(), dataTaskObjectArray[0], dataTaskObjectArray[1]};
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Object[] objectsArray) {
            List<String> tidesForFishingParserList = (List<String>) objectsArray[0];
            System.out.println(objectsArray[1]);
            Context thiscontext = (Context) objectsArray[1];
            ResourseID resourseID = new ResourseID(thiscontext);
            View view = (View) objectsArray[2];


            //вычисление процента и присвоение переменной percent
            String percent = String.valueOf(TimePercent.calculatePercentUntilEndCycle(tidesForFishingParserList.get(4), tidesForFishingParserList.get(0), tidesForFishingParserList.get(2)));

            //поиск textView для времени
            tv_waterTime_1_2 = view.findViewById(R.id.tv_waterTime_1_2);

            //поиск textView для высоты прилива/отлива
            tv_waterHeight_4_2 = view.findViewById(R.id.tv_waterHeight_4_2);


            //если процент вычислить не удалось он равен -100, иначе если удалось
            if(percent.equals("-100")){
                //вместо времени и высоты устанавливаем прочерки, слово прилив/отлив не выводим
                tv_waterTime_1_2.setText("-");
                tv_waterHeight_4_2.setText("-");

            } else {

                //устанавливаем вместо процента - время до конца цикла

                //получаем текущее время с учетом часового пояса +11
                LocalDateTime currentLocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));

                Instant instantCurrentTime = currentLocalDateTime.toInstant(ZoneOffset.UTC);
                long currentTimeNumber = Instant.ofEpochSecond(0L).until(instantCurrentTime, ChronoUnit.SECONDS);

                Instant instantEndCycleTime = Instant.parse(tidesForFishingParserList.get(2));
                long endCycleTimeNumber = Instant.ofEpochSecond(0L).until(instantEndCycleTime, ChronoUnit.SECONDS);

                long differenceTime = endCycleTimeNumber - currentTimeNumber;

                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern ( "HH:mm" );
                String output = timeToString(differenceTime);



                //установка процента
                tvPercentTide = view.findViewById(R.id.tv_percentTide);
                //tvPercentTide.setText(getString(R.string.ma_percent, percent));
                tvPercentTide.setText(output);

                Typeface typefaceCopperplateGothic = Typeface.createFromAsset(thiscontext.getAssets(), "fonts/COPRGTL.TTF");
                tvPercentTide.setTypeface(typefaceCopperplateGothic);

                //установка картинки
                //imageView2.setBackgroundResource(resourseID.getSearchImageResourseID(Integer.valueOf(percent)));

                //выводим состояние прилив/отлив
                String[] state = new String[]{"ПРИЛИВ", "ОТЛИВ", "Полная вода", "Малая вода"};
                tvState = view.findViewById(R.id.tv_state);
                tv_waterTime_1_1 = view.findViewById(R.id.tv_waterTime_1_1);
                if(tidesForFishingParserList.get(4).equals("true")){
                    tvState.setText(state[0]);
                    tv_waterTime_1_1.setText(state[2]);
                } else if (tidesForFishingParserList.get(4).equals("false")){
                    tvState.setText(state[1]);
                    tv_waterTime_1_1.setText(state[3]);
                }

                Typeface typefacePalatinoLinotype = Typeface.createFromAsset(thiscontext.getAssets(), "fonts/pala.ttf");
                tvState.setTypeface(typefacePalatinoLinotype);

                //устанавливаем время конца цикла
                tv_waterTime_1_2.setText(OffsetDateTime.parse(tidesForFishingParserList.get(2)).format(DateTimeFormatter.ofPattern("HH:mm")));

                //устанавливаем высоту воды
                tv_waterHeight_4_2.setText(getString(R.string.ma_water_height, tidesForFishingParserList.get(3)));
            }

        }

        private  String timeToString(long secs) {
            long hour = secs / 3600;
            long min = secs / 60 % 60;
            long sec = secs - hour * 3600 - min * 60;

            return String.format("%01d:%02d:%02d", hour, min, sec);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DataTaskTwo extends AsyncTask<Object, Void, Object[]>{

        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {
            return new Object[]{ForecaParser.getForecaDataList(), GismeteoParser.getGismeteoDataList(), dataTaskObjectArray[1]};
        }

        @Override
        protected void onPostExecute(Object[] objectsArray) {
            List<String> forecaParserList = (List<String>) objectsArray[0];
            List<String> gismeteoParserList = (List<String>) objectsArray[1];
            View view = (View) objectsArray[2];

            //устанавливаем температуру
            String temperature =  WeatherAverages.calculationTemperatureAverages(forecaParserList.get(0), gismeteoParserList.get(0));
            tv_temperature_2_2 = view.findViewById(R.id.tv_temperature_2_2);
            tv_temperature_2_2.setText(getString(R.string.ma_temperature, temperature));

            //устанавливаем влажность
            String humidity = WeatherAverages.сalculationAverageHumidity(forecaParserList.get(1), gismeteoParserList.get(3));
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
            if(mSwipeRefreshLayout.isRefreshing() == false && (countExecuteAsycnkTask != 0)){
                Toast.makeText((Context) dataTaskObjectArray[0], "Обновлено", Toast.LENGTH_SHORT).show();
            }
            countExecuteAsycnkTask++;
        }
    }
}
