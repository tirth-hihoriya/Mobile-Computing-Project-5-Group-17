package com.example.voicerecording;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneCallReciever extends BroadcastReceiver {


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
        MainActivity act=new MainActivity();
        act.startRecording();
    }
    private void performActionOnCallEnd(Context context){
        Log.d("PhoneCallReceiver", "started action on call attend");
        MainActivity act=new MainActivity();
        act.stopRecording();
    }

}
