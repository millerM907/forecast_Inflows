package com.stlanikstudio.forecastInflows.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.stlanikstudio.forecastInflows.AppAlertDialog;
import com.stlanikstudio.forecastInflows.ComputeTidalParam;
import com.stlanikstudio.forecastInflows.models.CurrentWeather;
import com.stlanikstudio.forecastInflows.db.DBHelper;
import com.stlanikstudio.forecastInflows.NetworkManager;
import com.stlanikstudio.forecastInflows.R;
import com.stlanikstudio.forecastInflows.ResourseID;
import com.stlanikstudio.forecastInflows.TidesForFishingParser;
import com.stlanikstudio.forecastInflows.TimePercent;
import com.stlanikstudio.forecastInflows.models.TidesDay;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context thisContext;

    private TextView tv_time;

    private ImageButton im_button;

    private static ImageView im_view_start_screen;
    private static ImageView im_view_2;
    private static boolean imViewStoneState = false;

    //создаем обработчик для обновления текущего времени
    private Handler handlerCurrentTime;


    private SharedPreferences mSettings = null;
    private static final String APP_PREFERENCES = "mysettings";
    private static final String APP_PREFERENCES_DB_DATE_UPDATE = "db_date_update";


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //установка стартовой страницы приложения
        im_view_start_screen = findViewById(R.id.iv_white);
        im_view_2 = findViewById(R.id.iv_icon);

        im_view_start_screen.bringToFront();
        im_view_2.bringToFront();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mSettings = this.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        thisContext = this;

        handlerCurrentTime = new Handler();
        tv_time = findViewById(R.id.tv_time);
        handlerCurrentTime.post(showCurrentTimeInfo);

        im_button = findViewById(R.id.ib_main_menu);
        im_button.setOnClickListener(viewClickListener);

        /*//проверка интернет соединения
        if (!NetworkManager.isNetworkAvailable(thisContext) && mSettings.getBoolean("firstrun", true)) {
            //вывод сообщения о том, что приложение недоступно из-за ошибки интернет соединения
            AppAlertDialog.showAlertDialog(thisContext, 1);
        } else if ((!NetworkManager.isNetworkAvailable(thisContext)) && !LocalDateTime.parse(mSettings.getString(APP_PREFERENCES_DB_DATE_UPDATE, null)).getMonth().equals(LocalDateTime.now(ZoneId.of("Asia/Magadan")).getMonth())){
            //вывод сообщения о том, что приложение недоступно из-за ошибки обновления базы без интернет соединения
            AppAlertDialog.showAlertDialog(thisContext, 4);

        } else if(!NetworkManager.isNetworkAvailable(thisContext)) {
            //вывод сообщения о том, что данные о текущей погоде могут отображаться некорреткно
            AppAlertDialog.showAlertDialog(thisContext, 5);
            continueLoadMainActivity();

        //проверить, доступен ли мой сервер, если нет выводим соответствующее сообщение
        } else if (!isServerAlive("https://still-dusk-90773.herokuapp.com")) {
            AppAlertDialog.showAlertDialog(thisContext, 6);
            continueLoadMainActivity();
        } else {
            continueLoadMainActivity();
        }*/

        ChekInet chekInet = new ChekInet();
        chekInet.execute();

    }

    // To check if server is reachable
    public boolean isServerAlive(String serverURL) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(serverURL).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode != 404) {
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
    * Метод принимает на вход Сontext и ключ вызываемого сообщения keyMessage.
    * Создает и выводит AlertDialog с сообщением.
    * */
    private void showAlertDialog(Context context, int keyMessage){
        AppAlertDialog alertDialog = new AppAlertDialog();
        android.app.AlertDialog dialog = alertDialog.onCreateDialog(context, keyMessage);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static ImageView getIm_view_start_screen(){
        return im_view_start_screen;
    }

    public static ImageView getIm_view_2(){
        return im_view_2;
    }

    public static boolean getImViewStoneState(){
        return imViewStoneState;
    }

    /**
     *Метод запускает асинхронный поток, отвечающий за проверку обновления и обновление бд,
     * установку картинки прилива и инициализацию ViewPager.
     * */
    private void continueLoadMainActivity(){

        Object[] dataTaskObjectArray = {thisContext};

        //запускаем поток по отрисовке процентов и передаем в него массив, содержищий контекст
        DataTask dataTask = new DataTask();
        dataTask.execute(dataTaskObjectArray);

        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager())); // устанавливаем адаптер
        viewPager.setCurrentItem(0); // выводим первый экран
    }


    /**
     *Метод запускает поток, обновляющтй текущее время в тулбаре.
     * */
    Runnable showCurrentTimeInfo = new Runnable() {
        public void run() {

            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));
            String currentTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            tv_time.setText(currentTime);
            // планирует сам себя через 1000 мсек
            handlerCurrentTime.postDelayed(showCurrentTimeInfo, 1000);
        }
    };


    class ChekInet extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            //проверка интернет соединения
            if (!NetworkManager.isNetworkAvailable(thisContext) && mSettings.getBoolean("firstrun", true)) {
                //код ошибки 1 - приложение недоступно из-за ошибки интернет соединения
                return 1;
            } else if ((!NetworkManager.isNetworkAvailable(thisContext)) && !LocalDateTime.parse(mSettings.getString(APP_PREFERENCES_DB_DATE_UPDATE, null)).getMonth().equals(LocalDateTime.now(ZoneId.of("Asia/Magadan")).getMonth())){
                //код ошибки 4 - приложение недоступно из-за ошибки обновления базы без интернет соединения
                return 4;
            } else if(!NetworkManager.isNetworkAvailable(thisContext)) {
                //код ошибки 5 - данные о текущей погоде могут отображаться некорреткно из-за недоступности интернет соединения
                return 5;
            } else if (!isServerAlive("https://still-dusk-90773.herokuapp.com")) {
                //код ошибки 6 - данные о текущей погоде могут отображаться некорреткно из-за недоступности сервера
                return 6;
            }

            return 10;
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case 1:
                    AppAlertDialog.showAlertDialog(thisContext, 1);
                    break;
                case 4:
                    AppAlertDialog.showAlertDialog(thisContext, 4);
                    break;
                case 5:
                    AppAlertDialog.showAlertDialog(thisContext, 5);
                    continueLoadMainActivity();
                    break;
                case 6:
                    AppAlertDialog.showAlertDialog(thisContext, 6);
                    continueLoadMainActivity();
                    break;
                case 10:
                    continueLoadMainActivity();
                    break;
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    class DataTask extends AsyncTask<Object, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Object[] dataTaskObjectArray) {

            //если приложение стартует первый раз
            if (mSettings.getBoolean("firstrun", true)) {

                return new Object[]{dataTaskObjectArray[0], getTidesTable()};

            } else {

                //получаем текущую дату и дату обновления базы
                LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));
                LocalDateTime dbUpdateTime = LocalDateTime.parse(mSettings.getString(APP_PREFERENCES_DB_DATE_UPDATE, null));

                //если таблица обновлялась в прошлом месяце, то получаем список, иначе передаем только контекст
                if (!dbUpdateTime.getMonth().equals(localDateTime.getMonth())) {
                    return new Object[]{dataTaskObjectArray[0], getTidesTable()};
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
                //получаем список с расписанием
                List<TidesDay> tidesTable = (List) objectsArray[1];

                //проверка на возврат отказа парсером
                if(tidesTable == null){

                    //вывод сообщения о том, что приложение недоступно по техническим причинам
                    AppAlertDialog.showAlertDialog(thisContext, 0);

                } else {

                    //записываем данные в бд
                    dbHelper.addTidesData(databaseTidesWritable, tidesTable);

                    /*записываем во вторую строку преференсов дату обновления таблицы приливов*/
                    mSettings.edit().putString(APP_PREFERENCES_DB_DATE_UPDATE, String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Magadan")))).apply();

                    mSettings.edit().putBoolean("firstrun", false).apply();
                }
            //если стартует не первый раз
            } else {

                //получаем текущую дату и дату обновления базы
                LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Magadan"));
                LocalDateTime dbUpdateTime = LocalDateTime.parse(mSettings.getString(APP_PREFERENCES_DB_DATE_UPDATE, null));

                //если база обновлялась не в этом месяце, тогда

                if(!dbUpdateTime.getMonth().equals(localDateTime.getMonth())){

                    //получае список с расписанием
                    List<TidesDay> tidesTable = (List<TidesDay>) objectsArray[1];

                    //проверка на возврат отказа парсером
                    if(tidesTable == null) {

                        //вывод сообщения о том, что приложение недоступно по техническим причинам
                        AppAlertDialog.showAlertDialog(thisContext, 0);

                    } else {

                        //стираем данные в таблице БД
                        dbHelper.onUpgrade(databaseTidesWritable, 0, 0);

                        //записываем полученные данные
                        dbHelper.addTidesData(databaseTidesWritable, tidesTable);

                        //записываем дату обновления БД в переменную
                        mSettings.edit().putString(APP_PREFERENCES_DB_DATE_UPDATE, String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Magadan")))).apply();
                    }
                }
            }

            //Переименовать tidesForFishingParserList
            List<String> tidesForFishingParserList = ComputeTidalParam.getCurrentTidesForFishingDataList(dbHelper);
            Context thisContext = (Context) objectsArray[0];
            ResourseID resourseID = new ResourseID(thisContext);

            if(tidesForFishingParserList.get(0).equals("-200")){

                //вывод сообщения о том, что приложение недоступно по техническим причинам
                AppAlertDialog.showAlertDialog(thisContext, 0);
            } else {
                //вычисление процента и присвоение переменной percent
                String percent = String.valueOf(TimePercent.calculatePercentUntilEndCycle(tidesForFishingParserList.get(4), tidesForFishingParserList.get(0), tidesForFishingParserList.get(2)));

                //поиск image_view для картинки
                ImageView imageView2 = findViewById(R.id.iv_wave);
                //если процент вычислить не удалось он равен -100, иначе если удалось
                if(percent.equals("-100")){
                    //установка картинки
                    imageView2.setBackgroundResource(R.drawable.stone);
                    imViewStoneState = true;

                } else {
                    //установка картинки
                    imageView2.setBackgroundResource(resourseID.getSearchImageResourseID(Integer.parseInt(percent)));
                }
            }
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

    private List<TidesDay> getTidesTable() {
        try {
            final String url = "https://still-dusk-90773.herokuapp.com/api/v1.0/getTidesTable";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            TidesDay[] tidesTableArray = restTemplate.getForObject(url, TidesDay[].class);
            System.out.println("update");
            return Arrays.asList(tidesTableArray);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
            return null;
        }
    }



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
