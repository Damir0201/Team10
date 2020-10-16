package com.team10.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class add_note extends AppCompatActivity {
    EditText theme_box;
    EditText text_box;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Button add_btn= (Button) findViewById(R.id.add_btn);

        theme_box=(EditText) findViewById(R.id.theme_box);
        text_box=(EditText) findViewById(R.id.note_text);
        text_box.setText(notes.text);
        theme_box.setText(notes.theme);
        if (!notes.text.equals("")) {
            theme_box.setEnabled(false);
            add_btn.setVisibility(View.INVISIBLE);
        }
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theme = theme_box.getText().toString();
                String text = text_box.getText().toString();
                String json_str =  "{\"user\": \""+MainActivity.username+"\", \"theme\": \""+theme+"\", \"text\": \""+text+"\"}";
                String api_url = "http://165.232.42.199:9000/api/v1.0/add_note";
                try {

                    post auth = new post();

                    String result = auth.sent(api_url,json_str);

                    Toast.makeText(add_note.this, result, Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    Toast.makeText(add_note.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

        });
    }


}