package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DayActivity extends AppCompatActivity {

    TextView tvSunset;
    TextView tvSunrise;

    Context thiscontext;

    TextView tv_weekDay;

    TextView tv_sunrise_time;
    TextView tv_sunset_time;

    TextView tv_water;
    TextView tv_time;
    TextView tv_height;
    TextView tv_tide;
    CardView cv_time;
    CardView cv_height;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        thiscontext = this;

        AppAlertDialog alertDialog = new AppAlertDialog();
        dialog = alertDialog.onCreateDialog(thiscontext, 3);
        dialog.show();

        ImageView imageView = findViewById(R.id.iv_bg);
        imageView.setBackgroundResource(R.drawable.pur_bg2);

        Bundle arguments = getIntent().getExtras();
        int keyDay = (Integer) arguments.get("keyDay");
        String nameDay = (String) arguments.get("nameDay");

        tv_weekDay = findViewById(R.id.tv_weekday);
        tv_weekDay.setText(nameDay);

        Typeface typefacePalatinoLinotype = Typeface.createFromAsset(thiscontext.getAssets(), "fonts/pala.ttf");

        tvSunrise = findViewById(R.id.tv_sunrise);
        tvSunrise.setTypeface(typefacePalatinoLinotype);

        tvSunset = findViewById(R.id.guidline);
        tvSunset.setTypeface(typefacePalatinoLinotype);

        Object[] dataTaskObjectArray = {thiscontext, keyDay};
        DataTask dataTask = new DataTask();
        dataTask.execute(dataTaskObjectArray);
    }


    private String dateTime(int offsetDay){
        String date = LocalDateTime.now(ZoneId.of("Asia/Magadan")).plusDays(offsetDay).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return date;
    }

    class DataTask extends AsyncTask<Object, Void, Object[]> {
        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {

            DBHelper dbHelper = new DBHelper(thiscontext);

            return new Object[]{ComputeTidalParam.getTodayTidesForFishingDataList(dbHelper, (Integer) dataTaskObjectArray[1]), ForecaParser.getForecaSunActivityDataList(), GismeteoParser.getGismeteoSunActivityDataList(), dataTaskObjectArray[0]};
        }


        @Override
        protected void onPostExecute(Object[] objectsArray) {
            List<String> tidesForFishingParserList = (List<String>) objectsArray[0];
            List<String> forecaParserList = (List<String>) objectsArray[1];
            List<String> gismeteoParserList = (List<String>) objectsArray[2];
            thiscontext = (Context) objectsArray[3];
            ResourseID resourseID = new ResourseID(thiscontext);
            Typeface typefacePalatinoLinotype = Typeface.createFromAsset(thiscontext.getAssets(), "fonts/pala.ttf");


            String sunriseTime = WeatherAverages.calculationMeanSunriseTime(forecaParserList.get(0), gismeteoParserList.get(0), tidesForFishingParserList.get(tidesForFishingParserList.size() - 2));
            tv_sunrise_time = findViewById(R.id.tv_sunrise_time);
            tv_sunrise_time.setText(sunriseTime);

            String sunsetTime = WeatherAverages.calculationMeanSunsetTime(forecaParserList.get(1), gismeteoParserList.get(1), tidesForFishingParserList.get(tidesForFishingParserList.size() - 1));
            tv_sunset_time = findViewById(R.id.tv_sunset_time);
            tv_sunset_time.setText(sunsetTime);

            int sizeTidesForFishingParserList = tidesForFishingParserList.size() - 2;

            String[] waterSateArray = {"Полная вода", "Малая вода", "малой", "полной", " м"};


            if (sizeTidesForFishingParserList == 12) {
                int x = 0;
                for (int i = 1; i < 5; i++) {
                    tv_water = findViewById(resourseID.tv_waterResourseID(i));
                    tv_time = findViewById(resourseID.tv_timeResourseID(i));
                    tv_height = findViewById(resourseID.tv_heightResourseID(i));
                    tv_tide = findViewById(resourseID.tv_tidetResourseID(i));

                    for (int y = x; y < x + 1; y++) {
                        tv_water.setText(tidesForFishingParserList.get(y));
                        tv_water.setTypeface(typefacePalatinoLinotype);
                        String s = tidesForFishingParserList.get(y + 1);
                        tv_time.setText(s);
                        tv_time.setTypeface(typefacePalatinoLinotype);
                        if (tidesForFishingParserList.get(y).equals(waterSateArray[0])) {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        }
                        tv_height.setText(tidesForFishingParserList.get(y + 2) + waterSateArray[4]);
                        tv_height.setTypeface(typefacePalatinoLinotype);
                    }
                    x += 3;
                }
                dialog.dismiss();
            } else if (sizeTidesForFishingParserList == 9) {

                //определяем и скрываем cv_time4
                cv_time = findViewById(R.id.cv_time4);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height4
                cv_height = findViewById(R.id.cv_height4);
                cv_height.setVisibility(View.INVISIBLE);

                int x = 0;
                for (int i = 1; i < 4; i++) {
                    tv_water = findViewById(resourseID.tv_waterResourseID(i));
                    tv_time = findViewById(resourseID.tv_timeResourseID(i));
                    tv_height = findViewById(resourseID.tv_heightResourseID(i));
                    tv_tide = findViewById(resourseID.tv_tidetResourseID(i));

                    for (int y = x; y < x + 1; y++) {
                        tv_water.setText(tidesForFishingParserList.get(y));
                        tv_water.setTypeface(typefacePalatinoLinotype);
                        tv_time.setText(tidesForFishingParserList.get(y + 1));
                        tv_time.setTypeface(typefacePalatinoLinotype);
                        if (tidesForFishingParserList.get(y).equals(waterSateArray[0])) {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        }
                        tv_height.setText(tidesForFishingParserList.get(y + 2) + waterSateArray[4]);
                        tv_height.setTypeface(typefacePalatinoLinotype);
                    }
                    x += 3;
                }
                dialog.dismiss();
            } else if (sizeTidesForFishingParserList == 6) {

                //определяем и скрываем cv_time3
                cv_time = findViewById(R.id.cv_time3);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_time4
                cv_time = findViewById(R.id.cv_time4);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height3
                cv_height = findViewById(R.id.cv_height3);
                cv_height.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height4
                cv_height = findViewById(R.id.cv_height4);
                cv_height.setVisibility(View.INVISIBLE);

                int x = 0;
                for (int i = 1; i < 3; i++) {
                    tv_water = findViewById(resourseID.tv_waterResourseID(i));
                    tv_time = findViewById(resourseID.tv_timeResourseID(i));
                    tv_height = findViewById(resourseID.tv_heightResourseID(i));
                    tv_tide = findViewById(resourseID.tv_tidetResourseID(i));

                    for (int y = x; y < x + 1; y++) {
                        tv_water.setText(tidesForFishingParserList.get(y));
                        tv_water.setTypeface(typefacePalatinoLinotype);
                        tv_time.setText(tidesForFishingParserList.get(y + 1));
                        tv_time.setTypeface(typefacePalatinoLinotype);
                        if (tidesForFishingParserList.get(y).equals(waterSateArray[0])) {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        }
                        tv_height.setText(tidesForFishingParserList.get(y + 2) + waterSateArray[4]);
                        tv_height.setTypeface(typefacePalatinoLinotype);
                    }
                    x += 3;
                }
            }
            dialog.dismiss();
        }
    }
}