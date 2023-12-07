package com.example.guardianangel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button task1,task4,ButtonStudentSupport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task1 = findViewById(R.id.Task1);
        task4 = findViewById(R.id.Task4);
        ButtonStudentSupport = findViewById(R.id.buttonStudentAssistance);

        task1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task1();
            }
        });

        task4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task4();
            }
        });

        ButtonStudentSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStudentSupport();
            }
        });

    }

    private void openStudentSupport(){
        Intent StudIntent = new Intent(this, studentLoginActivity.class);
        startActivity(StudIntent);
    }

    private void Task1() {
        Intent intent1 = new Intent(this, Task1Main.class);
        startActivity(intent1);
    }

    private void Task4() {
        Intent intent1 = new Intent(this, EmotionAnalysis.class);
        startActivity(intent1);
    }
}