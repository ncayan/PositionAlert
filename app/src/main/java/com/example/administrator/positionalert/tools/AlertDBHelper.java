package com.example.administrator.positionalert.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/3/16.
 */
public class AlertDBHelper extends SQLiteOpenHelper {

    /**
     * 创建 ALERT 表语句
     */
    public static final String CREATE_ALERT = "create table alert2 ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "latitude real, "
            + "longitude real, "
            + "range real,"
            + "ison integer)";

    private Context mContext;


    /**
     * 构造方法
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public AlertDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    /**
     * 创建数据库操作
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALERT);
    }

    /**
     * 更改数据库的操作
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists alert");
        db.execSQL("drop table if exists alert2");
        onCreate(db);
    }


}
