package com.example.myapplication;

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

            int sizeTidesForFishingParserList = tidesForFishingParserList.size();

            if(sizeTidesForFishingParserList == 12){
                Typeface typefacePalatinoLinotype = Typeface.createFromAsset(thiscontext.getAssets(), "fonts/pala.ttf");

                //выводим состояние (малая/полная вода)
                tv_water = view.findViewById(R.id.tv_water1);
                tv_water.setText(tidesForFishingParserList.get(0));
                tv_water.setTypeface(typefacePalatinoLinotype);

                tv_water = view.findViewById(R.id.tv_water2);
                tv_water.setText(tidesForFishingParserList.get(3));
                tv_water.setTypeface(typefacePalatinoLinotype);

                tv_water = view.findViewById(R.id.tv_water3);
                tv_water.setText(tidesForFishingParserList.get(6));
                tv_water.setTypeface(typefacePalatinoLinotype);

                tv_water = view.findViewById(R.id.tv_water4);
                tv_water.setText(tidesForFishingParserList.get(9));
                tv_water.setTypeface(typefacePalatinoLinotype);

                //выводим время прилива/отлива
                tv_time = view.findViewById(R.id.tv_time1);
                tv_time.setText(tidesForFishingParserList.get(1));
                tv_time.setTypeface(typefacePalatinoLinotype);

                tv_time = view.findViewById(R.id.tv_time2);
                tv_time.setText(tidesForFishingParserList.get(4));
                tv_time.setTypeface(typefacePalatinoLinotype);

                tv_time = view.findViewById(R.id.tv_time3);
                tv_time.setText(tidesForFishingParserList.get(7));
                tv_time.setTypeface(typefacePalatinoLinotype);

                tv_time = view.findViewById(R.id.tv_time4);
                tv_time.setText(tidesForFishingParserList.get(10));
                tv_time.setTypeface(typefacePalatinoLinotype);

                //выводим значения вывода прилива/отлива
                tv_height = view.findViewById(R.id.tv_height1);
                tv_height.setText(tidesForFishingParserList.get(2));
                tv_height.setTypeface(typefacePalatinoLinotype);

                tv_height = view.findViewById(R.id.tv_height2);
                tv_height.setText(tidesForFishingParserList.get(5));
                tv_height.setTypeface(typefacePalatinoLinotype);

                tv_height = view.findViewById(R.id.tv_height3);
                tv_height.setText(tidesForFishingParserList.get(8));
                tv_height.setTypeface(typefacePalatinoLinotype);

                tv_height = view.findViewById(R.id.tv_height4);
                tv_height.setText(tidesForFishingParserList.get(11));
                tv_height.setTypeface(typefacePalatinoLinotype);

            } else if (sizeTidesForFishingParserList == 9){

            } else if (sizeTidesForFishingParserList == 6){

            }
        }


    }
}
