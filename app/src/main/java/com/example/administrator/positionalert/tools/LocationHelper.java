package com.example.administrator.positionalert.tools;

import android.location.Geocoder;

import com.example.administrator.positionalert.option.MyApplication;

import java.util.Locale;

/**
 * Created by Administrator on 2016/4/7.
 */
public class LocationHelper {

    public static String getAddres(double longti,double lati){
        String add = "notfound";
        Geocoder geo=new Geocoder(MyApplication.getContext(), Locale.getDefault());
        try {
            add=geo.getFromLocation(lati,longti,1).get(0).toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return add;
        }
    }
}
