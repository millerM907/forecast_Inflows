package com.example.myapplication;

import android.content.pm.ActivityInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView tvPercentTide;
    private TextView tvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageView imageView = findViewById(R.id.image_bg);
        imageView.setBackgroundResource(R.drawable.highlights);

        ImageView imageView1 = findViewById(R.id.image_sand1);
        imageView1.setBackgroundResource(R.drawable.sand1);

        ImageView imageView2 = findViewById(R.id.image_wave1_20);
        imageView2.setBackgroundResource(R.drawable.wave1_20);

        String percent = "20%";
        tvPercentTide = findViewById(R.id.tv_percentTide);
        tvPercentTide.setText(percent);

        String[] state = new String[]{"ЗАГРУЗКА...", "ПРИЛИВ", "ОТЛИВ"};
        tvState = findViewById(R.id.tv_state);
        tvState.setText(state[1]);

    }
}
