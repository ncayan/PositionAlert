package com.example.administrator.positionalert.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.positionalert.MainActivity;
import com.example.administrator.positionalert.model.AlertItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/16.
 */
public class DBController {


    public static void DBInsert(AlertDBHelper alertDBHelper,Map<String,String> params){
        SQLiteDatabase db = alertDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
// 开始组装第一条数据

        values.put("name", params.get("name"));
        values.put("latitude", Double.parseDouble(params.get("latitude")));
        values.put("longitude", Double.parseDouble(params.get("longitude")));
        values.put("range", Double.parseDouble(params.get("range")));
        values.put("on", Integer.parseInt(params.get("on")));
        db.insert("Alert", null, values); // 插入第一条数据
        values.clear();
   }

    public static void DBInsert(AlertDBHelper alertDBHelper,AlertItem alertItem){
        SQLiteDatabase db = alertDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
// 开始组装第一条数据

        values.put("name", alertItem.getName());
        values.put("latitude", alertItem.getLatitude());
        values.put("longitude", alertItem.getLongitude());
        values.put("range", alertItem.getRange());
        values.put("ison", (alertItem.isOn() ? 1 : 0));
        try {
            db.insert("Alert", null, values); // 插入第一条数据
            values.clear();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DB:", "DBInsert: "+e.toString() );
        }
    }

    public static void DBUPdate(AlertDBHelper alertDBHelper,AlertItem alertItem){
        SQLiteDatabase db = alertDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String name=alertItem.getName();
        values.put("name", name);
        values.put("latitude", alertItem.getLatitude());
        values.put("longitude", alertItem.getLongitude());
        values.put("range", alertItem.getRange());
        values.put("on", (alertItem.isOn() ? 1 : 0));
        db.update("Alert", values, "name = ?", new String[]{name});
    }

    public static void DBDelete(AlertDBHelper alertDBHelper,AlertItem alertItem){
        SQLiteDatabase db = alertDBHelper.getWritableDatabase();
        String name=alertItem.getName();
        db.delete("Alert", "name = ?", new String[]{name});
    }

    public static void DBSearch(AlertDBHelper alertDBHelper){
        List<AlertItem> DBAlertRings=new ArrayList<AlertItem>();
        SQLiteDatabase db = alertDBHelper.getWritableDatabase();
        Cursor cursor=db.query("Alert",null,null,null,null,null,null);
        if(cursor.moveToNext()){
            do{
                Long id=cursor.getLong(cursor.getColumnIndex("id"));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                double latitude=cursor.getDouble(cursor.getColumnIndex("latitude"));
                double longitude=cursor.getDouble(cursor.getColumnIndex("longitude"));
                double range=cursor.getDouble(cursor.getColumnIndex("range"));
                int intOn=cursor.getInt(cursor.getColumnIndex("on"));
                boolean on=(intOn==1?true:false);
                AlertItem currentAlertItem=new AlertItem(id,name,latitude,longitude,range,on);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }


}
