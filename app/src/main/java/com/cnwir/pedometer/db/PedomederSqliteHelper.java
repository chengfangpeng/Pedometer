package com.cnwir.pedometer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by heaven on 2015/7/23.
 */
public class PedomederSqliteHelper extends SQLiteOpenHelper {

/**
 *
 * 创建Step表
 *
 * */

    public static final String CREATE_STEP = "create table step("
            + "id integer primary key autoincrement,"
            + "number integer,"
            + "date text,"
            + "userid integer)";


    public static final String CREATE_USER = "create table user("
            + "id integer primary key autoincrement,"
            + "username text,"
            + "nickname text,"
            + "sex text,"
            + "token text,"
            + "orangekeyid text,"
            + "orangekey text,"
            + "today_step_num integer,"
            + "goal_step integer,"
            + "total_step_num integer)";




    public PedomederSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STEP);
        db.execSQL(CREATE_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
