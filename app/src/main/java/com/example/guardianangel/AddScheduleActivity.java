package com.example.mobilecomp1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
//import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.model.DateTime;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddScheduleActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText dateTimeEditText;
    private Button appendButton;
    private TextView resultTextView;
    private EditText finishDate;
    private EditText editTextStartDate;
    private EditText editTextStartTime;
    private EditText editTextEndDate;
    private EditText editTextEndTime;
    //private EditText desEvent;
    private java.util.Calendar startDateTime = java.util.Calendar.getInstance();
    private java.util.Calendar endDateTime = java.util.Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addschedule);

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
        //Title
        titleEditText = findViewById(R.id.editTextTitle);
        //Start and End
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextStartTime = findViewById(R.id.editTextStartTime);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        editTextEndTime = findViewById(R.id.editTextEndTime);

        //dateTimeEditText = findViewById(R.id.editTextDateTime);
        appendButton = findViewById(R.id.buttonAppend);
        //finishDate = findViewById(R.id.EndDate);
        //desEvent = findViewById(R.id.DescriptionEditText);
        resultTextView = findViewById(R.id.textViewResult);

        java.util.Calendar startDateTime = java.util.Calendar.getInstance();
        java.util.Calendar endDateTime = java.util.Calendar.getInstance();
        appendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String title = titleEditText.getText().toString().trim();
                    if ("MAT 411".equals(title) || "CSE 535".equals(title)) {
                        addEventToICSFile();
                        getEvents(title);
                    } else {
                        Toast.makeText(AddScheduleActivity.this, "Please enter title as either MAT 411 or CSE 535", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(true);
            }
        });

        editTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(true);
            }
        });
        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(false);
            }
        });

        editTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(false);
            }
        });
    }
     private void addEventToICSFile() {
                try {
                    // Get user-inputted information
                    String eventTitle = titleEditText.getText().toString().trim();
                    String eventDateTime = editTextStartDate.getText().toString().trim()+"T"+editTextStartTime.getText().toString()+"Z";

                    String eventEndTime = editTextEndDate.getText().toString().trim()+"T"+editTextEndTime.getText().toString()+"Z";
                    Log.d("EventStart:",eventDateTime);
                    Log.d("Event end",eventEndTime);
                    //String eventDescription = String.valueOf(desEvent.getText());

                    //Log.d("Description: ",eventDescription);
                    // Read the existing ICS file
                    File icsFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "myfile.ics");
                    FileInputStream fis = new FileInputStream(icsFile);
                    InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    reader.close();

                    //20221231T210000Z
                    // Append the new event information
                    stringBuilder.append("BEGIN:VEVENT\n");
                    stringBuilder.append("SUMMARY:").append(eventTitle).append("\n");
                    stringBuilder.append("DTSTART:").append(eventDateTime).append("\n");
                    stringBuilder.append("DTEND:").append(eventEndTime).append("\n");
                    //stringBuilder.append("DESCRIPTION:").append(eventDescription).append("\n");
                    stringBuilder.append("END:VEVENT\n");

                    Log.d("ICS", stringBuilder.toString());


                    // Save the updated content back to the ICS file
                    FileOutputStream outputStream = new FileOutputStream(icsFile);
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                    writer.write(stringBuilder.toString());
                    writer.close();
                    outputStream.close();

                    Toast.makeText(this, "Event added to ICS file", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error adding event to ICS file", Toast.LENGTH_SHORT).show();
                }
            }



    private void getEvents(String keyword) {
        String fileName = "myfile.ics";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName;

        List<VEvent> events = getEventsFromIcsFile(path, keyword);


        if (events != null) {
            resultTextView.setText("");
            resultTextView.append("Event Successfully Added to calender\nAll Events with the Present title are:\n");
            for (VEvent event : events) {
                resultTextView.append("Event Title: " + event.getSummary().getValue()+"\n");
                resultTextView.append("Event Start Date: " + formatDate(event.getStartDate().getDate())+"\n");
                resultTextView.append("Event Start Time: " + formatTime(event.getStartDate().getDate())+"\n");
                resultTextView.append("Event End Date: " + formatDate(event.getEndDate().getDate())+"\n");
                resultTextView.append("Event End Time: " + formatTime(event.getEndDate().getDate())+"\n");
                //resultTextView.append("Event Description: " + event.getDescription().getValue()+"\n");
                resultTextView.append("----------------------------------------\n");
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
    private void showDatePicker(final boolean isStartDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (isStartDate) {
                            startDateTime.set(java.util.Calendar.YEAR, year);
                            startDateTime.set(java.util.Calendar.MONTH, monthOfYear);
                            startDateTime.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateStartDateEditText();
                        } else {
                            endDateTime.set(java.util.Calendar.YEAR, year);
                            endDateTime.set(java.util.Calendar.MONTH, monthOfYear);
                            endDateTime.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateEndDateEditText();
                        }
                    }
                },
                isStartDate ? startDateTime.get(java.util.Calendar.YEAR) : endDateTime.get(java.util.Calendar.YEAR),
                isStartDate ? startDateTime.get(java.util.Calendar.MONTH) : endDateTime.get(java.util.Calendar.MONTH),
                isStartDate ? startDateTime.get(java.util.Calendar.DAY_OF_MONTH) : endDateTime.get(java.util.Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showTimePicker(final boolean isStartTime) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (isStartTime) {
                            startDateTime.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
                            startDateTime.set(java.util.Calendar.MINUTE, minute);
                            updateStartTimeEditText();
                        } else {
                            endDateTime.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
                            endDateTime.set(java.util.Calendar.MINUTE, minute);
                            updateEndTimeEditText();
                        }
                    }
                },
                isStartTime ? startDateTime.get(java.util.Calendar.HOUR_OF_DAY) : endDateTime.get(java.util.Calendar.HOUR_OF_DAY),
                isStartTime ? startDateTime.get(java.util.Calendar.MINUTE) : endDateTime.get(java.util.Calendar.MINUTE),
                true
        );

        timePickerDialog.show();
    }

    private void updateStartDateEditText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        editTextStartDate.setText(dateFormat.format(startDateTime.getTime()));
    }

    private void updateEndDateEditText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        editTextEndDate.setText(dateFormat.format(endDateTime.getTime()));
    }

    private void updateStartTimeEditText() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss", Locale.US);
        editTextStartTime.setText(timeFormat.format(startDateTime.getTime()));
    }

    private void updateEndTimeEditText() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss", Locale.US);
        editTextEndTime.setText(timeFormat.format(endDateTime.getTime()));
    }

}
