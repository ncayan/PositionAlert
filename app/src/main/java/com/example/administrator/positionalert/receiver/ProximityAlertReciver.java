package com.example.administrator.positionalert.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import com.example.administrator.positionalert.model.AlertItem;
import com.example.administrator.positionalert.option.MyApplication;
import com.example.administrator.positionalert.ui.AlertPenal;
import com.example.administrator.positionalert.ui.MainActivity;

import java.io.IOException;

/**
 * Created by Administrator on 2016/4/1.
 */
public class ProximityAlertReciver extends BroadcastReceiver {
    private static Context mContext = MyApplication.getContext();

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        zd();
        /*try {
            startAlarm();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        String alertId = intent.getStringExtra("alertId");
        Intent intent1 = new Intent(MyApplication.getContext(),AlertPenal.class);
        MyApplication.getContext().startActivity(intent1);
        if(!intent.getBooleanExtra(key,false)){
            return;
        }
        AlertItem.refreshRing();
        AlertItem alertItem = AlertItem.findByIdFromRing(alertId);
        if(alertItem == null){

        }else{

            Log.d("OUT", "onReceive: " + alertItem.getName());
        }
    }

    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(mContext,
                RingtoneManager.TYPE_RINGTONE);
    }

    private void startAlarm() throws IllegalStateException, IOException {
        MediaPlayer mMediaPlayer = MediaPlayer.create(mContext, getSystemDefultRingtoneUri());
        mMediaPlayer.setLooping(true);//设置循环
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    private void zd(){
        Vibrator vib = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(5000);
    }
}
