package com.stlanikstudio.forecastInflows.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.stlanikstudio.forecastInflows.util.message.AppAlertDialog;
import com.stlanikstudio.forecastInflows.util.tides.ComputeTidalParam;
import com.stlanikstudio.forecastInflows.db.DBHelper;
import com.stlanikstudio.forecastInflows.R;
import com.stlanikstudio.forecastInflows.util.resourse.ResourseID;
import com.stlanikstudio.forecastInflows.util.weather.WeatherDataFormatter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DayActivity extends AppCompatActivity {

    Context thisContext;

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

    ImageButton im_button;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_v3);

        thisContext = this;

        AppAlertDialog alertDialog = new AppAlertDialog();
        dialog = alertDialog.onCreateDialog(thisContext, 3);
        dialog.show();

        ImageView imageView = findViewById(R.id.iv_day_bg);
        imageView.setBackgroundResource(R.drawable.pur_bg2);

        Bundle arguments = getIntent().getExtras();
        int keyDay = (Integer) arguments.get("keyDay");
        String numberDay = (String) arguments.get("numberDay");
        String nameDayOfWeek = (String) arguments.get("nameDayOfWeek");

        List<String> monthOfRussian = new ArrayList<>();
        Collections.addAll(monthOfRussian,
                "ЯНВАРЯ",
                "ФЕВРАЛЯ",
                "МАРТА",
                "АПРЕЛЯ",
                "МАЯ",
                "ИЮНЯ",
                "ИЮЛЯ",
                "АВГУСТА",
                "СЕНТЯБРЯ",
                "ОКТЯБРЯ",
                "НОЯБРЯ",
                "ДЕКАБРЯ");

        tv_weekDay = findViewById(R.id.tv_weekday);
        tv_weekDay.setText(nameDayOfWeek + ", "
                + numberDay + " "
                + monthOfRussian.get(LocalDate.now(ZoneId.of("Asia/Magadan")).getMonthValue() - 1)
        );

        Object[] dataTaskObjectArray = {keyDay};
        DataTask dataTask = new DataTask();
        dataTask.execute(dataTaskObjectArray);

        im_button = findViewById(R.id.ib_day_menu);
        im_button.setOnClickListener(viewClickListener);
    }

    @SuppressLint("StaticFieldLeak")
    class DataTask extends AsyncTask<Object, Void, Object[]> {
        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {

            DBHelper dbHelper = new DBHelper(thisContext);

            return new Object[]{ComputeTidalParam.getTodayTidesForFishingDataList(dbHelper, (Integer) dataTaskObjectArray[0])};
        }


        @Override
        protected void onPostExecute(Object[] objectsArray) {
            List<String> chooseDayTidesDataList = (List<String>) objectsArray[0];
            ResourseID resourseID = new ResourseID(thisContext);

            String sunriseTime = WeatherDataFormatter.parseSunriseTime(chooseDayTidesDataList.get(chooseDayTidesDataList.size()-2));
            tv_sunrise_time = findViewById(R.id.tv_day_sunrise_time);
            tv_sunrise_time.setText(sunriseTime);

            String sunsetTime = WeatherDataFormatter.parseSunsetTime(chooseDayTidesDataList.get(chooseDayTidesDataList.size()-1));
            tv_sunset_time = findViewById(R.id.tv_day_sunset_time);
            tv_sunset_time.setText(sunsetTime);

            int sizeChooseDayTidesDataList = chooseDayTidesDataList.size() - 2;

            String[] waterSateArray = {"Полная вода", "Малая вода", "малой", "полной", " м"};

            if (sizeChooseDayTidesDataList == 12) {
                int x = 0;
                for (int i = 1; i < 5; i++) {
                    tv_water = findViewById(resourseID.tv_waterResourseID(i, "DayActivity"));
                    tv_time = findViewById(resourseID.tv_timeResourseID(i, "DayActivity"));
                    tv_height = findViewById(resourseID.tv_heightResourseID(i, "DayActivity"));
                    tv_tide = findViewById(resourseID.tv_tideResourseID(i, "DayActivity"));

                    for (int y = x; y < x + 1; y++) {
                        tv_water.setText(chooseDayTidesDataList.get(y));
                        String s = chooseDayTidesDataList.get(y + 1);
                        tv_time.setText(s);
                        if (chooseDayTidesDataList.get(y).equals(waterSateArray[0])) {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                        }
                        tv_height.setText(chooseDayTidesDataList.get(y + 2) + waterSateArray[4]);
                    }
                    x += 3;
                }
                dialog.dismiss();
            } else if (sizeChooseDayTidesDataList == 9) {

                //определяем и скрываем cv_time4
                cv_time = findViewById(R.id.cv_day_time1);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height4
                cv_height = findViewById(R.id.cv_day_height1);
                cv_height.setVisibility(View.INVISIBLE);

                int x = 0;
                for (int i = 2; i <= 4; i++) {
                    tv_water = findViewById(resourseID.tv_waterResourseID(i, "DayActivity"));
                    tv_time = findViewById(resourseID.tv_timeResourseID(i, "DayActivity"));
                    tv_height = findViewById(resourseID.tv_heightResourseID(i, "DayActivity"));
                    tv_tide = findViewById(resourseID.tv_tideResourseID(i, "DayActivity"));

                    for (int y = x; y < x + 1; y++) {
                        tv_water.setText(chooseDayTidesDataList.get(y));
                        tv_time.setText(chooseDayTidesDataList.get(y + 1));
                        if (chooseDayTidesDataList.get(y).equals(waterSateArray[0])) {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                        }
                        tv_height.setText(chooseDayTidesDataList.get(y + 2) + waterSateArray[4]);
                    }
                    x += 3;
                }
                dialog.dismiss();
            } else if (sizeChooseDayTidesDataList == 6) {

                //определяем и скрываем cv_time1
                cv_time = findViewById(R.id.cv_day_time1);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_time2
                cv_time = findViewById(R.id.cv_day_time2);
                cv_time.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height1
                cv_height = findViewById(R.id.cv_day_height1);
                cv_height.setVisibility(View.INVISIBLE);

                //определяем и скрываем cv_height2
                cv_height = findViewById(R.id.cv_day_height2);
                cv_height.setVisibility(View.INVISIBLE);

                int x = 0;
                for (int i = 3; i <= 4; i++) {
                    tv_water = findViewById(resourseID.tv_waterResourseID(i, "DayActivity"));
                    tv_time = findViewById(resourseID.tv_timeResourseID(i, "DayActivity"));
                    tv_height = findViewById(resourseID.tv_heightResourseID(i, "DayActivity"));
                    tv_tide = findViewById(resourseID.tv_tideResourseID(i, "DayActivity"));

                    for (int y = x; y < x + 1; y++) {
                        tv_water.setText(chooseDayTidesDataList.get(y));
                        tv_time.setText(chooseDayTidesDataList.get(y + 1));
                        if (chooseDayTidesDataList.get(y).equals(waterSateArray[0])) {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[3]));
                        } else {
                            tv_tide.setText(getString(R.string.tide_now, waterSateArray[2]));
                        }
                        tv_height.setText(chooseDayTidesDataList.get(y + 2) + waterSateArray[4]);
                    }
                    x += 3;
                }
            }
            dialog.dismiss();
        }
    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //showPopupMenu(v);
            AppAlertDialog alertDialog = new AppAlertDialog();
            android.app.AlertDialog dialog = alertDialog.onCreateDialog(thisContext, 2);
            dialog.show();

            TextView messageView = dialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.CENTER);
        }
    };

    /*
    public void showPopupMenu (View view)
    {
        PopupMenu menu = new PopupMenu (this, view);
        menu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener ()
        {
            @Override
            public boolean onMenuItemClick (MenuItem item)
            {
                int id = item.getItemId();
                switch (id)
                {

                    case R.id.item_instruction:
                        Intent intent = new Intent(thisContext, InstructionActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.item_about:
                        AppAlertDialog alertDialog = new AppAlertDialog();
                        android.app.AlertDialog dialog = alertDialog.onCreateDialog(thisContext, 2);
                        dialog.show();

                        TextView messageView = dialog.findViewById(android.R.id.message);
                        messageView.setGravity(Gravity.CENTER);
                        break;
                }
                return true;
            }
        });
        menu.inflate (R.menu.popupmenu);
        menu.show();
    }
    */

}