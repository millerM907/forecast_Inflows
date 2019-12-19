package com.example.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TodayActivity extends Fragment {

    TextView tvSunset;
    TextView tvSunrise;
    //ImageView ivBg;
    ImageView ivShell;

    Context thiscontext;

    TextView tv_sunrise_time;
    TextView tv_sunset_time;

    TextView tv_water;
    TextView tv_time;
    TextView tv_height;
    TextView tv_tide;
    CardView cv_time;
    CardView cv_height;

    ImageButton imageButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_today, container, false);

        thiscontext = getActivity();

        Typeface typefacePalatinoLinotype = Typeface.createFromAsset(thiscontext.getAssets(), "fonts/pala.ttf");

        tvSunrise = v.findViewById(R.id.tv_sunrise);
        tvSunrise.setTypeface(typefacePalatinoLinotype);

        tvSunset = v.findViewById(R.id.guidline);
        tvSunset.setTypeface(typefacePalatinoLinotype);

        Object[] dataTaskObjectArray = {v, thiscontext};
        DataTask dataTask = new DataTask();
        dataTask.execute(dataTaskObjectArray);



        return v;
    }

    class DataTask extends AsyncTask<Object, Void, Object[]> {
        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {
           return new Object[]{TidesForFishingParser.getTodayTidesForFishingDataList(0), ForecaParser.getForecaDataList(), GismeteoParser.getGismeteoDataList(), dataTaskObjectArray[0], dataTaskObjectArray[1]};
        }


        @Override
        protected void onPostExecute(Object[] objectsArray) {
            List<String> tidesForFishingParserList = (List<String>) objectsArray[0];
            List<String> forecaParserList = (List<String>) objectsArray[1];
            List<String> gismeteoParserList = (List<String>) objectsArray[2];
            View view = (View) objectsArray[3];
            thiscontext = (Context) objectsArray[4];
            ResourseID resourseID = new ResourseID(thiscontext);
            Typeface typefacePalatinoLinotype = Typeface.createFromAsset(thiscontext.getAssets(), "fonts/pala.ttf");


            String sunriseTime = WeatherAverages.calculationMeanSunriseTime(forecaParserList.get(2), gismeteoParserList.get(4), tidesForFishingParserList.get(tidesForFishingParserList.size()-2));
            tv_sunrise_time = view.findViewById(R.id.tv_sunrise_time);
            tv_sunrise_time.setText(sunriseTime);

            String sunsetTime = WeatherAverages.calculationMeanSunsetTime(forecaParserList.get(3), gismeteoParserList.get(5), tidesForFishingParserList.get(tidesForFishingParserList.size()-1));
            tv_sunset_time = view.findViewById(R.id.tv_sunset_time);
            tv_sunset_time.setText(sunsetTime);

            int sizeTidesForFishingParserList = tidesForFishingParserList.size() - 2;

            String[] waterSateArray = {"Полная вода", "Малая вода", "отлива","прилива", " м"};


            if(sizeTidesForFishingParserList == 12){
                int x = 0;
                for(int i = 1; i < 5; i++){
                    tv_water = view.findViewById(resourseID.tv_waterResourseID(i));
                    tv_time = view.findViewById(resourseID.tv_timeResourseID(i));
                    tv_height = view.findViewById(resourseID.tv_heightResourseID(i));
                    tv_tide = view.findViewById(resourseID.tv_tidetResourseID(i));

                    for(int y = x; y < x+1 ; y++){
                        tv_water.setText(tidesForFishingParserList.get(y));
                        tv_water.setTypeface(typefacePalatinoLinotype);
                        String s = tidesForFishingParserList.get(y+1);
                        tv_time.setText(s);
                        tv_time.setTypeface(typefacePalatinoLinotype);
                        if(tidesForFishingParserList.get(y).equals(waterSateArray[0])){
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        }
                        tv_height.setText(tidesForFishingParserList.get(y+2) + waterSateArray[4]);
                        tv_height.setTypeface(typefacePalatinoLinotype);
                    }
                    x+=3;
                }

            } else if (sizeTidesForFishingParserList == 9){

                //определяем и скрываем cv_time4
                cv_time = view.findViewById(R.id.cv_time4);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height4
                cv_height = view.findViewById(R.id.cv_height4);
                cv_height.setVisibility(View.INVISIBLE);

                int x = 0;
                for(int i = 1; i < 4; i++){
                    tv_water = view.findViewById(resourseID.tv_waterResourseID(i));
                    tv_time = view.findViewById(resourseID.tv_timeResourseID(i));
                    tv_height = view.findViewById(resourseID.tv_heightResourseID(i));
                    tv_tide = view.findViewById(resourseID.tv_tidetResourseID(i));

                    for(int y = x; y < x+1 ; y++){
                        tv_water.setText(tidesForFishingParserList.get(y));
                        tv_water.setTypeface(typefacePalatinoLinotype);
                        tv_time.setText(tidesForFishingParserList.get(y+1));
                        tv_time.setTypeface(typefacePalatinoLinotype);
                        if(tidesForFishingParserList.get(y).equals(waterSateArray[0])){
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        }
                        tv_height.setText(tidesForFishingParserList.get(y+2) + waterSateArray[4]);
                        tv_height.setTypeface(typefacePalatinoLinotype);
                    }
                    x+=3;
                }


            } else if (sizeTidesForFishingParserList == 6){

                //определяем и скрываем cv_time3
                cv_time = view.findViewById(R.id.cv_time3);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_time4
                cv_time = view.findViewById(R.id.cv_time4);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height3
                cv_height = view.findViewById(R.id.cv_height3);
                cv_height.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height4
                cv_height = view.findViewById(R.id.cv_height4);
                cv_height.setVisibility(View.INVISIBLE);

                int x = 0;
                for(int i = 1; i < 3; i++){
                    tv_water = view.findViewById(resourseID.tv_waterResourseID(i));
                    tv_time = view.findViewById(resourseID.tv_timeResourseID(i));
                    tv_height = view.findViewById(resourseID.tv_heightResourseID(i));
                    tv_tide = view.findViewById(resourseID.tv_tidetResourseID(i));

                    for(int y = x; y < x+1 ; y++){
                        tv_water.setText(tidesForFishingParserList.get(y));
                        tv_water.setTypeface(typefacePalatinoLinotype);
                        tv_time.setText(tidesForFishingParserList.get(y+1));
                        tv_time.setTypeface(typefacePalatinoLinotype);
                        if(tidesForFishingParserList.get(y).equals(waterSateArray[0])){
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                            tv_tide.setTypeface(typefacePalatinoLinotype);
                        }
                        tv_height.setText(tidesForFishingParserList.get(y+2) + waterSateArray[4]);
                        tv_height.setTypeface(typefacePalatinoLinotype);
                    }
                    x+=3;
                }
            }
        }


    }
}
