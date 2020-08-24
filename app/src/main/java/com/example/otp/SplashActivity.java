package com.example.otp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
        if (ConnectionCheck.isConnected(connectivityManager, networkInfo, this)) {

            handler.postDelayed(new Runnable() {@Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
            }, 3000);

        } else {
            Toast.makeText(this, "Internet Connection Required", Toast.LENGTH_LONG).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishAffinity();
                }
            }, 2000);
        }


    }
    }
