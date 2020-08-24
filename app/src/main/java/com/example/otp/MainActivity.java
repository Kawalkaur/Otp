package com.example.otp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText etContact;
    String Contact;
    Button Next;
    int otp;
    String NAME, MOBILE;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etContact = findViewById(R.id.contact);
        Next = findViewById(R.id.nextButton);
        requestQueue = Volley.newRequestQueue(this);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact = etContact.getText().toString();
                if ((!Contact.equals(""))){
                    TelephonyManager telemanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Permission Required")
                                .setMessage("SMS And Phone Permission Required to get registered. Do You want to allow");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        builder.show();
                    } else {
                      //  String getSerialNumber = "ba0c3b3b";
                       if(!Contact.matches("[0-9]{10}"))
                        {
                            Toast.makeText(MainActivity.this, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                         }
                        else forgetPassowrd();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "All Fields Are Mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void EgenerateOTP(){
        try {
            otp= generateOTP();
        } catch (Exception e) {
            otp=127856;
        }

        if ((!Contact.equals(""))){
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(etContact.getText().toString(), null, "OTP to get register with Bong Basket Is "+otp, null, null);
                Intent intent=new Intent(MainActivity.this,ValidateOTP.class);
                intent.putExtra("mobile",Contact);
                Log.w("contactMain",Contact);
                intent.putExtra("otp",String.valueOf(otp));
                Toast.makeText(this, "OTP Sent to your mobile number", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "MSG Can not sent Check Your Balance", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(this, "All Fields Are Mandatory", Toast.LENGTH_SHORT).show();
        }
    }
    public void forgetPassowrd() {

    GetContactFromServer();

    }

    public void GetContactFromServer(){
        StringRequest request = new StringRequest(Request.Method.POST, Util.Contacts, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("res",""+response);

                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("Student");
                    String message=object.getString("Message");

                    if (message.contains("Successful")){

                        for (int i=0;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i) ;
                            NAME = object1.getString("Name");
                          //  EMAIL = object1.getString("EMAIL");
                        MOBILE = object1.getString("Contact");
                           // ADDRESS = object1.getString("ADDRESS");

                        }
                           EgenerateOTP();
                        Intent i = new Intent(MainActivity.this, ValidateOTP.class);
                        startActivity(i);
                        Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    }

                }

                catch (JSONException e)
                {
                    Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Volley error", Toast.LENGTH_SHORT).show();

            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map=new HashMap<>();
                map.put("EditContact", etContact.getText().toString());
                return map;
            }
        };

        requestQueue.add(request);
    }

    public int generateOTP() throws Exception {
        Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());

        int num = generator.nextInt(99999) + 99999;
        if (num < 100000 || num > 999999) {
            num = generator.nextInt(99999) + 99999;
            if (num < 100000 || num > 999999) {
                throw new Exception("BONG123");
            }
        }
        return num;
    }

}
