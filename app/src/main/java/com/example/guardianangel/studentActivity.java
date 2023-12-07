package com.example.guardianangel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class studentActivity extends AppCompatActivity {
     private Button newScheduleButton;
     private Button RetschButton,RepsymBtn,RetschTitButton;
     private TextView RetrievefullSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentactivity);
        newScheduleButton= findViewById(R.id.button_add_schedule);
        RetschTitButton = findViewById(R.id.RetrieveScheduleforTitle);
        RepsymBtn = findViewById(R.id.ReportSymptoms);


        newScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(studentActivity.this, AddScheduleActivity.class));
            }
        });
        RetschTitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity((new Intent(studentActivity.this,RetrieveScheduleActivity.class)));
            }
        });
        RepsymBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentActivity.this, ReportSymptomsActivity.class);
                //intent.putExtra("REPORT_TYPE", "Report1");
                startActivity(intent);
            }
        });
        File sourceFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "myfile.ics");

        // Specify the destination directory
        File destinationDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "destination");

        // Check if the file already exists in the destination directory
        File destinationFile = new File(destinationDirectory, sourceFile.getName());

        if (destinationFile.exists()) {
            // File is already present
            Toast.makeText(this, "File is already present", Toast.LENGTH_SHORT).show();
        } else {
            // File is not present, so move it
            try {
                IcsFileUtils.moveIcsFileToStorage(this, R.raw.temp,"temp.ics");

                // Display a success message
                Toast.makeText(this, "File moved successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Handle the exception
                e.printStackTrace();
                Toast.makeText(this, "Error moving file", Toast.LENGTH_SHORT).show();
            }
        }

    }


}
