package com.team10.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class people extends AppCompatActivity {

    ListView people_list;
    LinkedList<String> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        people_list=(ListView) findViewById(R.id.people_list);
        update_list();
    }

    void update_list(){
        String json_str =  "{\"user\": \""+MainActivity.username+"\"}";
        String api_url = "http://165.232.42.199:9000/api/v1.0/analysis";
        try {

            post themes = new post();

            String result = themes.sent(api_url,json_str);

            values=new LinkedList<>(Arrays.asList(result.split(";")));

            //Collections.reverse(values);
            //Toast.makeText(this, values.getLast(), Toast.LENGTH_LONG).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_single_choice, values);

            people_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            people_list.setAdapter(adapter);
            people_list.invalidateViews();

        } catch (Exception e){
            Toast.makeText(people.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

        getMenuInflater().inflate(R.menu.menu_group, menu);

        return true;
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {



            case R.id.select:
                String mail = values.get(people_list.getCheckedItemPosition());
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("mailto:"+mail)));



                return true;





        }

        return super.onOptionsItemSelected(item);
    }
}
