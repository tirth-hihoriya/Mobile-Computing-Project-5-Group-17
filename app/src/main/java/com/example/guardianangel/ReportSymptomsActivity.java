package com.example.guardianangel;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

//import static com.example.mobilecomp1.MainActivity.ROW;
public class ReportSymptomsActivity extends AppCompatActivity {
    private int hr; // Replace with actual HR value
    private int rr;
    private Button btnMailTAToRemedies,btnMailToProfessor;

    private TextView SymNameDispTextView;

    private static HashMap<String,Double> symOps= new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportsymptom);

        btnMailTAToRemedies = findViewById(R.id.btnMailTAToRemedies);
        btnMailToProfessor = findViewById(R.id.btnMailToProfessor);
        SymNameDispTextView = findViewById(R.id.SymptsNameRes);



        HeartRateResult InstanceofHR = new HeartRateResult();
        hr = InstanceofHR.HR;

        RespirationResult InstanceofRR = new RespirationResult();
        rr = InstanceofRR.RR;
        try {
            Resources resources = this.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.final_file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Split the CSV line by comma
                String[] values = line.split(",");

                // Assuming the first two columns are key and value
                if (values.length >= 2) {
                    String key = values[1].trim();
                    Double value = Double.valueOf(values[3].trim());
                    symOps.put(key, value);
                }
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Double valDrug = null;
        StringBuilder text = new StringBuilder("");
        for (HashMap.Entry<String, Double> entry : symOps.entrySet()) {
            Log.d("Key: " , entry.getKey());
            Log.d(", Value: " , entry.getValue().toString());
            String symptomsSelectedSymptomsActivity = getIntent().getStringExtra("selectedSymptom");
            String textviewRes = getIntent().getStringExtra("DrugName");
            if (entry.getKey().equals(symptomsSelectedSymptomsActivity)) {
                if (entry.getValue() == Double.valueOf(textviewRes)) {
                    text.append(entry.getKey() + " with " + entry.getValue() + " Rating \n");
                    valDrug= entry.getValue();
                }
            }

        }

        if (hr >= 60 && hr <= 100 && rr >= 12 && rr <= 20) {

            // Repeat this for all spinner items
            SymNameDispTextView.setText("The Symptoms you entered are :\n");
            // First clause: 3 or more spinner items are rated
            if (valDrug >= 0.2) {
                Log.d("val Count", String.valueOf(valDrug));
                SymNameDispTextView.append(text+"\n");
                btnMailToProfessor.setVisibility(View.VISIBLE);
            } else {
                SymNameDispTextView.append(text+"\n");
                    // Third clause: make btnMailTAToRemedies visible
                    btnMailTAToRemedies.setVisibility(View.VISIBLE);
                }
            }
         else {
            // HR or RR conditions not met, make btnMailToProfessor visible
            btnMailToProfessor.setVisibility(View.VISIBLE);
        }
    }

    public void onReportSymptomsMailTA(View view){
        Intent intent = new Intent(ReportSymptomsActivity.this, RemediesActivity.class);
        startActivity(intent);
    }
    public void onReportSymptomsMailPROF(View view){
        Intent intent = new Intent(ReportSymptomsActivity.this, MessageActivity.class);
        startActivity(intent);
    }
    /*private HashMap<String,Double> DatabaseOperations(){
        final HashMap<String,Double> symptomsMap= new HashMap<>();
        //DB Code
        helper = new DbUpload(this);

        // Replace ROW with your actual row ID
        /*Cursor rowcur = helper.getSymptomsDataOrderedByDescLimit1(); // Example row ID
        if (rowcur != null && rowcur.moveToFirst()) {
            do {
                for (int i = 0; i < rowcur.getColumnCount(); i++) {
                    ROW = rowcur.getInt(i);
                    Log.d("ROW", String.valueOf(ROW));
                }

        Retrieve Spinner values and ratings values from the database
        Cursor cursor = helper.getSymptomsData(ROW);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Iterate through the columns and retrieve values
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    double value = cursor.getDouble(i);
                    symptomsMap.put(columnName,value);
                    // Process the retrieved data as needed
                    // Example: Display data in a Toast
                    Log.d(String.valueOf(this), columnName + ": " + value);

                }
            } while (cursor.moveToNext());

            cursor.close();
        }
            //} while (rowcur.moveToNext());

       // }
       // rowcur.close();
        return symptomsMap;
    }*/
    public static HashMap<String, Double> getSymOps() {
        return symOps;
    }
}
