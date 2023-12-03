package com.example.guardianangel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.FFmpegSessionCompleteCallback;
import com.arthenica.ffmpegkit.Level;
import com.arthenica.ffmpegkit.ReturnCode;
import com.arthenica.ffmpegkit.Session;
import com.arthenica.ffmpegkit.SessionState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class HeartRateActivity extends AppCompatActivity {
    private static final int VIDEO_CAPTURE = 101;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    CameraWorking Work_Camera;
    String videoName = "Heart_Video.mp4";
    String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() ;
    String aviName = "heartRate.avi";
    private int fileId;
    String mpjegName = "heartRate.mjpeg";
//    DatabaseWorking dbHandler;
    String value;
    private Uri fileUri;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        configurePermissions();

        Work_Camera = new CameraWorking();
        Button measure = findViewById(R.id.heartRateBtn);

        if (!hasCamera()) {
            measure.setEnabled(false);
        }

        measure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start_recording_intent();
                progressDialog = new ProgressDialog(getApplicationContext());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog = ProgressDialog.show(HeartRateActivity.this, "Heart Rate", "The App Is Measuring You Heart Rate", false, false);
            }
        });
        Log.i("HeartRateActivity", "onCreate executed");

//        upload.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                TextView RespiRateView = findViewById(R.id.RespiRateValTextView);
//                dbHandler = new DatabaseWorking();
//                dbHandler.create_logging_database();
//                dbHandler.create_logging_table();
//
//                if (dbHandler.upload_logging_data(Integer.parseInt(value), "HeartRate")) {
//                    Toast.makeText(HeartRateActivity.this, "Data Uploaded", Toast.LENGTH_LONG).show();
//                    upload.setVisibility(View.INVISIBLE);
//                } else {
//                    Toast.makeText(HeartRateActivity.this, "Upload Failed", Toast.LENGTH_LONG).show();
//                    upload.setVisibility(View.VISIBLE);
//                }
//            }
//        });

    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void start_recording_intent() {
        int recordValue = 0;
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 45);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, VIDEO_CAPTURE);
        Log.i("HeartRateActivity", "start_recording_intent executed");
    }

    void configurePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , 10);
            return;
        }
    }


    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        Log.i("HeartRateActivity", "onActivityResult started");
        int ix;
        super.onActivityResult(requestCode, resultCode, data);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Log.i("HeartRateActivity", "onActivityResult permission not granted");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Log.i("HeartRateActivity", "onActivityResult Result OK");
                AssetFileDescriptor videoAsset = null;
                FileInputStream inputStream = null;
                OutputStream outputStream = null;
                File newFile;
                try {
                    videoAsset = getContentResolver().openAssetFileDescriptor(Objects.requireNonNull(data.getData()), "r");
                    Log.i("HeartRateActivity", "onActivityResult videoAsset");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    assert videoAsset != null;
                    inputStream = videoAsset.createInputStream();
                    Log.i("HeartRateActivity", "onActivityResult inputStream");
                } catch (IOException e) {
                    e.printStackTrace();
                }


                File dir = new File(folderPath);
                if (!dir.exists()) {
                    Log.i("HeartRateActivity", "Directory does not exist");
                    dir.mkdirs();
                }

                newFile = new File(dir, videoName);
                Log.i("HeartRateActivity", "onActivityResult dir.mkdirs");
                if (newFile.exists()) {
                    boolean temp = newFile.delete();
                    Log.i("HeartRateActivity", "onActivityResult delete");
                }

                try {
                    Toast.makeText(this, "Heart rate is 87", Toast.LENGTH_LONG).show();
                    outputStream = new FileOutputStream(newFile);
                    Log.i("HeartRateActivity", "onActivityResult outputstream");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                int[] ie = new int[1024];
                byte[] buf = new byte[1024];
                int len;

                while (true) {
                    try {
                        if (((len = inputStream.read(buf)) > 0)) {

                            outputStream.write(buf, 0, len);
                            Log.i("HeartRateActivity", "onActivityResult outputstream.write");
                        } else {
                            inputStream.close();
                            outputStream.close();
                            Log.i("HeartRateActivity", "onActivityResult input output stream close");
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                convertVideoCommands();

                Toast.makeText(this, "Captured video is saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording is cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Error: Video recording failed", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void convertVideoCommands() {
        Log.i("HeartRateActivity", "convertVideoCommands started");
        String cmd = "new";

//        FFmpeg ffmpeg = FFmpeg.getInstance(this);
//        try {
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//
//                @Override
//                public void onStart() {
//                }
//
//                @Override
//                public void onFailure() {
//                }
//
//                @Override
//                public void onSuccess() {
//                }
//
//                @Override
//                public void onFinish() {
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            Log.d("FFMPEG", "HI HERE YOU GOT THE ERROR 2");
//            throw new NullPointerException();
//
//        }

        File newfile = new File(folderPath + mpjegName);

        if (newfile.exists()) {
            newfile.delete();
        }

        FFmpegKitConfig.setLogLevel(Level.AV_LOG_INFO);

// Define your FFmpeg command
        String ffmpegCommand = "-i "+ folderPath + videoName+ " -vcodec"+ " mjpeg "+ folderPath + mpjegName;
        Log.i("HeartRateActivity", "convertVideoCommands ffmpeg command mjpeg");
        try {
            // Execute the FFmpeg command
            Session session = FFmpegKit.execute(ffmpegCommand);

            // Wait for the command to complete and get the result
            ReturnCode returnCode = session.getReturnCode();

            if (ReturnCode.isSuccess(returnCode)) {
                // The FFmpeg command was successful
                Log.i("HeartRateActivity", "convertVideoCommands mjpeg converted");
            } else {
                // Handle the failure case
                String errorOutput = session.getOutput();
                String errorLog = session.getAllLogsAsString();
                // You can log or display errorOutput and errorLog for debugging
            }

        } catch (Exception e) {
            // Handle exceptions, such as FFmpegCommandAlreadyRunningException
            Log.d("FFMPEG", "HI HERE YOU GOT THE ERROR");
            e.printStackTrace();
        }
//        try {
//            int status = -1;
//            ffmpeg.execute(new String[]{"-i", folderPath + videoName, "-vcodec", "mjpeg", folderPath + mpjegName}, new ExecuteBinaryResponseHandler() {
//
//                @Override
//                public void onStart() {
//                }
//
//                @Override
//                public void onProgress(String message) {
//
//                }
//
//                @Override
//                public void onFailure(String message) {
//                }
//
//                @Override
//                public void onSuccess(String message) {
//
//                }
//
//                @Override
//                public void onFinish() {
//
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            Log.d("FFMPEG", "HI HERE YOU GOT THE ERROR 1");
//            throw new NullPointerException();
//        }

        File avi_newfile = new File(folderPath + aviName);

        if (avi_newfile.exists()) {
            avi_newfile.delete();
        }

        FFmpegKitConfig.setLogLevel(Level.AV_LOG_INFO);

// Define your FFmpeg command

        String ffmpegCommand_2 = "-i "+ folderPath + mpjegName+ " -vcodec"+ " mjpeg "+ folderPath + aviName;
        Log.i("HeartRateActivity", "convertVideoCommands avi ffmpegCommand_2");

        try {
            // Execute the FFmpeg command
            FFmpegKit.executeAsync(ffmpegCommand_2, new FFmpegSessionCompleteCallback() {
                @Override
                public void apply(FFmpegSession session) {
                    Log.i("HeartRateActivity", "convertVideoCommands ffmpegCommand_2 executed");
                    SessionState state = session.getState();
                    ReturnCode returnCode = session.getReturnCode();
                    if (ReturnCode.isSuccess(returnCode)) {
                        Log.i("HeartRateActivity", "convertVideoCommands ReturnCode.isSuccess");
                        // The FFmpeg command was successful
                        // Perform additional tasks here
                        try {
                            String heart_rate = Work_Camera.measure_heart_rate(folderPath, aviName);
                            Log.i("HeartRateActivity", "convertVideoCommands heartrate "+heart_rate);
                            if (!Objects.equals(heart_rate, "")) {
                                Log.i("HeartRateActivity", "convertVideoCommands heartRate not null");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView textView = findViewById(R.id.heartRateValTextView);
                                        Button button = findViewById(R.id.heartRateBtn);
                                        value = heart_rate;
                                        textView.setText("HEART RATE IS: " + heart_rate + "\n");
                                        button.setText("MEASURE HEART RATE AGAIN");
                                        progressDialog.dismiss();
                                        Log.i("HeartRateActivity", "convertVideoCommands heartRate in run()");
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Handle the failure case
                        Log.e("FFMPEG", "FFmpeg command failed: " + session.getAllLogsAsString());
                    }
                }
            });

            // Handle the command's result in the onFinish() callback


        } catch (Exception e) {
            // Handle exceptions, such as FFmpegCommandAlreadyRunningException
            e.printStackTrace();
        }
//        try {
//            int status = 2;
//            ffmpeg.execute(new String[]{"-i", folderPath + mpjegName, "-vcodec", "mjpeg", folderPath + aviName}, new ExecuteBinaryResponseHandler() {
//
//                @Override
//                public void onStart() {
//
//                }
//
//                @Override
//                public void onProgress(String message) {
//
//                }
//
//                @Override
//                public void onFailure(String message) {
//
//                }
//
//                @Override
//                public void onSuccess(String message) {
//
//                }
//
//                @Override
//                public void onFinish() {
//                int status = 0;
//                    while (true) {
//
//                        try {
//                            String heart_rate = Work_Camera.measure_heart_rate(folderPath, aviName);
//                            if (!Objects.equals(heart_rate, "")) {
//                                TextView textView = findViewById(R.id.heartRateValTextView);
//                                Button button = findViewById(R.id.heartRateBtn);
//                                value = heart_rate;
//                                textView.setText("HEART RATE IS: " + heart_rate + "\n");
//                                button.setText("MEASURE HEART RATE AGAIN");
//                                progressDialog.dismiss();
//                                upload.setVisibility(View.VISIBLE);
//                                break;
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            e.printStackTrace();
//        }
    }



}