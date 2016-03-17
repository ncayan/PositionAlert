package com.example.administrator.positionalert.model;

import com.example.administrator.positionalert.MainActivity;
import com.example.administrator.positionalert.tools.DBController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
public class AlertItem {
    public static List<AlertItem> alertRing = new ArrayList<AlertItem>();

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
        alertRing.add(this);
    }
    public AlertItem(Long id,String name,double latitude,double longitude,double range,boolean on){
        this.id=id.toString();
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.range=range;
        this.on=on;
        alertRing.add(this);
        DBController.DBInsert(MainActivity.alertDBHelper, this);
    }

    public AlertItem(String name,double latitude,double longitude,double range,boolean on){
        this.id="";
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.range=range;
        this.on=on;
        alertRing.add(this);
        DBController.DBInsert(MainActivity.alertDBHelper, this);
    }

    public static void clearRing(){
        alertRing.clear();
    }

    public static void refreshRing(){
        alertRing.clear();
        DBController.DBSearch(MainActivity.alertDBHelper);
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

    public String getId() {
        return id;
    }
}
