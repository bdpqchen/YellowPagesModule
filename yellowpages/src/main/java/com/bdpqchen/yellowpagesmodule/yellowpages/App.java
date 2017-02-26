package com.bdpqchen.yellowpagesmodule.yellowpages;

import android.app.Application;
import android.content.Context;

import com.bdpqchen.yellowpagesmodule.yellowpages.data.GreenDaoManager;
import com.inst.greendao3_demo.dao.DaoSession;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by chen on 17-2-21.
 */

public class App extends Application {

    private static Context mContext;
    private static DaoSession daoSession;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initDependencies();


    }

    private void initDependencies() {
        Logger.init("logger").hideThreadInfo().logLevel(LogLevel.FULL);
        Hawk.init(mContext).build();
        GreenDaoManager.getInstance();
    }

}
