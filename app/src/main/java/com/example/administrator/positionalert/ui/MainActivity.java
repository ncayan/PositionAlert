package com.example.administrator.positionalert.ui;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.positionalert.R;
import com.example.administrator.positionalert.model.AlertItem;
import com.example.administrator.positionalert.option.Globle;
import com.example.administrator.positionalert.option.MyApplication;
import com.example.administrator.positionalert.tools.AlertAdapter;
import com.example.administrator.positionalert.DB.AlertDBHelper;
import com.example.administrator.positionalert.service.alertLocationService;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //manager
    private LocationManager locationManager;
    //获取到的 provider名称
    private String provider;

    private Location location;
    //闹铃列表 listView
    private ListView alertList;
    //数据库操作类
    public static AlertDBHelper alertDBHelper;
    //存放所有闹铃实例
    private List<AlertItem> alertRing;
    // 临时显示位置变化
    TextView textView;

    private alertLocationService.AlertBinder alertBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            alertBinder = (alertLocationService.AlertBinder) service;
            alertBinder.refreshRing();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    AlertAdapter alertAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        alertDBHelper=new AlertDBHelper(this,"PositionAlert2.db",null,6);

        //闹铃类 刷新闹铃集合
        AlertItem.refreshRing();
        //重新初始化闹铃ListView
        alertListInit();

        textView=(TextView)findViewById(R.id.hellow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Looper looper = null;
                locationManager.requestSingleUpdate(provider, locationListener, looper);
                Toast.makeText(MainActivity.this, "当前位置" + location.getLatitude() + "+" + location.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        });

        //浮动按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent=new Intent(MainActivity.this, AddAlertActivity.class);
                startActivity(intent);

            }
        });
        FloatingActionButton startMonitor = (FloatingActionButton) findViewById(R.id.startMonitor);
        startMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent=new Intent(MainActivity.this,alertLocationService.class);
                startService(intent);

                bindService(intent, connection, BIND_AUTO_CREATE);

            }
        });
        FloatingActionButton stopMonitor = (FloatingActionButton) findViewById(R.id.stopMonitor);
        stopMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if (!isServiceWork(Globle.ALERT_SERVICE_NAME)){
                    Toast.makeText(MainActivity.this,"闹铃监控服务未开启",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(MainActivity.this,alertLocationService.class);
                unbindService(connection);
                stopService(intent);

            }
        });

        getCurrentPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        alertAdapter.notifyDataSetChanged();
    }

    void proximityAlert(){

    }

    /**
     * 初始化 闹铃列表
     */
    void alertListInit(){
        //填入临时假数据
        //alertRingInitTemp();
        alertRing = AlertItem.getAlertRing();

        alertList=(ListView)findViewById(R.id.alert_list);
        alertAdapter=new AlertAdapter(MainActivity.this,R.layout.alert_item_frag,alertRing);
        alertList.setAdapter(alertAdapter);
    }

    /**
     * 初始化 数据库操作类
     */
    void DBInit(){
        AlertDBHelper alertDBHelper=new AlertDBHelper(this,"PositionAlert2.db",null,1);
        alertDBHelper.getWritableDatabase();
    }

    /**
     * 在闹铃列表填入临时数据
     */
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

    /**
     * 创建菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 菜单响应方法
     * @param item
     * @return
     */
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

    /**
     * 获取当前位置
     */
    void getCurrentPosition(){
        //获取LocationManaget实例
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //获取可用provider列表
        List<String> providerList=locationManager.getProviders(true);

        if(providerList.contains(LocationManager.GPS_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;      //如果找到GPS provider
        }else if (providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider=LocationManager.NETWORK_PROVIDER;          ////如果找到网络 provider
        }else{
            //都没获取到的话
            Toast.makeText(this,"没找到合适的为之提供器",Toast.LENGTH_SHORT).show();
            textView.setText("没找到合适的为之提供器");
            return;
        }

        //获取最后一次获得的位置
        location=locationManager.getLastKnownLocation(provider);
        if(location!=null){
            //展示位置
            Toast.makeText(this, "当前位置" + location.getLatitude() + "+" + location.getLongitude(), Toast.LENGTH_SHORT).show();
            textView.setText("当前位置" + location.getLatitude() + "+" + location.getLongitude());

            Intent intent=new Intent(this,AddAlertActivity.class);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(this,-1,intent,0);

            locationManager.addProximityAlert(location.getLatitude(),location.getLongitude(),1f,-1,pendingIntent);

        }
        locationManager.requestLocationUpdates(provider,5000,1,locationListener);
    }


    /**
     * 匿名类
     * 监听位置变化
     */
    LocationListener locationListener=new LocationListener() {
        /**
         * 当位置变化时
         * @param location
         */
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(MainActivity.this,"刷新位置"+location.getLatitude()+"+"+location.getLongitude(),Toast.LENGTH_SHORT).show();
            textView.setText("刷新位置"+location.getLatitude()+"+"+location.getLongitude()+AutoAddress());
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

    /**
     * 将坐标解析为地址信息
     * @return
     */
    String AutoAddress(){
        String add="notFound";
        Geocoder geo=new Geocoder(MainActivity.this,Locale.getDefault());
        try {
            add=geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getAddressLine(0).toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return add;
    }

    /**
     * AC销毁时的相关处理
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }

    public void onAlertDBChanged(){
        alertRing = AlertItem.getAlertRing();
        alertAdapter.notifyDataSetChanged();

    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(String serviceName) {

        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) MyApplication.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
