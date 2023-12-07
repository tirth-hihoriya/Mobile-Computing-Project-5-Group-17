package com.example.guardianangel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class PhoneCallReciever extends BroadcastReceiver {

//    EmotionAnalysis act=new EmotionAnalysis();
    static MediaRecorder myAudioRecorder;
    String url="http://98a8-34-125-55-206.ngrok-free.app/test";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (state != null) {
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    // Phone is ringing
                    String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    Log.d("PhoneCallReceiver", "Incoming call from: " + incomingNumber);
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    // Phone call has been answered (user attended the call)
                    Log.d("PhoneCallReceiver", "Phone call attended");

                    // Perform your desired action here
                    performActionOnCallAttend(context);
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    // Phone call has ended
                    Log.d("PhoneCallReceiver", "Phone call ended");
                    performActionOnCallEnd(context);
                }
            }
        }
    }

    private void performActionOnCallAttend(Context context) {
        Log.d("PhoneCallReceiver", "started action on call attend");

        startRecording(context);
    }
    private void performActionOnCallEnd(Context context){
        Log.d("PhoneCallReceiver", "started action on call attend");
        stopRecording(context);
    }

    public void startRecording(Context context) {
        System.out.println("recording has started");
        myAudioRecorder = new MediaRecorder();
//        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

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
//                file=new File(context.getFilesDir(),"recs/rec.wav");
                file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MUSIC), "/" + "angry_voice.wav");

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
                        String op=response.body().string();
                        System.out.println(op);
//                        EmotionAnalysis obj=new EmotionAnalysis();
//                        obj.startMain(op);
                        Intent intent=new Intent(context, EmotionAnalysis.class);
                        intent.putExtra("response",op+"");
                        intent.putExtra("flag",true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                });


            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }


}
