package com.mvcoder.mediacomponent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wang.avi.AVLoadingIndicatorView;

public class MainActivity extends AppCompatActivity {


    private AVLoadingIndicatorView loadingView;

    private Button btShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        loadingView = findViewById(R.id.loading);
        int yellow = Color.parseColor("#ffff00");
        int green = Color.parseColor("#b2ff59");
        int blue = Color.parseColor("#00b0ff");
        RainbowIndicator indicator = new RainbowIndicator(new int[]{yellow,green,blue});
        loadingView.setIndicator(indicator);
        btShow = findViewById(R.id.btShow);
        btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loadingView.isShown()){
                    loadingView.hide();
                }else {
                    loadingView.show();
                }
            }
        });
    }
}
