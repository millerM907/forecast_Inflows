package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context thisContext;

    TextView tv_time;

    ImageButton im_button;
    static ImageView im_view_start_screen;
    static ImageView im_view_2;

    //создаем обработчик для обновления текущего времени
    Handler handlerCurrentTime;


    SharedPreferences mSettings = null;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_DB_DATE_UPDATE = "db_date_update";


    @SuppressLint({"ClickableViewAccessibility", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //установка стартовой страницы приложения
        im_view_start_screen = findViewById(R.id.imageView);
        im_view_2 = findViewById(R.id.imageView2);

        im_view_start_screen.bringToFront();
        im_view_2.bringToFront();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mSettings = this.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        thisContext = this;

        ImageView imageView = findViewById(R.id.iv_bg);
        imageView.setBackgroundResource(R.drawable.pur_bg);

        handlerCurrentTime = new Handler();
        tv_time = findViewById(R.id.tv_time);
        handlerCurrentTime.post(showCurrentTimeInfo);

        //проверка интернет соединения
        if (!NetworkManager.isNetworkAvailable(thisContext) && mSettings.getBoolean("firstrun", true)) {
            //вывод сообщения о том, что приложение недоступно из-за ошибки интернет соединения
            AppAlertDialog alertDialog = new AppAlertDialog();
            android.app.AlertDialog dialog = alertDialog.onCreateDialog(thisContext, 1);
            dialog.setCancelable(false);
            dialog.show();
        } else if ((!NetworkManager.isNetworkAvailable(thisContext)) && !LocalDateTime.parse(mSettings.getString(APP_PREFERENCES_DB_DATE_UPDATE, null)).getMonth().equals(LocalDateTime.now(ZoneId.of("Asia/Magadan")).getMonth())){
            //вывод сообщения о том, что приложение недоступно из-за ошибки обновления базы без интернет соединения
            AppAlertDialog alertDialog = new AppAlertDialog();
            android.app.AlertDialog dialog = alertDialog.onCreateDialog(thisContext, 4);
            dialog.setCancelable(false);
            dialog.show();

        } else {
            im_button = findViewById(R.id.imageButton);
            im_button.setOnClickListener(viewClickListener);

            Object[] dataTaskObjectArray = {thisContext};

            //запускаем поток по отрисовке процентов и передаем в него массив, содержищий контекст
            DataTask dataTask = new DataTask();
            dataTask.execute(dataTaskObjectArray);

            ViewPager viewPager = findViewById(R.id.pager);
            viewPager.setOffscreenPageLimit(2);

            viewPager.setAdapter(new MyAdapter(getSupportFragmentManager())); // устанавливаем адаптер
            viewPager.setCurrentItem(0); // выводим первый экран
        }

    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }
    };

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

            //если приложение стартует первый раз
            if (mSettings.getBoolean("firstrun", true)) {

                return new Object[]{dataTaskObjectArray[0], TidesForFishingParser.getTidesTableDataList()};

            } else {

                //получаем текущую дату и дату обновления базы
                LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));
                LocalDateTime dbUpdateTime = LocalDateTime.parse(mSettings.getString(APP_PREFERENCES_DB_DATE_UPDATE, null));

                //если в прошлом месяце, то получаем список, иначе передаем только контекст
                if(!dbUpdateTime.getMonth().equals(localDateTime.getMonth())) {
                    return new Object[]{dataTaskObjectArray[0], TidesForFishingParser.getTidesTableDataList()};
                } else {
                    return new Object[]{dataTaskObjectArray[0]};
                }
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Object[] objectsArray) {

            //работаем дальше
            DBHelper dbHelper = new DBHelper(thisContext);

            //получаем запрос на запись бд
            SQLiteDatabase databaseTidesWritable = dbHelper.getWritableDatabase();

            //если приложение стартует первый раз
            if (mSettings.getBoolean("firstrun", true)) {
                //получае список с расписанием
                List tidesTable = (List) objectsArray[1];

                //проверка на возврат отказа парсером
                if(tidesTable.get(0).equals("-200")){

                    //вывод сообщения о том, что приложение недоступно по техническим причинам
                    AppAlertDialog alertDialog = new AppAlertDialog();
                    android.app.AlertDialog dialog = alertDialog.onCreateDialog(thisContext, 0);
                    dialog.setCancelable(false);
                    dialog.show();

                } else {

                    //записываем данные в бд
                    dbHelper.addTidesData(databaseTidesWritable, tidesTable);

                    /*записываем во вторую строку преференсов дату обновления */
                    mSettings.edit().putString(APP_PREFERENCES_DB_DATE_UPDATE, String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Magadan")))).apply();

                    mSettings.edit().putBoolean("firstrun", false).apply();
                }
                //если стартует не первый раз
            } else {

                //получаем текущую дату и дату обновления базы
                LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));
                LocalDateTime dbUpdateTime = LocalDateTime.parse(mSettings.getString(APP_PREFERENCES_DB_DATE_UPDATE, null));

                //если база обновлялась в прошлом месяце, тогда

                if(!dbUpdateTime.getMonth().equals(localDateTime.getMonth())){
                    System.out.println("След месяц");
                    //получае список с расписанием
                    List tidesTable = (List) objectsArray[1];

                    //проверка на возврат отказа парсером
                    if(tidesTable.get(0).equals("-200")) {

                        //вывод сообщения о том, что приложение недоступно по техническим причинам
                        AppAlertDialog alertDialog = new AppAlertDialog();
                        android.app.AlertDialog dialog = alertDialog.onCreateDialog(thisContext, 0);
                        dialog.setCancelable(false);
                        dialog.show();
                    } else {

                        //стираем данные в таблтце БД
                        dbHelper.onUpgrade(databaseTidesWritable, 0, 0);

                        //записываем полученные данные
                        dbHelper.addTidesData(databaseTidesWritable, tidesTable);

                        //записываем дату обновления БД в переменную
                        mSettings.edit().putString(APP_PREFERENCES_DB_DATE_UPDATE, String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Magadan")))).apply();
                    }
                }
            }


            List<String> tidesForFishingParserList = ComputeTidalParam.getCurrentTidesForFishingDataList(dbHelper);
            Context thiscontext = (Context) objectsArray[0];
            ResourseID resourseID = new ResourseID(thiscontext);

            if(tidesForFishingParserList.get(0).equals("-200")){

                //вывод сообщения о том, что приложение недоступно по техническим причинам
                AppAlertDialog alertDialog = new AppAlertDialog();
                android.app.AlertDialog dialog = alertDialog.onCreateDialog(thiscontext, 0);
                dialog.setCancelable(false);
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
}
