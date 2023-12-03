package com.example.guardianangel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button task1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task1 = findViewById(R.id.Task1);

        task1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task1();
            }
        });

    }

    private void Task1() {
        Intent intent1 = new Intent(this, Task1Main.class);
        startActivity(intent1);
    }
}