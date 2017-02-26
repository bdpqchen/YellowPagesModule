package com.bdpqchen.yellowpagesmodule.yellowpages.data;

import com.bdpqchen.yellowpagesmodule.yellowpages.App;
import com.inst.greendao3_demo.dao.DaoMaster;
import com.inst.greendao3_demo.dao.DaoSession;

/**
 * Created by chen on 17-2-26.
 */
public class GreenDaoManager {
    private static GreenDaoManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private DaoMaster.DevOpenHelper devOpenHelper;


    public GreenDaoManager() {
        //创建一个数据库
        devOpenHelper = new DaoMaster.DevOpenHelper(App.getContext(), "yellow_pages.db", null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();

    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoManager();
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

    /**
     * 关闭数据连接
     */
    public void close() {
        if (devOpenHelper != null) {
            devOpenHelper.close();
        }
    }
}