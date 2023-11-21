package com.example.guardianangel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Task1Main extends AppCompatActivity {

    Button buttonHeartRate, buttonRespiratoryRate, buttonSymptomLogging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task1_main);

        buttonHeartRate = findViewById(R.id.buttonHeartRate);
        buttonRespiratoryRate = findViewById(R.id.buttonRespiratoryRate);
        buttonSymptomLogging = findViewById(R.id.buttonSymptomLogging);

        buttonHeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHeartRateMonitor();
            }
        });

        buttonRespiratoryRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code for respiratory rate calculation goes here
            }
        });

        buttonSymptomLogging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code for symptom logging and diagnosis goes here
            }
        });
    }
    public void openHeartRateMonitor () {
        Intent intent1 = new Intent(this, HeartRateActivity.class);
        startActivity(intent1);
    }

}