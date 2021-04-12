package com.example.curameapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Play fad ein on start
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //Display for 2 seconds
        Context context = this;
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //Start the main activity after 2 seconds
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                finish();
            }
        }, 2000);
    }

}