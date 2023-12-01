package com.example.mobilecomp1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class RetrieveScheduleActivity extends AppCompatActivity {
    private EditText TitleEntered;
    private Button ScheduleRetrieveButton;
    private TextView ResultTextView;
    private Button ReportSympButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrieveschedule);

        TitleEntered = findViewById(R.id.editTextTitle);
        ScheduleRetrieveButton = findViewById(R.id.RetrieveScheduleBtn);
        ResultTextView = findViewById(R.id.textViewResult);
        ReportSympButton = findViewById(R.id.ReportSymptomsBtn);

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
    public void onRetSchButtonClick(View view){
        ResultTextView.setText(" ");
        try {
            String title = TitleEntered.getText().toString().trim();
            if ("MAT 411".equals(title) || "CSE 535".equals(title)) {
                getEvents(title);
            } else {
                Toast.makeText(RetrieveScheduleActivity.this, "Please enter title as either MAT 411 or CSE 535", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideKeyboard();
        ReportSympButton.setVisibility(View.VISIBLE);
    }
    public void onReportSymptomsButtonClick(View view){
        Intent intent = new Intent(RetrieveScheduleActivity.this, ReportSymptomsActivity.class);
        //intent.putExtra("REPORT_TYPE", "Report2");
        startActivity(intent);
    }
    private void getEvents(String keyword) {
        String fileName = "myfile.ics";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName;

        List<VEvent> events = getEventsFromIcsFile(path, keyword);

        if (events != null) {
            ResultTextView.setText("");
            ResultTextView.append("All Events with the given title are:\n");
            for (VEvent event : events) {
                ResultTextView.append("Event Title: " + event.getSummary().getValue()+"\n");
                ResultTextView.append("Event Start Date: " + formatDate(event.getStartDate().getDate())+"\n");
                ResultTextView.append("Event Start Time: " + formatTime(event.getStartDate().getDate())+"\n");
                ResultTextView.append("Event End Date: " + formatDate(event.getEndDate().getDate())+"\n");
                ResultTextView.append("Event End Time: " + formatTime(event.getEndDate().getDate())+"\n");
                //resultTextView.append("Event Description: " + event.getDescription().getValue()+"\n");
                ResultTextView.append("----------------------------------------\n");
            }
        }
    }
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return dateFormat.format(date);
    }

    // Helper method to format time in a readable format
    private String formatTime(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        return timeFormat.format(date);
    }

    private List<VEvent> getEventsFromIcsFile(String filePath,String keyword) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(fis);
            List<Component> components = calendar.getComponents();
            List<VEvent> events = new ArrayList<>();
            System.setProperty("ical4j.parsing.relaxed", "true");
            System.setProperty("ical4j.validation.relaxed", "true");
            System.setProperty("ical4j.compatibility.outlook", "true");
            System.setProperty("ical4j.compatibility.notes", "true");
            System.setProperty("ical4j.unfolding.relaxed", "true");
            System.setProperty("net.fortuna.ical4j.timezone.update.enabled", "true");

            for (Component component : components) {
                if (component instanceof VEvent) {
                    VEvent event = (VEvent) component;
                    PropertyList properties = event.getProperties();
                    Summary summary = (Summary) properties.getProperty(Property.SUMMARY);
                    /*for (Iterator i = calendar.getComponents(Component.VEVENT).iterator(); i.hasNext();) {
                        VEvent vEvent = (VEvent) i.next();
                        Log.d("event", "Title: " + vEvent.getSummary().getValue());
                        Log.d("event", "Start Time: " + vEvent.getStartDate().getDate());
                        Log.d("event", "End Time: " + vEvent.getEndDate().getDate());
                        Log.d("event", "Time modified: " + vEvent.getDateStamp().getDate());
                        Log.d("event", "Desc: " + vEvent.getDescription().getValue());
                        // Add more log statements to print other event properties as needed
                    }*/
                    Uid uid = new Uid(UUID.randomUUID().toString());
                    event.getProperties().add(uid);
//
                    if (summary != null && summary.getValue().startsWith(keyword)) {
                        events.add(event);
                    }
                    /*for (Iterator i = calendar.getComponents(Component.VEVENT).iterator(); i.hasNext();) {
                        VEvent vEvent = (VEvent) i.next();
                        Log.d("event", "Title: " + vEvent.getSummary().getValue());
                        Log.d("event", "Start Time: " + vEvent.getStartDate().getDate());
                        Log.d("event", "End Time: " + vEvent.getEndDate().getDate());
                        Log.d("event", "Start Time: " + vEvent.getDateStamp().getDate());
                        Log.d("event", "Desc: " + vEvent.getDescription().getValue());
                        // Add more log statements to print other event properties as needed
                    }*/
                }
            }

            return events;
        } catch (IOException | ParserException e) {
            e.printStackTrace();
        }

        return null;
    }
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
