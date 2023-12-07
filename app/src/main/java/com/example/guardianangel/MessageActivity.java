package com.example.guardianangel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagepage);
        final EditText messageSubjectEditText = findViewById(R.id.sendMessageSubjectEditText);
        final EditText messageEditText = findViewById(R.id.sendMessageEditText);
        Button sendMessageButton = findViewById(R.id.SendMesageButton);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder messageText = new StringBuilder("");
                messageText.append(messageSubjectEditText.getText().toString().trim());
                messageText.append(messageEditText.getText().toString().trim());
                String finalMessage = messageText.toString();
                /*if (!finalMessage.isEmpty()) {
                    // Create an intent to send a message
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"));
                    sendIntent.putExtra("sms_body", finalMessage);

                    // Check if there's an SMS app available to handle the intent
                    if (sendIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(sendIntent);
                    }

                        else {
                        Toast.makeText(MessageActivity.this, "No messaging app found", Toast.LENGTH_SHORT).show();
                    }
                }*/
                if (!finalMessage.isEmpty()) {
                    // Create an intent to send an email
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    //intent.addCategory(Intent.CATEGORY_APP_EMAIL);

                    emailIntent.setData(Uri.parse("mailto:aaravreddy06@gmail.com"));

                    // You can add more details to the email, such as subject and body
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, (CharSequence) messageSubjectEditText.getText().toString().trim());
                    emailIntent.putExtra(Intent.EXTRA_TEXT, (CharSequence) messageEditText.getText().toString().trim());

                    // Check if there's an email app available to handle the intent
                    if (emailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(emailIntent);
                    }
                    else {
                        Toast.makeText(MessageActivity.this, "No email app found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MessageActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

