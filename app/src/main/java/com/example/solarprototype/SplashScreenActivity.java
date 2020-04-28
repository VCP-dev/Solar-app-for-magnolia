package com.example.solarprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1023;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //SplashScreenContext = getApplicationContext();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent Main = new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(Main);
            }
        },SPLASH_TIME_OUT);
    }




}
