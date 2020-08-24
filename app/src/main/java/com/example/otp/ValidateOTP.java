package com.example.otp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.RequestQueue;
import com.chaos.view.PinView;

public class ValidateOTP extends AppCompatActivity{
    RequestQueue requestQueue;
    PinView pinView;
    Button ValidateOTPBtn;
    String getContact, getOTP, otpStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_otp);
        pinView = findViewById(R.id.pinView);
        ValidateOTPBtn = findViewById(R.id.buttonValidate);
        Bundle bundle=getIntent().getExtras();
        getContact = bundle.getString("mobile");
        Log.w("contactValidate",getContact);
        getOTP = bundle.getString("otp");

        ValidateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            otpStr = pinView.getText().toString();
                Log.w("optstr","");
                Log.w("pinview","");

                if (getOTP.equals(otpStr)){
                    Toast.makeText(ValidateOTP.this, "OTP matched", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ValidateOTP.this, ResetPasswordActivity.class);
                    intent.putExtra("mobile",getContact);
                    startActivity(intent);
                }else {
                    Toast.makeText(ValidateOTP.this, "Otp not matched", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


}
