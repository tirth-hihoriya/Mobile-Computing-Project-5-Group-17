package com.example.mobilecomp1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import static com.example.mobilecomp1.MainActivity.ROW;
public class ReportSymptomsActivity extends AppCompatActivity {
    private int hr = 62; // Replace with actual HR value
    private int rr = 18;
    private Button btnMailTAToRemedies,btnMailToProfessor;
    private DbUpload helper;
    private TextView SymNameDispTextView;

    private static HashMap<String,Double> symOps= new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportsymptom);

        btnMailTAToRemedies = findViewById(R.id.btnMailTAToRemedies);
        btnMailToProfessor = findViewById(R.id.btnMailToProfessor);
        SymNameDispTextView = findViewById(R.id.SymptsNameRes);


        symOps = DatabaseOperations();

        //int stringCount = 0;
        int doubleCount = 0;
        int FourplusRatingCount= 0;
        StringBuilder text = new StringBuilder("");
        for (HashMap.Entry<String, Double> entry : symOps.entrySet()) {
            Log.d("Key: " , entry.getKey());
            Log.d(", Value: " , entry.getValue().toString());
            if (entry.getKey() != null && entry.getValue() != 0.0) {
                text.append(entry.getKey()+ " with " +entry.getValue()+ " Rating \n");
                doubleCount++;
                if (doubleCount <= 2 ) {
                    if(entry.getValue() >= 4.0){
                        FourplusRatingCount++;
                    }
                }
            }

        }

        if (hr >= 60 && hr <= 100 && rr >= 12 && rr <= 20) {

            // Repeat this for all spinner items
            SymNameDispTextView.setText("The Symptoms you entered are :\n");
            // First clause: 3 or more spinner items are rated
            if (doubleCount >= 3) {
                Log.d("Double Count", String.valueOf(doubleCount));
                SymNameDispTextView.append(text+"\n");
                btnMailToProfessor.setVisibility(View.VISIBLE);
            } else {
                Log.d("Double Count < 3", String.valueOf(doubleCount));
                SymNameDispTextView.append(text+"\n");

                if (FourplusRatingCount > 0.0) {
                    Log.d("FourRating cnt double cnt <3", String.valueOf(FourplusRatingCount));
                    btnMailToProfessor.setVisibility(View.VISIBLE);
                } else {
                    // Third clause: make btnMailTAToRemedies visible
                    btnMailTAToRemedies.setVisibility(View.VISIBLE);
                }
            }
        } else {
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
    private HashMap<String,Double> DatabaseOperations(){
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
*/
        // Retrieve Spinner values and ratings values from the database
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
    }
    public static HashMap<String, Double> getSymOps() {
        return symOps;
    }
}
