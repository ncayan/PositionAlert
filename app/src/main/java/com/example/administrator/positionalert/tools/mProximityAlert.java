package com.example.administrator.positionalert.tools;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.example.administrator.positionalert.model.AlertItem;
import com.example.administrator.positionalert.model.Globle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/1.
 */
public class mProximityAlert {

    private static Map<String, PendingIntent> Ring = new HashMap<>();

    private static LocationManager locationManager = null;

    public static void addProximityAlert(AlertItem alertItem,Context context) {
        String id = alertItem.getId();
        Intent intent = new Intent(Globle.ALERT_RECIVER);
        intent.putExtra("alertId",id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.getInteger(id), intent, 0);
        locationManager.addProximityAlert(alertItem.getLatitude(), alertItem.getLongitude(), (float) alertItem.getRange(), -1, pendingIntent);
        Ring.put(id, pendingIntent);
    }

    public static void removeProximityAlertById(String id) {
        boolean notFound = true;
        if (Ring.containsKey(id)) {
            notFound = false;
            locationManager.removeProximityAlert(Ring.get(id));
            Ring.remove(id);
        }
    }

    public static void refreshRing(Context context){
        AlertItem.refreshRing();
        Ring.clear();
        for(AlertItem alertItem:AlertItem.alertRing){
            addProximityAlert(alertItem,context);
        }
    }
}
