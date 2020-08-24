package com.example.otp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText Passowrd1, Passowrd2;
    Button OK;
    RequestQueue requestQueue;
    String getContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Passowrd1 = findViewById(R.id.password1);
        Passowrd2 = findViewById(R.id.password2);
        Bundle bundle=getIntent().getExtras();
        getContact = bundle.getString("mobile");
        Log.w("contactReset",getContact);
        requestQueue = Volley.newRequestQueue(this);
        OK = findViewById(R.id.buttonOK);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordReset();
            }
        });

    }

     void PasswordReset() {

         StringRequest request = new StringRequest(Request.Method.POST, Util.PASSWORD, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 Log.w("res",response);

                 try{
                     JSONObject object = new JSONObject(response);
                     String message = object.getString("Message");
                     if (message.contains("Password Updated")) {

                         Toast.makeText(ResetPasswordActivity.this, "Password changed Successfully.",
                                 Toast.LENGTH_SHORT).show();
                         Intent intent=new Intent(ResetPasswordActivity.this,MainActivity.class);

                         startActivity(intent);
                         finish();



                     } else {

                         Toast.makeText(ResetPasswordActivity.this, "password not matched", Toast.LENGTH_SHORT).show();
                     }

                 }

                 catch (Exception e)
                 {

                     Toast.makeText(ResetPasswordActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();



                 }

             }
         }, new Response.ErrorListener() {


             @Override
             public void onErrorResponse(VolleyError error) {

             }
         })
         {
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> map = new HashMap<>();
                 map.put("NewPassword",Passowrd1.getText().toString());
                 map.put("ConfirmPassword",Passowrd2.getText().toString());
                 map.put("GetContact",getContact);
                 return map;
             }
         };
         requestQueue.add(request);
    }
}
