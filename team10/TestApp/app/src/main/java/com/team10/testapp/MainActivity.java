package com.team10.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;




public class MainActivity extends AppCompatActivity {
    EditText password_box;
    EditText email_box;
    EditText confirm_box;

    public static String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        Button log_in_btn= (Button) findViewById(R.id.log_in);
        Button reg_btn= (Button) findViewById(R.id.register);

        password_box=(EditText) findViewById(R.id.password_box);
        email_box=(EditText) findViewById(R.id.email_box);
        confirm_box=(EditText) findViewById(R.id.confirm_box);

        log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json_str =  "{\"user\": \""+email_box.getText().toString()+"\", \"password\": \""+password_box.getText().toString()+"\"}";
                String api_url = "http://165.232.42.199:9000/api/v1.0/authorization";
                try {

                    post auth = new post();

                    String result = auth.sent(api_url,json_str);
                    if(result.equals("success")){
                        MainActivity.username = email_box.getText().toString();
                        startActivity(new Intent(getApplicationContext(), notes.class));
                    }
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password_box.getText().toString().equals(confirm_box.getText().toString())){
                    String json_str = "{\"user\": \""+email_box.getText().toString()+"\", \"password\": \""+password_box.getText().toString()+"\"}";
                    String api_url = "http://165.232.42.199:9000/api/v1.0/registration";

                    try {

                        post reg = new post();

                        String result = reg.sent(api_url,json_str);
                        if(result.equals("success")){
                            MainActivity.username = email_box.getText().toString();
                            startActivity(new Intent(getApplicationContext(), notes.class));
                        }

                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    } catch (Exception e){
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "Wrong password confirmation", Toast.LENGTH_LONG).show();
                }
            }

        });



    }





}