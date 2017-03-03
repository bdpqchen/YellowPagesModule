package com.bdpqchen.yellowpagesmodule.yellowpages.data;

import android.support.annotation.Nullable;

import com.bdpqchen.yellowpagesmodule.yellowpages.database.GreenDaoManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.inst.greendao3_demo.dao.PhoneDao;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 17-2-26.
 */

public class DataManager {

    private static PhoneDao mPhoneDao;

    private static PhoneDao getPhoneDao() {
        if (mPhoneDao == null) {
            mPhoneDao = GreenDaoManager.getInstance().getDaoSession().getPhoneDao();
        }
        return mPhoneDao;
    }

    public static void insertPhone(Phone phone) {
        getPhoneDao().insert(phone);

    }

    public static void insertBatch(List<Phone> phoneList) {
        getPhoneDao().insertInTx(phoneList);
    }

    public static void deleteAll() {
        getPhoneDao().deleteAll();
    }

    public static List<Phone> limitQueryPhone(String name, int limit) {
        List<Phone> list = getPhoneDao().queryBuilder()
                .where(PhoneDao.Properties.Name.like("%" + name + "%"))
                .limit(limit)
                .list();
        return list;
    }

    public static List<Phone> fullQueryPhone(String name) {
        List<Phone> list = getPhoneDao().queryBuilder().where(PhoneDao.Properties.Name.like("$" + name + "%")).list();
        return list;
    }

    public static List<Phone> getCollectedDataList() {

        return getPhoneDao().queryBuilder().where(PhoneDao.Properties.IsCollected.eq(0)).orderAsc(PhoneDao.Properties.Name).list();
    }

    /* 按照类型分组查询
    * @param type 表示对应的分类，
    * 1--->校级
    * 2--->院级
    * 3--->其他*/
    public static String[][] getDepartmentsByCategory() {
        String groupBy = "1 GROUP BY department";
        int type = 0;
        List<Phone> phone0 = getPhoneDao().queryBuilder().where(PhoneDao.Properties.Category.eq(type))
                .where(new WhereCondition.StringCondition(groupBy)).list();
        String[] arr0 = new String[phone0.size()];
        for (int i = 0; i < phone0.size(); i++) {
            arr0[i] = (phone0.get(i).getDepartment());
        }
        type++;
        List<Phone> phone1 = getPhoneDao().queryBuilder().where(PhoneDao.Properties.Category.eq(type))
                .where(new WhereCondition.StringCondition(groupBy)).list();
        String[] arr1 = new String[phone1.size()];
        for (int i = 0; i < phone1.size(); i++) {
            arr1[i] = phone1.get(i).getDepartment();
        }
        type++;
        List<Phone> phone2 = getPhoneDao().queryBuilder().where(PhoneDao.Properties.Category.eq(type))
                .where(new WhereCondition.StringCondition(groupBy)).list();
        String[] arr2 = new String[phone2.size()];
        for (int i = 0; i < phone2.size(); i++) {
            arr2[i] = phone2.get(i).getDepartment();
        }
        String[][] results = {arr0, arr1, arr2};
        Logger.i(results[0][0]);
        Logger.i(groupBy);
        return results;
    }

    public static List<Phone> getUnitListByDepartment(String department) {
        return getPhoneDao().queryBuilder()
                .where(PhoneDao.Properties.Department.eq(department))
                .build()
                .list();
    }

    public static void updateCollectState(String name, String phone) {
        boolean isExistItem = false;
        List<Phone> result = getPhoneDao().queryBuilder()
                .where(PhoneDao.Properties.Name.eq(name)).build().list();
        if (null == result || result.size() == 0) {
            result = getPhoneDao().queryBuilder()
                    .where(PhoneDao.Properties.Phone.eq(phone)).list();
            if (result != null && result.size() > 0){
                isExistItem = true;
            }
        } else {
            isExistItem = true;
        }
        if (isExistItem){
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).getIsCollected() == 0) {
                    result.get(i).setIsCollected(1);
                } else {
                    result.get(i).setIsCollected(0);
                }
            }
            getPhoneDao().updateInTx(result);
        }

    }


}

