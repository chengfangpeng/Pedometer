package com.cnwir.pedometer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cnwir.pedometer.domain.Step;
import com.cnwir.pedometer.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 对数据库pedometer里的各个表进行增删改查
 * <p/>
 * Created by heaven on 2015/7/23.
 */
public class PedometerDB {


    public static final String DB_NAME = "pedometer.db";// 数据库名称

    public static final int VERSION = 1;// 数据版本

    private static PedometerDB pedometerDB;

    private SQLiteDatabase db;

    /**
     * 将PedometerDB的构造方法设置为私有方法，在别的类里不能通过new来创建这个对象
     *
     * @param context
     */
    private PedometerDB(Context context) {
        PedomederSqliteHelper pHelper = new PedomederSqliteHelper(context, DB_NAME,
                null, VERSION);
        db = pHelper.getWritableDatabase();
    }

    /**
     * 使用单例模式创建数据库
     */
    public synchronized static PedometerDB getInstance(Context context) {
        if (pedometerDB == null) {
            pedometerDB = new PedometerDB(context);
        }
        return pedometerDB;
    }

    /**
     * 增加user表里的数据
     *
     * @param user
     */
    public void saveUser(User user) {
        if (user != null) {
            ContentValues values = new ContentValues();

            values.put("id", user.getId());
            values.put("username", user.getUsername());
            values.put("today_step_num", user.getToday_step_num());
            values.put("goal_step", user.getGoal());
            values.put("total_step_num", user.getTotal_step_num());
            values.put("sex", user.getSex());
            values.put("nickname", user.getNickname());
            values.put("orangekeyid", user.getOrangekeyId());
            values.put("orangekey", user.getOrangekey());
            values.put("token", user.getToken());

            db.insert("user", null, values);
        }
    }

    /**
     * 根据user的id删除user表里的数据
     *
     * @param user
     */
    public void deleteUser(User user) {
        if (user != null) {
            db.delete("user", "id = ?",
                    new String[]{user.getId() + ""});
        }
    }

    /**
     * 升级user表里的数据
     *
     * @param user
     */
    public void updateUser(User user) {
        if (user != null) {
            ContentValues values = new ContentValues();
            values.put("id", user.getId());
            values.put("username", user.getUsername());
            values.put("today_step_num", user.getToday_step_num());
            values.put("goal_step", user.getGoal());
            values.put("total_step_num", user.getTotal_step_num());
            values.put("sex", user.getSex());
            values.put("nickname", user.getNickname());
            values.put("orangekeyid", user.getOrangekeyId());
            values.put("orangekey", user.getOrangekey());
            values.put("token", user.getToken());
            db.update("user", values, "id = ?",
                    new String[]{user.getId() + ""});
        }
    }

    /**
     * 升级user表里的数据
     *
     * @param user
     */
    public void changeObjectId(User user) {
        if (user != null) {
            ContentValues values = new ContentValues();
            values.put("objectId", user.getId());
            db.update("user", values, null, null);
        }
    }

    /**
     * 增加step表里的数据
     *
     * @param step
     */
    public void saveStep(Step step) {
        if (step != null) {
            ContentValues values = new ContentValues();
            values.put("number", step.getNumber());
            values.put("date", step.getDate());
            values.put("userId", step.getUserId());
            db.insert("step", null, values);
        }
    }

    /**
     * 升级step表里的数据
     *
     * @param step
     */
    public void updateStep(Step step) {
        if (step != null) {
            ContentValues values = new ContentValues();
            values.put("number", step.getNumber());
            values.put("date", step.getDate());
            values.put("userId", step.getUserId());
            db.update("step", values, "userId = ? and date = ?", new String[]{
                    step.getUserId() + "", step.getDate()});
        }
    }

    /**
     * 升级step表里的数据
     *
     * @param step
     */
    public void changeuserId(Step step) {
        if (step != null) {
            ContentValues values = new ContentValues();
            // values.put("number", step.getNumber());
            // values.put("date", step.getDate());
            values.put("userId", step.getUserId());
            db.update("step", values, null, null);
        }
    }


    /**
     * 根据user表的userid和date来取数据
     *
     * @param id
     * @param date
     * @return
     */
    public Step loadSteps(int id, String date) {
        Step step = null;
        Cursor cursor = db.query("step", null, "userId = ? and date = ?",
                new String[]{id + "", date}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                step = new Step();
                step.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
                step.setDate(cursor.getString(cursor.getColumnIndex("date")));
                step.setId(id);
            } while (cursor.moveToNext());

        } else {
            Log.i("tag", "step is null!");
        }
        return step;
    }


    /**
     * 更具date取出所有的step数据
     *
     * @return
     */
    public List<Step> loadListSteps() {
        List<Step> list = new ArrayList<Step>();

        Cursor cursor = db.rawQuery("select * from step order by number desc",
                null);
        if (cursor.moveToFirst()) {
            do {
                Step step = new Step();
                step.setId(cursor.getInt(cursor.getColumnIndex("id")));
                step.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
                step.setDate(cursor.getString(cursor.getColumnIndex("date")));
                step.setId(cursor.getInt(cursor.getColumnIndex("id")));
                list.add(step);
            } while (cursor.moveToNext());

        }

        return list;
    }

    /**
     * 根据id取出user数据
     *
     * @param id
     * @return
     */
    public User loadUser(int id) {
        User user = null;
        Cursor cursor = db.query("user", null, "id = ?",
                new String[]{id + ""}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setId(id);
                user.setGoal(cursor.getInt(cursor.getColumnIndex("goal_step")));
                user.setToday_step_num(cursor.getInt(cursor.getColumnIndex("today_step_num")));
                user.setTotal_step_num(cursor.getInt(cursor.getColumnIndex("total_step_num")));
                user.setNickname(cursor.getString(cursor.getColumnIndex("nickname")));
                user.setOrangekeyId(cursor.getString(cursor.getColumnIndex("orangekeyid")));
                user.setOrangekey(cursor.getString(cursor.getColumnIndex("orangekey")));
                user.setSex(cursor.getString(cursor.getColumnIndex("sex")));
                user.setToken(cursor.getString(cursor.getColumnIndex("token")));
            } while (cursor.moveToNext());
        } else {
            Log.i("tag", "User is null!");
        }
        return user;
    }

    /**
     * 取出第一个用户，也就是用此app的用户
     *
     * @param id
     * @return
     */
    public User loadFirstUser() {
        User user = null;
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            user = new User();
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setGoal(cursor.getInt(cursor.getColumnIndex("goal_step")));
            user.setToday_step_num(cursor.getInt(cursor.getColumnIndex("today_step_num")));
            user.setTotal_step_num(cursor.getInt(cursor.getColumnIndex("total_step_num")));
            user.setNickname(cursor.getString(cursor.getColumnIndex("nickname")));
            user.setOrangekeyId(cursor.getString(cursor.getColumnIndex("orangekeyid")));
            user.setSex(cursor.getString(cursor.getColumnIndex("sex")));
            user.setToken(cursor.getString(cursor.getColumnIndex("token")));
        } else {
            Log.i("tag", "User is null!");
        }
        return user;
    }

}
