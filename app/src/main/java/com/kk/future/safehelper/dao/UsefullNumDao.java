package com.kk.future.safehelper.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kk.future.safehelper.bean.UsefullGroupBean;
import com.kk.future.safehelper.bean.UsefullGroupContextBean;

import java.util.ArrayList;


/**
 * Author: Future <br>
 * QQ: <br>
 * Description:用作常用号码的数据查询<br>
 * date: 2016/10/31  14:45.
 */
public class UsefullNumDao {
    private Context mContext;

    public UsefullNumDao(Context context) {
        this.mContext = context;
    }

    /**
     * @return 常用号码分类的组
     */
    public ArrayList<UsefullGroupBean> getGroups() {
        ArrayList<UsefullGroupBean> groupBeenList = new ArrayList<>();
        String path = mContext.getFilesDir() + "/usefullNum.db";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        String sql = "select  name,idx from classlist;";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            UsefullGroupBean bean = new UsefullGroupBean();
            bean.name = cursor.getString(0);
            bean.idx = cursor.getInt(1);
            groupBeenList.add(bean);
        }
        return groupBeenList;
    }

    /**
     * 查询分组的所有号码
     *
     * @param groupIdx 分组索引
     * @return ArrayList<UsefullGroupContextBean>集合
     */
    public ArrayList<UsefullGroupContextBean> getGroupContext(int groupIdx) {
        ArrayList<UsefullGroupContextBean> groupContextBeenList = new ArrayList<>();
        String path = mContext.getFilesDir() + "/usefullNum.db";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        String sql = "select number,name from table" + groupIdx + " ;";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            UsefullGroupContextBean bean = new UsefullGroupContextBean();
            bean.number = cursor.getString(0);
            bean.name = cursor.getString(1);
            groupContextBeenList.add(bean);
        }
        return groupContextBeenList;
    }


}
