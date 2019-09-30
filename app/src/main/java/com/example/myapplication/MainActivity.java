package com.example.myapplication;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView tvPercentTide;
    private TextView tvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ResourseID resourseID = new ResourseID(this);

        ImageView imageView = findViewById(R.id.iv_bg);
        imageView.setBackgroundResource(R.drawable.highlights);

        ImageView imageView1 = findViewById(R.id.iv_sand);
        imageView1.setBackgroundResource(R.drawable.sand1);

        int x = 53;
        ImageView imageView2 = findViewById(R.id.iv_wave5);
        imageView2.setBackgroundResource(resourseID.getSearchImageResourseID(x));

        String percent = "20%";
        tvPercentTide = findViewById(R.id.tv_percentTide);
        tvPercentTide.setText(percent);
        Typeface typefaceCopperplateGothic = Typeface.createFromAsset(getAssets(), "fonts/COPRGTL.TTF");
        tvPercentTide.setTypeface(typefaceCopperplateGothic);

        String[] state = new String[]{"ЗАГРУЗКА...", "ПРИЛИВ", "ОТЛИВ"};
        tvState = findViewById(R.id.tv_state);
        tvState.setText(state[1]);
        Typeface typefacePalatinoLinotype = Typeface.createFromAsset(getAssets(), "fonts/pala.ttf");
        tvState.setTypeface(typefacePalatinoLinotype);

    }
}
