package com.kgec.knowyourweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Find_City_Activity extends AppCompatActivity {
    private ImageView imageView;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__city_);

        imageView=findViewById(R.id.backButton);
        editText=findViewById(R.id.searchCity);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String cityName=editText.getText().toString();

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("cityName",cityName);
                startActivity(intent);


                return false;
            }
        });
    }
}