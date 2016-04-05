package com.example.administrator.positionalert.model;

import com.example.administrator.positionalert.MainActivity;
import com.example.administrator.positionalert.tools.AlertDBHelper;
import com.example.administrator.positionalert.tools.DBController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/16.
 */
public class AlertItem {


    public static List<AlertItem> alertRing = new ArrayList<AlertItem>();

    private static Map<String,AlertItem> Ring = new HashMap<String,AlertItem>();

    String id;
    String name;
    double latitude;//纬度
    double longitude;//经度
    double range;//范围半径
    boolean on;

    public AlertItem(){
        id=new Date().toString();
        name="未命名地点闹铃";
        latitude=0.00;
        longitude=0.00;
        range=0.00;
        on=true;
        //alertRing.add(this);
    }
    public AlertItem(Long id,String name,double latitude,double longitude,double range,boolean on){
        this.id=id.toString();
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.range=range;
        this.on=on;
        //alertRing.add(this);
        //DBController.DBInsert(MainActivity.alertDBHelper, this);
    }

    public AlertItem(String name,double latitude,double longitude,double range,boolean on){
        this.id="";
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.range=range;
        this.on=on;
        //alertRing.add(this);
        //DBController.DBInsert(MainActivity.alertDBHelper, this);
    }

    public static void clearRing(){
        alertRing.clear();
        Ring.clear();
    }

    public static void refreshRing(){
        //清空现有集合
        alertRing.clear();
        Ring.clear();
        //从数据库获取新集合
        DBController.DBSearch(MainActivity.alertDBHelper);

        Iterator it = Ring.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            alertRing.clear();
            alertRing.add(Ring.get(key));
        }
    }

    public static AlertItem findByIdFromRing(String id){
        AlertItem alertItem = null;
        if (Ring.containsKey(id)){
            alertItem = Ring.get(id);
        }
        return alertItem;
    }

    public static void addToRing(AlertItem alertItem){
        Ring.put(alertItem.getId(),alertItem);
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRange() {
        return range;
    }

    public boolean isOn() {
        return on;
    }

    public static Map<String, AlertItem> getRing() {
        return Ring;
    }

    public static List<AlertItem> getAlertRing() {
        Iterator it = Ring.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            alertRing.clear();
            alertRing.add(Ring.get(key));
        }
        return alertRing;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
