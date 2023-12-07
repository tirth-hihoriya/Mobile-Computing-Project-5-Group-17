package com.example.guardianangel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class studentLoginActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    //String usernamedb;
    //String passworddb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentlogin);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty()) {
                    editTextUsername.setError("Username is required");
                    editTextUsername.requestFocus();
                    return;
                }


                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                    return;
                }

                if (checkLogin(username, password)) {
                    // If the login was successful, start a new activity or display a success message
                    startActivity(new Intent(studentLoginActivity.this, studentActivity.class));
                } else {
                    // If the login was not successful, display an error message
                    Toast.makeText(studentLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
        private boolean checkLogin (String usernamedb, String passworddb) {
            // Read the CSV file from the raw directory
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.studentllogincred)));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    // If the username and password match, return true
                    if (data[0].equals(usernamedb) && data[1].equals(passworddb)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // If the username and password were not found in the CSV file, return false
            return false;
        }


}
