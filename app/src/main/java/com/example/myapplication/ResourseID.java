package com.example.myapplication;

import android.content.Context;

public class ResourseID {
    private Context context;

    public ResourseID(Context currentContext){
        this.context = currentContext;
    }

    public int getSearchImageResourseID(int percentTides){
        int imageID = (int)(percentTides + 5) / 10;
        String imageWaveName = "wave_" + imageID;
        return context.getResources().getIdentifier(imageWaveName, "drawable", "com.example.myapplication");
    }

    public int tv_waterResourseID(int identifier, String activityName){
        String tv_water = "";

        if(activityName.equals("TodayActivity")){
            tv_water = "tv_water" + identifier;
        } else if (activityName.equals("DayActivity")){
            tv_water = "tv_day_water" + identifier;
        }

        return context.getResources().getIdentifier(tv_water, "id", "com.example.myapplication");
    }

    public int tv_timeResourseID(int identifier, String activityName){
        String tv_time = "tv_time" + identifier;

        if(activityName.equals("TodayActivity")){
            tv_time = "tv_time" + identifier;
        } else if (activityName.equals("DayActivity")){
            tv_time = "tv_day_time" + identifier;
        }

        return context.getResources().getIdentifier(tv_time, "id", "com.example.myapplication");
    }


    public int tv_heightResourseID(int identifier, String activityName){
        String tv_height = "";

        if(activityName.equals("TodayActivity")){
            tv_height = "tv_height" + identifier;
        } else if (activityName.equals("DayActivity")){
            tv_height = "tv_day_height" + identifier;
        }

        return context.getResources().getIdentifier(tv_height, "id", "com.example.myapplication");
    }


    public int tv_tideResourseID(int identifier, String activityName){
        String tv_tide = "";

        if(activityName.equals("TodayActivity")){
            tv_tide = "tv_tide" + identifier;
        } else if (activityName.equals("DayActivity")){
            tv_tide = "tv_day_tide" + identifier;
        }

        return context.getResources().getIdentifier(tv_tide, "id", "com.example.myapplication");
    }


    public int but_dayResourseID(int identifier){
        String but_day = "but_day" + identifier;
        return context.getResources().getIdentifier(but_day, "id", "com.example.myapplication");
    }

    public int tv_dayResourseID(int identifier){
        String tv_day = "tv_day" + identifier;
        return context.getResources().getIdentifier(tv_day, "id", "com.example.myapplication");
    }

}

