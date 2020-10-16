package com.team10.testapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class notes extends AppCompatActivity {

    ListView notes_picker;
    LinkedList<String> values;
    public static String text = "";
    public static String theme = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_notes);
        notes_picker=(ListView) findViewById(R.id.notes_view);
        update_list();
    }


    String get_text(){
        String theme = values.get(notes_picker.getCheckedItemPosition());

        String json_str =  "{\"user\": \""+MainActivity.username+"\", \"theme\": \""+theme+"\"}";
        String api_url = "http://165.232.42.199:9000/api/v1.0/get_text";
        try {

            post note = new post();


            return note.sent(api_url,json_str);
        } catch (Exception e){
            Toast.makeText(notes.this, e.getMessage(), Toast.LENGTH_LONG).show();
            return "connection error";
        }
    }

    void update_list(){
        String json_str =  "{\"user\": \""+MainActivity.username+"\"}";
        String api_url = "http://165.232.42.199:9000/api/v1.0/get_themes";
        try {

            post themes = new post();

            String result = themes.sent(api_url,json_str);
            values=new LinkedList<> (Arrays.asList(result.split(";")));
            Collections.reverse(values);
            //Toast.makeText(this, values.getLast(), Toast.LENGTH_LONG).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_single_choice, values);

            notes_picker.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            notes_picker.setAdapter(adapter);
            notes_picker.invalidateViews();

        } catch (Exception e){
            Toast.makeText(notes.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        update_list();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.add:
                notes.text = "";
                notes.theme = "";
                startActivity(new Intent(getApplicationContext(), add_note.class));

                return true;

            case R.id.select:
                notes.text = get_text();
                notes.theme = values.get(notes_picker.getCheckedItemPosition());
                startActivity(new Intent(getApplicationContext(), add_note.class));

                return true;
            case R.id.group:

                startActivity(new Intent(getApplicationContext(), questions.class));

                return true;

            case R.id.people:

                startActivity(new Intent(getApplicationContext(), people.class));

                return true;




        }

        return super.onOptionsItemSelected(item);
    }




}