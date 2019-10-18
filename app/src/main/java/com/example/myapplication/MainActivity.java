package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvPercentTide;
    TextView tv_waterTime_1_1;
    TextView tv_waterTime_1_2;
    TextView tv_waterHeight_4_2;
    TextView tv_windStrength_6_2;
    TextView tv_windSide_3_2;

    TextView tv_temperature_2_2;
    TextView tv_humidity_5_2;

    private TextView tvState;
    TextView tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ResourseID resourseID = new ResourseID(this);

        ImageView imageView = findViewById(R.id.iv_bg);
        imageView.setBackgroundResource(R.drawable.highlights);

        ImageView imageView1 = findViewById(R.id.iv_sand);
        imageView1.setBackgroundResource(R.drawable.sand1);

        //установка текущего времени города Магадан
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));
        String currentTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        tv_time = findViewById(R.id.tv_time);
        tv_time.setText(currentTime);

        //запускаем поток по отрисовке процентов и передаем в него пременную типа ResourseID
        DataTask dataTask = new DataTask();
        dataTask.execute(resourseID);

        //запускаем второй поток
        DataTaskTwo dataTaskTwo = new DataTaskTwo();
        dataTaskTwo.execute();

    }

    @SuppressLint("StaticFieldLeak")
    class DataTask extends AsyncTask<ResourseID, Void, Object[]> {

        @Override
        protected Object[] doInBackground(ResourseID... resourseID) {
            return new Object[]{TidesForFishingParser.getTidesForFishingDataList(), resourseID};
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Object[] objectsArray) {
            List<String> tidesForFishingParserList = (List<String>) objectsArray[0];
            ResourseID[] resourseID = (ResourseID[]) objectsArray[1];

            //установка процента
            String percent = String.valueOf(TimePercent.calculatePercentUntilEndCycle(tidesForFishingParserList.get(6), tidesForFishingParserList.get(2), tidesForFishingParserList.get(4)));
            tvPercentTide = findViewById(R.id.tv_percentTide);
            tvPercentTide.setText(getString(R.string.ma_percent, percent));
            Typeface typefaceCopperplateGothic = Typeface.createFromAsset(getAssets(), "fonts/COPRGTL.TTF");
            tvPercentTide.setTypeface(typefaceCopperplateGothic);

            //установка картинки
            ImageView imageView2 = findViewById(R.id.iv_wave);
            imageView2.setBackgroundResource(resourseID[0].getSearchImageResourseID(Integer.valueOf(percent)));

            //выводим состояние прилив/отлив
            String[] state = new String[]{"ЗАГРУЗКА...", "ПРИЛИВ", "ОТЛИВ", "Полная вода", "Малая вода"};
            tvState = findViewById(R.id.tv_state);
            tv_waterTime_1_1 = findViewById(R.id.tv_waterTime_1_1);
            if(tidesForFishingParserList.get(6).equals("true")){
                tvState.setText(state[1]);
                tv_waterTime_1_1.setText(state[3]);
            } else if (tidesForFishingParserList.get(6).equals("false")){
                tvState.setText(state[2]);
                tv_waterTime_1_1.setText(state[4]);
            } else {
                tvState.setText(state[0]);
            }

            //устанавливаем время конца цикла
            tv_waterTime_1_2 = findViewById(R.id.tv_waterTime_1_2);
            tv_waterTime_1_2.setText(OffsetDateTime.parse(tidesForFishingParserList.get(4)).format(DateTimeFormatter.ofPattern("HH:mm")));

            //устанавливаем высоту воды
            tv_waterHeight_4_2 = findViewById(R.id.tv_waterHeight_4_2);
            tv_waterHeight_4_2.setText(getString(R.string.ma_water_height, tidesForFishingParserList.get(5)));

            Typeface typefacePalatinoLinotype = Typeface.createFromAsset(getAssets(), "fonts/pala.ttf");
            tvState.setTypeface(typefacePalatinoLinotype);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DataTaskTwo extends AsyncTask<Object, Void, Object[]>{

        @Override
        protected Object[] doInBackground(Object... unused) {
            return new Object[]{ForecaParser.getForecaDataList(), GismeteoParser.getGismeteoDataList()};
        }

        @Override
        protected void onPostExecute(Object[] objectsArray) {
            List<String> forecaParserList = (List<String>) objectsArray[0];
            List<String> gismeteoParserList = (List<String>) objectsArray[1];

            //устанавливаем температуру
            String temperature =  WeatherAverages.calculationTemperatureAverages(forecaParserList.get(0), gismeteoParserList.get(0));
            tv_temperature_2_2 = findViewById(R.id.tv_temperature_2_2);
            tv_temperature_2_2.setText(getString(R.string.ma_temperature, temperature));

            //устанавливаем влажность
            String humidity = WeatherAverages.сalculationAverageHumidity(forecaParserList.get(1), gismeteoParserList.get(3));
            tv_humidity_5_2 = findViewById(R.id.tv_humidity_5_2);
            tv_humidity_5_2.setText(getString(R.string.ma_humidity, humidity));

            //устанавливаем силу ветра
            String windStrength = gismeteoParserList.get(1);
            tv_windStrength_6_2 = findViewById(R.id.tv_windStrength_6_2);
            tv_windStrength_6_2.setText(getString(R.string.ma_wind, windStrength));

            //направление ветра
            String windDirection = gismeteoParserList.get(2);
            tv_windSide_3_2 = findViewById(R.id.tv_windSide_3_2);
            tv_windSide_3_2.setText(windDirection);
        }

    }


}
