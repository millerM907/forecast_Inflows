package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

            if(tidesForFishingParserList.get(0).equals("-200")){

                //вывод сообщения о том, что приложение недоступно
                //CloseAlertDialog closeAlertDialog = new CloseAlertDialog();
                //AlertDialog dialog = closeAlertDialog.onCreateDialog((MainActivity) getActivity());
                //dialog.show();

            } else {
                //вычисление процента и присвоение переменной percent
                String percent = String.valueOf(TimePercent.calculatePercentUntilEndCycle(tidesForFishingParserList.get(4), tidesForFishingParserList.get(0), tidesForFishingParserList.get(2)));

                //поиск image_view для картинки
                ImageView imageView2 = view.findViewById(R.id.iv_wave);

                //если процент вычислить не удалось он равен -100, иначе если удалось
                if(percent.equals("-100")){
                    //установка картинки
                    imageView2.setBackgroundResource(R.drawable.crab);

                } else {

                    //установка картинки
                    imageView2.setBackgroundResource(resourseID.getSearchImageResourseID(Integer.valueOf(percent)));

                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.iv_bg);
        imageView.setBackgroundResource(R.drawable.highlights_sand_bg_200);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        if (viewPager != null) {
            viewPager.setAdapter(new MyAdapter(getSupportFragmentManager())); // устанавливаем адаптер
            viewPager.setCurrentItem(0); // выводим первый экран
        }
    }

    public static class MyAdapter extends FragmentPagerAdapter {

        MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CurrentActivity();
                case 1:
                    return new TodayActivity();

                default:
                    return new CurrentActivity();
            }
        }
    }
}
