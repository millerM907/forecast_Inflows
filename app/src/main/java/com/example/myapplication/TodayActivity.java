package com.example.myapplication;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TodayActivity extends AppCompatActivity {

    TextView tvSunset;
    TextView tvSunrise;
    ImageView ivBg;
    ImageView ivShell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        ivBg = findViewById(R.id.iv_bg);
        ivBg.setBackgroundResource(R.drawable.highlights);

        ivShell = findViewById(R.id.iv_shell);
        ivShell.setBackgroundResource(R.drawable.shell);

        Typeface typefacePalatinoLinotype = Typeface.createFromAsset(getAssets(), "fonts/pala.ttf");

        tvSunrise = findViewById(R.id.tv_sunrise);
        tvSunrise.setTypeface(typefacePalatinoLinotype);

        tvSunset = findViewById(R.id.tv_sunset);
        tvSunset.setTypeface(typefacePalatinoLinotype);
    }
}
