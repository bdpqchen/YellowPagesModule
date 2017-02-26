package com.bdpqchen.yellowpagesmodule.yellowpages.data;

import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.inst.greendao3_demo.dao.PhoneDao;

import java.util.List;

/**
 * Created by chen on 17-2-26.
 */

public class DataManager {

    private static PhoneDao mPhoneDao;

    public DataManager(){
//        mPhoneDao = GreenDaoManager.getInstance().getSession().getPhoneDao();
    }

    private static void initPhoneDao(){
        mPhoneDao = GreenDaoManager.getInstance().getSession().getPhoneDao();
    }

    public static void insertPhone(Phone phone){
        initPhoneDao();
        mPhoneDao.insert(phone);

    }

    public static void insertBatch(List<Phone> phoneList){
        initPhoneDao();
        mPhoneDao.insertInTx(phoneList);
    }

    public static void deleteAll(){
        initPhoneDao();
        mPhoneDao.deleteAll();

    }

}
