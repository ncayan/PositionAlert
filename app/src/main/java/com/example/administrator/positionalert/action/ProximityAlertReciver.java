package com.example.administrator.positionalert.action;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.positionalert.MainActivity;
import com.example.administrator.positionalert.model.AlertItem;

/**
 * Created by Administrator on 2016/4/1.
 */
public class ProximityAlertReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        String alertId = intent.getStringExtra("alertId");
        if(!intent.getBooleanExtra(key,false)){
            return;
        }
        AlertItem.refreshRing();
        AlertItem alertItem = AlertItem.findByIdFromRing(alertId);
        if(alertItem == null){

        }else{
            Log.d("OUT", "onReceive: "+alertItem.getName());
        }
    }
}
