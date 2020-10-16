package com.team10.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class questions extends AppCompatActivity {
    EditText emo_box;
    EditText reasons_box;
    EditText future_box;
    ListView q1_picker;
    LinkedList<String> values;
    CheckBox ch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Button answer_btn= (Button) findViewById(R.id.answer_btn);

        emo_box=(EditText) findViewById(R.id.emo_state);
        reasons_box=(EditText) findViewById(R.id.reasons);
        future_box=(EditText) findViewById(R.id.future);
        q1_picker = (ListView) findViewById(R.id.q1_list);
        ch = (CheckBox) findViewById(R.id.checkBox);
        String q_str = "no;not sure;yes";
        values=new LinkedList<> (Arrays.asList(q_str.split(";")));

        //Toast.makeText(this, values.getLast(), Toast.LENGTH_LONG).show();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, values);

        q1_picker.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        q1_picker.setAdapter(adapter);
        q1_picker.invalidateViews();


        answer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (ch.isChecked()) {
                   LinkedList<Double> values_db = new LinkedList<>();
                   values_db.add(0.0);
                   values_db.add(0.5);
                   values_db.add(1.0);
                   String q1 ="\"is_concern\": \""+String.valueOf(values_db.get(q1_picker.getCheckedItemPosition()))+"\"";
                   String reason = "\"reason\": \""+reasons_box.getText().toString()+"\"";
                   String emo_state =  "\"emo_state\": \""+String.valueOf(Double.parseDouble(emo_box.getText().toString())/10)+"\"";
                   String future =  "\"future\": \""+String.valueOf(Double.parseDouble(future_box.getText().toString())/10)+"\"";

                   String json_str =  "{\"user\": \""+MainActivity.username+"\", "+q1+", "+reason+", "+emo_state+", "+future+"}";
                   String api_url = "http://165.232.42.199:9000/api/v1.0/questions";
                   try {

                       post q = new post();

                       String result = q.sent(api_url,json_str);

                       Toast.makeText(questions.this, result, Toast.LENGTH_LONG).show();
                       startActivity(new Intent(getApplicationContext(), people.class));
                   } catch (Exception e){
                       Toast.makeText(questions.this, e.getMessage(), Toast.LENGTH_LONG).show();
                   }
                }

            }

        });
    }
}