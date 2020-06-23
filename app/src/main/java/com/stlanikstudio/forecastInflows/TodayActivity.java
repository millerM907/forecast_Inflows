package com.stlanikstudio.forecastInflows;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TodayActivity extends Fragment {

    Context thisContext;

    TextView tv_sunrise_time;
    TextView tv_sunset_time;

    TextView tv_water;
    TextView tv_time;
    TextView tv_height;
    TextView tv_tide;
    CardView cv_time;
    CardView cv_height;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_today, container, false);

        thisContext = getActivity();

        Object[] dataTaskObjectArray = {v};
        DataTask dataTask = new DataTask();
        dataTask.execute(dataTaskObjectArray);

        return v;
    }

    @SuppressLint("StaticFieldLeak")
    class DataTask extends AsyncTask<Object, Void, Object[]> {
        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {
           DBHelper dbHelper = new DBHelper(thisContext);

           return new Object[]{ComputeTidalParam.getTodayTidesForFishingDataList(dbHelper,0), dataTaskObjectArray[0]};
        }


        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Object[] objectsArray) {
            List<String> todayTidesDataList = (List<String>) objectsArray[0];
            View view = (View) objectsArray[1];
            ResourseID resourseID = new ResourseID(thisContext);

            String sunriseTime = WeatherDataFormatter.parseSunriseTime(todayTidesDataList.get(todayTidesDataList.size()-2));
            tv_sunrise_time = view.findViewById(R.id.tv_sunrise_time);
            tv_sunrise_time.setText(sunriseTime);

            String sunsetTime = WeatherDataFormatter.parseSunsetTime(todayTidesDataList.get(todayTidesDataList.size()-1));
            tv_sunset_time = view.findViewById(R.id.tv_sunset_time);
            tv_sunset_time.setText(sunsetTime);

            int sizeTodayTidesDataList = todayTidesDataList.size() - 2;

            String[] waterSateArray = {"Полная вода", "Малая вода", "малой","полной", " м"};


            if(sizeTodayTidesDataList == 12){
                int x = 0;
                for(int i = 1; i < 5; i++){
                    tv_water = view.findViewById(resourseID.tv_waterResourseID(i, "TodayActivity"));
                    tv_time = view.findViewById(resourseID.tv_timeResourseID(i, "TodayActivity"));
                    tv_height = view.findViewById(resourseID.tv_heightResourseID(i, "TodayActivity"));
                    tv_tide = view.findViewById(resourseID.tv_tideResourseID(i, "TodayActivity"));

                    for(int y = x; y < x+1 ; y++){
                        tv_water.setText(todayTidesDataList.get(y));
                        String s = todayTidesDataList.get(y+1);
                        tv_time.setText(s);
                        if(todayTidesDataList.get(y).equals(waterSateArray[0])){
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                        }
                        tv_height.setText(todayTidesDataList.get(y+2) + waterSateArray[4]);
                    }
                    x+=3;
                }

            } else if (sizeTodayTidesDataList == 9){

                //определяем и скрываем cv_time4
                cv_time = view.findViewById(R.id.cv_time1);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height4
                cv_height = view.findViewById(R.id.cv_height1);
                cv_height.setVisibility(View.INVISIBLE);

                int x = 0;
                for (int i = 2; i <= 4; i++) {
                    tv_water = view.findViewById(resourseID.tv_waterResourseID(i, "TodayActivity"));
                    tv_time = view.findViewById(resourseID.tv_timeResourseID(i, "TodayActivity"));
                    tv_height = view.findViewById(resourseID.tv_heightResourseID(i, "TodayActivity"));
                    tv_tide = view.findViewById(resourseID.tv_tideResourseID(i, "TodayActivity"));

                    for (int y = x; y < x + 1; y++) {
                        tv_water.setText(todayTidesDataList.get(y));
                        tv_time.setText(todayTidesDataList.get(y + 1));
                        if (todayTidesDataList.get(y).equals(waterSateArray[0])) {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                        }
                        tv_height.setText(todayTidesDataList.get(y + 2) + waterSateArray[4]);
                    }
                    x += 3;
                }

            } else if (sizeTodayTidesDataList == 6){

                //определяем и скрываем cv_time1
                cv_time = view.findViewById(R.id.cv_time1);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_time2
                cv_time = view.findViewById(R.id.cv_time2);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height1
                cv_height = view.findViewById(R.id.cv_height1);
                cv_height.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height2
                cv_height = view.findViewById(R.id.cv_height2);
                cv_height.setVisibility(View.INVISIBLE);

                int x = 0;
                for (int i = 3; i <= 4; i++) {
                    tv_water = view.findViewById(resourseID.tv_waterResourseID(i, "TodayActivity"));
                    tv_time = view.findViewById(resourseID.tv_timeResourseID(i, "TodayActivity"));
                    tv_height = view.findViewById(resourseID.tv_heightResourseID(i, "TodayActivity"));
                    tv_tide = view.findViewById(resourseID.tv_tideResourseID(i, "TodayActivity"));

                    for (int y = x; y < x + 1; y++) {
                        tv_water.setText(todayTidesDataList.get(y));
                        tv_time.setText(todayTidesDataList.get(y + 1));
                        if (todayTidesDataList.get(y).equals(waterSateArray[0])) {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                        }
                        tv_height.setText(todayTidesDataList.get(y + 2) + waterSateArray[4]);
                    }
                    x += 3;
                }
            }
        }


    }
}
