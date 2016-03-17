package com.example.administrator.positionalert.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/3/16.
 */
public class AlertDBHelper extends SQLiteOpenHelper {

    public static final String CREATE_ALERT = "create table alert ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "latitude real, "
            + "longitude real, "
            + "range real"
            + "ison integer)";

    private Context mContext;



    public AlertDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALERT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists alert");
        onCreate(db);
    }


}
