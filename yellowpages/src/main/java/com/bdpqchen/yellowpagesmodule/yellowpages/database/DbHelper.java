package com.bdpqchen.yellowpagesmodule.yellowpages.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.inst.greendao3_demo.dao.DaoMaster;

/**
 * Created by chen on 17-2-26.
 */

public class DbHelper extends DaoMaster.OpenHelper {

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }


    public DbHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

    }
}
