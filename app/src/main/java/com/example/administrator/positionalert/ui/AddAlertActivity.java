package com.example.administrator.positionalert.ui;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.positionalert.MainActivity;
import com.example.administrator.positionalert.R;
import com.example.administrator.positionalert.model.AlertItem;
import com.example.administrator.positionalert.tools.DBController;

import java.util.List;

/**
 * 创建新的地点闹铃的 Activity
 * Created by Administrator on 2016/3/16.
 */
public class AddAlertActivity extends Activity {

    TextView alertLongi;
    TextView alertLati;
    EditText alertName;
    EditText alertRange;
    Button addConfirm;

    LocationManager locationManager;
    Location location;
    String provider;

    double longi;
    double lati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);

        alertLati=(TextView)findViewById(R.id.add_alert_lat);
        alertLongi=(TextView)findViewById(R.id.add_alert_long);
        alertName=(EditText)findViewById(R.id.add_alert_name);
        alertRange=(EditText)findViewById(R.id.add_alert_range);
        addConfirm=(Button)findViewById(R.id.add_alert_addConfirm_bt);

        getCurrentPosition();
        addConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlert();
                finish();
            }
        });

        Toast.makeText(AddAlertActivity.this,"onCreateAddAC",Toast.LENGTH_SHORT).show();
        Log.d("add", "onCreate: addActivity");
        Log.d("add", "onCreate: addActivity");
        Log.d("add", "onCreate: addActivity");
    }

    /**
     * 保存新闹铃
     */
    void saveAlert(){
        if(alertName.getText().toString().equals("")||alertRange.getText().toString().equals("")){
            //如果名称和范围为空
            //不成功
        }else {
            String name = alertName.getText().toString();
            double range=Double.valueOf(alertRange.getText().toString());

            //根据输入 创建新闹铃实例
            AlertItem alertItem = new AlertItem(name,lati,longi,range,true);

            DBController.DBInsert(alertItem);
            //刷新 闹铃集合
            AlertItem.refreshRing();
        }
    }

    void getCurrentPosition(){
        //获取LocationManaget实例
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //获取可用provider列表
        List<String> providerList=locationManager.getProviders(true);

        if(providerList.contains(LocationManager.GPS_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;
        }else if (providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider=LocationManager.NETWORK_PROVIDER;
        }else{
            Toast.makeText(this,"没找到合适的为之提供器",Toast.LENGTH_SHORT);
            return;
        }

        location=locationManager.getLastKnownLocation(provider);
        if(location!=null){

            Toast.makeText(this, "当前位置" + location.getLatitude() + "+" + location.getLongitude(), Toast.LENGTH_SHORT);
            longi=location.getLongitude();
            lati=location.getLatitude();
            //textView.setText("当前位置"+location.getLatitude()+"+"+location.getLongitude());
        }

        //locationManager.requestLocationUpdates(provider,5000,1,locationListener);

        Looper looper = null;
        locationManager.requestSingleUpdate(provider, locationListener, looper);

        alertLongi.setText("" + longi);
        alertLati.setText(""+lati);
    }
    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Toast.makeText(AddAlertActivity.this, "刷新位置" + location.getLatitude() + "+" + location.getLongitude(), Toast.LENGTH_SHORT);
            //textView.setText("刷新位置"+location.getLatitude()+"+"+location.getLongitude()+AutoAddress());
            longi=location.getLongitude();
            lati=location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }
}
