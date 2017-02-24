package com.bdpqchen.yellowpagesmodule.yellowpages;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by chen on 17-2-21.
 */

public class App extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Logger.init("logger").hideThreadInfo().logLevel(LogLevel.FULL);



    }




}
