package com.example.administrator.positionalert.tools;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.positionalert.MainActivity;
import com.example.administrator.positionalert.R;
import com.example.administrator.positionalert.model.AlertItem;
import com.example.administrator.positionalert.model.Globle;
import com.example.administrator.positionalert.ui.AddAlertActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/31.
 */
public class alertLocationService extends Service {
    //manager
    private static LocationManager locationManager;
    //获取到的 provider名称
    private String provider;

    private Location location;

    private static Map<String, PendingIntent> Ring = new HashMap<>();

    public AlertBinder alertBinder = new AlertBinder();

    public class AlertBinder extends Binder{
        public void refreshRing(){
            AlertItem.refreshRing();
            Ring.clear();
            for(AlertItem alertItem:AlertItem.alertRing){
                addProximityAlert(alertItem);
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return alertBinder;
    }

    /**
     * 获取当前位置
     */
    void getCurrentPosition() {
        //获取LocationManaget实例
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //获取可用provider列表
        List<String> providerList = locationManager.getProviders(true);

        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;      //如果找到GPS provider
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;          ////如果找到网络 provider
        } else {
            //都没获取到的话
            //Toast.makeText(this, "没找到合适的为之提供器", Toast.LENGTH_SHORT);
            //textView.setText("没找到合适的为之提供器");
            return;
        }

        //获取最后一次获得的位置
        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            //展示位置
            // Toast.makeText(MainActivity.this, "当前位置" + location.getLatitude() + "+" + location.getLongitude(), Toast.LENGTH_SHORT);
            // textView.setText("当前位置" + location.getLatitude() + "+" + location.getLongitude());

            /*Intent intent=new Intent(MainActivity.this,AddAlertActivity.class);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(this,-1,intent,0);
            locationManager.addProximityAlert(location.getLatitude(),location.getLongitude(),1f,-1,pendingIntent);*/

        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    /**
     * 匿名类
     * 监听位置变化
     */
    LocationListener locationListener = new LocationListener() {
        /**
         * 当位置变化时
         * @param location
         */
        @Override
        public void onLocationChanged(Location location) {
            //Toast.makeText(this,"刷新位置"+location.getLatitude()+"+"+location.getLongitude(),Toast.LENGTH_SHORT).show();
            // textView.setText("刷新位置"+location.getLatitude()+"+"+location.getLongitude()+AutoAddress());
            /*Intent intent=new Intent(MainActivity.this,AddAlertActivity.class);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(MainActivity.this,-1,intent,0);
            locationManager.addProximityAlert(location.getLatitude(), location.getLongitude(), 10f, -1, pendingIntent);*/
        }

        /**
         * 情景变化时？
         * 还没搞懂
         * @param provider
         * @param status
         * @param extras
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        /**
         * 位置provider不可时
         * @param provider
         */
        @Override
        public void onProviderEnabled(String provider) {

        }

        /**
         * 位置provider不可用时
         * @param provider
         */
        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Ring != null){
            Ring.clear();
        }
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //老的构造方法已经不推荐使用（话说这书有多老了）
        //Notification notification = new Notification(R.mipmap.ic_launcher,"Notification comes", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        //API 16 之后开始用Bulider 现在都 23 了是不是又换了？
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(Globle.appName)
                .setContentText(Globle.MONITOR_ONLINE)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build();
        //
        //notification.setLatestEventInfo(this, Globle.appName, Globle.MONITOR_ONLINE, pendingIntent);
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
       // mProximityAlert.refreshRing();
    }



    public void addProximityAlert(AlertItem alertItem) {
        String id = alertItem.getId();
        Intent intent = new Intent(Globle.ALERT_RECIVER);
        intent.putExtra("alertId",id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(alertLocationService.this, Integer.getInteger(id), intent, 0);
        if (ActivityCompat.checkSelfPermission(alertLocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
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

    public void refreshRing(){
        AlertItem.refreshRing();
        Ring.clear();
        for(AlertItem alertItem:AlertItem.alertRing){
            addProximityAlert(alertItem);
        }
    }



}
