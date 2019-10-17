package com.example.myapplication;

import android.content.Context;

public class ResourseID {
    private Context context;

    public ResourseID(Context current){
        this.context = current;
    }

    public int getSearchImageResourseID(int percentTides){
        int imageID = (int)(percentTides + 5) / 10;
        String imageWaveName = "wave_" + imageID;
        int resourseID = context.getResources().getIdentifier(imageWaveName, "drawable", context.getPackageName());
        return resourseID;
    }
}
