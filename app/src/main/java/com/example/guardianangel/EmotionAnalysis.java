package com.example.guardianangel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class EmotionAnalysis extends AppCompatActivity {

    String url="http://5468-34-125-101-66.ngrok-free.app/test";
    MediaRecorder myAudioRecorder;
    String op;
    int flag=0;

    TextView emotionText;
    Button start,stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_analysis);

        int permissionsCode = 42;
        String[] permissions = new String[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        }

        ActivityCompat.requestPermissions(this, permissions, permissionsCode);

        start=findViewById(R.id.start);
        stop=findViewById(R.id.stop);


        Intent intent=getIntent();
        String resp=intent.getStringExtra("response");
        if(resp!=null && resp.equals("happy"))
            Toast.makeText(this, "User is happy, Nothing to do for now", Toast.LENGTH_SHORT).show();
        else if(resp!=null && resp.equals("Unable to detect, Please try again"))
            Toast.makeText(this, resp+"", Toast.LENGTH_SHORT).show();
        else if(resp!=null && resp.contains("Tunnel"))
            Toast.makeText(this, "Some error with server, Please check your internet connection", Toast.LENGTH_SHORT).show();
        else if(resp!=null && !resp.equals("Unable to detect, Please try again")){
            Toast.makeText(this, "The user seems to be "+resp, Toast.LENGTH_SHORT).show();
            String query="";
            if(resp.equals("angry"))
                query+="anger soothing songs";
            if(resp.equals("sad"))
                query+="Happy mood songs";
            if(resp.equals("fear"))
                query+="calming music";
//            String spotifyUri = "spotify:open";

            if(isSpotifyInstalled()){
                String spotifyUri = "spotify:search:"+query;

                // Create the intent with the Spotify URI
                Intent sample = new Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUri));
                sample.putExtra(
                        Intent.EXTRA_REFERRER,
                        Uri.parse("android-app://" + getPackageName())
                );
                // Start the activity
                this.finish();
                startActivity(sample);
            }
            else openSpotifyInPlayStore();

        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording(EmotionAnalysis.this);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording(EmotionAnalysis.this);
            }
        });
    }

    public void startRecording(Context context) {
        System.out.println("recording has started");
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        File f=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            f = new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_MUSIC),"/"+ "rec.wav");

//            File file = new File(EmotionAnalysis.this.getFilesDir(), "recs");

            File file = new File(context.getFilesDir(), "recs");
            if (!file.exists()) {
                file.mkdir();
            }
            f=new File(file,"rec.wav");

        }
        myAudioRecorder.setOutputFile(f.getAbsolutePath());
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopRecording(Context context){
        System.out.println("recording has ended");
        if(myAudioRecorder!=null)
            myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        UploadTask uploadTask=new UploadTask();
        uploadTask.uploadFile(context);
    }

    public class UploadTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        private void uploadFile(Context context) {
            File file = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                file = new File(Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_MUSIC), "/" + "rec.wav");

//                file=new File(EmotionAnalysis.this.getFilesDir(),"recs/rec.wav");
                file=new File(context.getFilesDir(),"recs/rec.wav");

            }
            try{
                RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("files",file.getName(),RequestBody.create(MediaType.parse("audio/vnd.wav"),file))
                        .addFormDataPart("some key","some value")
                        .addFormDataPart("submit","submit")
                        .build();
                okhttp3.Request request=new okhttp3.Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                OkHttpClient client=new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                        op=response.body().string();
                        System.out.println(op);
                        Intent intent=new Intent(EmotionAnalysis.this, EmotionAnalysis.class);
                        intent.putExtra("response",op+"");
//                        if(op.equals("angry"))
                        intent.putExtra("flag",true);
                        startActivity(intent);
                    }
                });


            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean isSpotifyInstalled(){
        boolean available=true;
        try {
            getPackageManager().getPackageInfo("com.spotify.music",PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            available = false;
        }
        return available;
    }
    private void openSpotifyInPlayStore() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.spotify.music")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.spotify.music")));
        }
    }

}