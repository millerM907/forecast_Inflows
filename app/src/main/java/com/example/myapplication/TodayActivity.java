package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TodayActivity extends Fragment {

    TextView tvSunset;
    TextView tvSunrise;
    //ImageView ivBg;
    ImageView ivShell;

    Context thiscontext;

    TextView tv_water;
    TextView tv_time;
    TextView tv_height;
    TextView tv_tide;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_today, container, false);

        thiscontext = getActivity();

        ivShell = v.findViewById(R.id.iv_shell);
        ivShell.setBackgroundResource(R.drawable.shell);

        Typeface typefacePalatinoLinotype = Typeface.createFromAsset(thiscontext.getAssets(), "fonts/pala.ttf");

        tvSunrise = v.findViewById(R.id.tv_sunrise);
        tvSunrise.setTypeface(typefacePalatinoLinotype);

        tvSunset = v.findViewById(R.id.tv_sunset);
        tvSunset.setTypeface(typefacePalatinoLinotype);

        Object[] dataTaskObjectArray = {v, thiscontext};
        DataTask dataTask = new DataTask();
        dataTask.execute(dataTaskObjectArray);

        return v;
    }

    class DataTask extends AsyncTask<Object, Void, Object[]> {
        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {
           return new Object[]{TidesForFishingParser.getTodayTidesForFishingDataList(), dataTaskObjectArray[0], dataTaskObjectArray[1]};
        }


        @Override
        protected void onPostExecute(Object[] objectsArray) {
            List<String> tidesForFishingParserList = (List<String>) objectsArray[0];
            View view = (View) objectsArray[1];
            thiscontext = (Context) objectsArray[2];
            ResourseID resourseID = new ResourseID(thiscontext);

            int sizeTidesForFishingParserList = tidesForFishingParserList.size();

            String[] waterSateArray = {"Полная вода", "Малая вода", "отлива","прилива", " м"};

            if(sizeTidesForFishingParserList == 12){
                Typeface typefacePalatinoLinotype = Typeface.createFromAsset(thiscontext.getAssets(), "fonts/pala.ttf");

                int x = 0;
                for(int i = 1; i < 5; i++){
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

            } else if (sizeTidesForFishingParserList == 9){

            } else if (sizeTidesForFishingParserList == 6){

            }
        }


    }
}
