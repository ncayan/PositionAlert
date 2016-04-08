package com.example.administrator.positionalert.tools;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.positionalert.ui.MainActivity;
import com.example.administrator.positionalert.option.MyApplication;
import com.example.administrator.positionalert.R;
import com.example.administrator.positionalert.option.Globle;

/**
 * Created by Administrator on 2016/4/7.
 */
public class AlertNotificationController {
    private static Notification notification;

    private static Context mContext = MyApplication.getContext();

    static {
        updateNotification(Globle.MONITOR_ONLINE);
    }

    public static Notification getNotification(){
        return notification;
    }

    public static void updateNotification(String text){
        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                notificationIntent, 0);
        notification = new Notification.Builder(mContext)
                .setAutoCancel(true)
                .setContentTitle(Globle.appName)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build();
    }

}
