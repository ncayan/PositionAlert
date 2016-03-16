package com.example.administrator.positionalert;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.positionalert.model.AlertItem;
import com.example.administrator.positionalert.tools.AlertAdapter;
import com.example.administrator.positionalert.tools.AlertDBHelper;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private String provider;
    private Location location;
    private ListView alertList;
    public static AlertDBHelper alertDBHelper;
    private List<AlertItem> alertRing;

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        alertDBHelper=new AlertDBHelper(this,"PositionAlert.db",null,1);

        AlertItem.refreshRing();
        alertListInit();

        textView=(TextView)findViewById(R.id.hellow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Looper looper = null;
                locationManager.requestSingleUpdate(provider, locationListener, looper);
                Toast.makeText(MainActivity.this, "当前位置" + location.getLatitude() + "+" + location.getLongitude(), Toast.LENGTH_SHORT);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getCurrentPosition();
    }


    void alertListInit(){
        alertRingInitTemp();
        alertList=(ListView)findViewById(R.id.alert_list);
        AlertAdapter alertAdapter=new AlertAdapter(MainActivity.this,R.layout.alert_item_frag,alertRing);
        alertList.setAdapter(alertAdapter);
    }

    void DBInit(){
        AlertDBHelper alertDBHelper=new AlertDBHelper(this,"PositionAlert.db",null,1);
        alertDBHelper.getWritableDatabase();
    }


    void alertRingInitTemp() {
        AlertItem alertItem1 = new AlertItem();
        AlertItem alertItem2 = new AlertItem();
        AlertItem alertItem3 = new AlertItem();
        AlertItem alertItem4 = new AlertItem();
        AlertItem alertItem5 = new AlertItem();
        AlertItem alertItem6 = new AlertItem();
        AlertItem alertItem7 = new AlertItem();

        alertRing = AlertItem.alertRing;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            textView.setText("没找到合适的为之提供器");
            return;
        }

        location=locationManager.getLastKnownLocation(provider);
        if(location!=null){

            Toast.makeText(this,"当前位置"+location.getLatitude()+"+"+location.getLongitude(),Toast.LENGTH_SHORT);
            textView.setText("当前位置"+location.getLatitude()+"+"+location.getLongitude());
        }
        locationManager.requestLocationUpdates(provider,5000,1,locationListener);
    }

    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(MainActivity.this,"刷新位置"+location.getLatitude()+"+"+location.getLongitude(),Toast.LENGTH_SHORT);
            textView.setText("刷新位置"+location.getLatitude()+"+"+location.getLongitude()+AutoAddress());
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

    String AutoAddress(){
        String add="notFound";
        Geocoder geo=new Geocoder(MainActivity.this,Locale.getDefault());
        try {
            add=geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1).toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return add;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }
}
