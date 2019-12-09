package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context thiscontext;

    TextView tv_time;

    //создаем обработчик для обновления текущего времени
    Handler handlerCurrentTime;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thiscontext = this;

        ImageView imageView = findViewById(R.id.iv_bg);
        imageView.setBackgroundResource(R.drawable.highlights_sand_bg_200);

        handlerCurrentTime = new Handler();
        tv_time = findViewById(R.id.tv_time);
        handlerCurrentTime.post(showCurrentTimeInfo);

        Object[] dataTaskObjectArray = {thiscontext};

        //запускаем поток по отрисовке процентов и передаем в него массив, содержищий контекст
        DataTask dataTask = new DataTask();
        dataTask.execute(dataTaskObjectArray);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        if (viewPager != null) {
            viewPager.setAdapter(new MyAdapter(getSupportFragmentManager())); // устанавливаем адаптер
            viewPager.setCurrentItem(0); // выводим первый экран
        }
    }


    Runnable showCurrentTimeInfo = new Runnable() {
        public void run() {
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));
            String currentTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            tv_time.setText(currentTime);
            // планирует сам себя через 1000 мсек
            handlerCurrentTime.postDelayed(showCurrentTimeInfo, 1000);
        }
    };


    @SuppressLint("StaticFieldLeak")
    class DataTask extends AsyncTask<Object, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {
            return new Object[]{TidesForFishingParser.getCurrentTidesForFishingDataList(), dataTaskObjectArray[0]};
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Object[] objectsArray) {
            List<String> tidesForFishingParserList = (List<String>) objectsArray[0];
            System.out.println(objectsArray[1]);
            Context thiscontext = (Context) objectsArray[1];
            ResourseID resourseID = new ResourseID(thiscontext);

            if(tidesForFishingParserList.get(0).equals("-200")){

                //вывод сообщения о том, что приложение недоступно
                CloseAlertDialog closeAlertDialog = new CloseAlertDialog();
                AlertDialog dialog = closeAlertDialog.onCreateDialog((MainActivity) thiscontext);
                dialog.show();

            } else {
                //вычисление процента и присвоение переменной percent
                String percent = String.valueOf(TimePercent.calculatePercentUntilEndCycle(tidesForFishingParserList.get(4), tidesForFishingParserList.get(0), tidesForFishingParserList.get(2)));

                //поиск image_view для картинки
                ImageView imageView2 = findViewById(R.id.iv_wave);
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

    public static class MyAdapter extends FragmentPagerAdapter {

        MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CurrentActivity();
                case 1:
                    return new TodayActivity();
                case 2:
                    return new WeekActivity();
                default:
                    return new CurrentActivity();
            }
        }
    }
}
