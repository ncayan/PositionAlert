package com.example.administrator.positionalert.tools;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.example.administrator.positionalert.option.MyApplication;
import com.example.administrator.positionalert.model.AlertItem;
import com.example.administrator.positionalert.option.Globle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/1.
 */
public class mProximityAlert {
    private static int AlertNum;

    static{
        AlertNum = 0;
    }

    private static Map<String, PendingIntent> Ring = new HashMap<>();

    private static LocationManager locationManager =(LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);

    public static void addProximityAlert(AlertItem alertItem,Context context) {
        String id = alertItem.getId();
        int intId =  Integer.parseInt(id);
        Intent intent = new Intent(Globle.ALERT_RECIVER);
        intent.putExtra("alertId",id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, intId, intent, 0);
        locationManager.addProximityAlert(alertItem.getLatitude(), alertItem.getLongitude(), (float) alertItem.getRange(), -1, pendingIntent);
        Ring.put(id, pendingIntent);
        AlertNum+=1;
    }

    public static void removeProximityAlertById(String id) {
        boolean notFound = true;
        if (Ring.containsKey(id)) {
            notFound = false;
            locationManager.removeProximityAlert(Ring.get(id));
            Ring.remove(id);
            AlertNum-=1;
        }
    }

    public static void refreshRing(Context context){
        AlertItem.refreshRing();
        Ring.clear();
        AlertNum = 0;
        for(AlertItem alertItem:AlertItem.alertRing){
            addProximityAlert(alertItem,context);
            AlertNum += 1;
        }
    }
}
