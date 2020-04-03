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
        int resourseID = context.getResources().getIdentifier(imageWaveName, "drawable", "com.example.myapplication");
        return resourseID;
    }

    public int tv_waterResourseID(int identifier){
        String tv_water = "tv_water" + identifier;
        int resourseID = context.getResources().getIdentifier(tv_water, "id", "com.example.myapplication");
        return resourseID;
    }

    public int tv_day_waterResourseID(int identifier){
        String tv_water = "tv_day_water" + identifier;
        int resourseID = context.getResources().getIdentifier(tv_water, "id", "com.example.myapplication");
        return resourseID;
    }

    public int tv_timeResourseID(int identifier){
        String tv_time = "tv_time" + identifier;
        int resourseID = context.getResources().getIdentifier(tv_time, "id", "com.example.myapplication");
        return resourseID;
    }

    public int tv_day_timeResourseID(int identifier){
        String tv_time = "tv_day_time" + identifier;
        int resourseID = context.getResources().getIdentifier(tv_time, "id", "com.example.myapplication");
        return resourseID;
    }

    public int tv_heightResourseID(int identifier){
        String tv_height = "tv_height" + identifier;
        int resourseID = context.getResources().getIdentifier(tv_height, "id", "com.example.myapplication");
        return resourseID;
    }

    public int tv_day_heightResourseID(int identifier){
        String tv_height = "tv_day_height" + identifier;
        int resourseID = context.getResources().getIdentifier(tv_height, "id", "com.example.myapplication");
        return resourseID;
    }

    public int tv_tideResourseID(int identifier){
        String tv_tide = "tv_tide" + identifier;
        int resourseID = context.getResources().getIdentifier(tv_tide, "id", "com.example.myapplication");
        return resourseID;
    }

    public int tv_day_tideResourseID(int identifier){
        String tv_tide = "tv_day_tide" + identifier;
        int resourseID = context.getResources().getIdentifier(tv_tide, "id", "com.example.myapplication");
        return resourseID;
    }

    public int but_dayResourseID(int identifier){
        String but_day = "but_day" + identifier;
        int resourseID = context.getResources().getIdentifier(but_day, "id", "com.example.myapplication");
        return resourseID;
    }

    public int tv_dayResourseID(int identifier){
        String tv_day = "tv_day" + identifier;
        int resourseID = context.getResources().getIdentifier(tv_day, "id", "com.example.myapplication");
        return resourseID;
    }

}

