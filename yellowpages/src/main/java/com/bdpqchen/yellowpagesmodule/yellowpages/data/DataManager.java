package com.bdpqchen.yellowpagesmodule.yellowpages.data;

import com.bdpqchen.yellowpagesmodule.yellowpages.database.GreenDaoManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.inst.greendao3_demo.dao.PhoneDao;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * Created by chen on 17-2-26.
 */

public class DataManager {

    private static PhoneDao mPhoneDao;

    private static PhoneDao getPhoneDao(){
        if (mPhoneDao == null){
            mPhoneDao = GreenDaoManager.getInstance().getDaoSession().getPhoneDao();
        }
        return mPhoneDao;
    }

    public static void insertPhone(Phone phone){
        getPhoneDao().insert(phone);

    }

    public static void insertBatch(List<Phone> phoneList){
        getPhoneDao().insertInTx(phoneList);
    }

    public static void deleteAll(){
        getPhoneDao().deleteAll();
    }

    public static List<Phone> limitQueryPhone(String name, int limit){
        List<Phone> list = getPhoneDao().queryBuilder()
                .where(PhoneDao.Properties.Name.like("%" + name + "%"))
                .limit(limit)
                .list();
        return list;
    }

    public static List<Phone> fullQueryPhone(String name){
        List<Phone> list = getPhoneDao().queryBuilder().where(PhoneDao.Properties.Name.like("$" + name + "%")).list();
        return list;
    }

    public static List<Phone> getCollectedDataList() {
        return getPhoneDao().queryBuilder().where(PhoneDao.Properties.IsCollected.eq(1)).orderAsc(PhoneDao.Properties.Name).list();
    }

    /*
    * @param type 表示对应的分类，
    * 1--->校级
    * 2--->院级
    * 3--->其他*/
    @Deprecated
    public static List<Phone> getDataListByCategory(int type){
        return getPhoneDao().queryBuilder().where(PhoneDao.Properties.Category.eq(type)) .orderAsc(PhoneDao.Properties.Name).list();
    }

    public static List<Phone> getDepartmentsByCategory(int type){
        List<Phone> phones = getPhoneDao().queryBuilder().where(PhoneDao.Properties.Category.eq(type))
                .where(new WhereCondition.StringCondition("1 GROUP BY department")).list();
        return phones;
    }

 /*   public static List<Phone> getDataListByDepartment(String department){

    }*/

}
