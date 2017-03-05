package com.bdpqchen.yellowpagesmodule.yellowpages.data;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import com.bdpqchen.yellowpagesmodule.yellowpages.database.GreenDaoManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.History;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.SearchResult;
import com.daimajia.easing.quad.QuadEaseIn;
import com.inst.greendao3_demo.dao.HistoryDao;
import com.inst.greendao3_demo.dao.PhoneDao;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Printer;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 17-2-26.
 */

public class DataManager {

    private static PhoneDao mPhoneDao;
    private static HistoryDao mHistoryDao;
    public static final String TOO_MUCH_DATA_NAME = "点击查看全部结果";

    private static PhoneDao getPhoneDao() {
        if (mPhoneDao == null) {
            mPhoneDao = GreenDaoManager.getInstance().getDaoSession().getPhoneDao();
        }
        return mPhoneDao;
    }

    private static HistoryDao getHistoryDao() {
        if (mHistoryDao == null) {
            mHistoryDao = GreenDaoManager.getInstance().getDaoSession().getHistoryDao();
        }
        return mHistoryDao;
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
        List<Phone> list = getPhoneDao().queryBuilder().where(PhoneDao.Properties.Name.like("%" + name + "%")).list();
        return list;
    }

    public static List<Phone> getCollectedDataList() {

        return getPhoneDao().queryBuilder().where(PhoneDao.Properties.IsCollected.eq(1)).orderAsc(PhoneDao.Properties.Name).list();
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

    //动态搜索， 如果记录数太多只返回limit条
    //判断用户是否想要按号码搜索
    public static List<SearchResult> findSuggestions(String query, int limit) {

        WhereCondition whereCondition;
        if (isNumeric(query)){
            whereCondition = PhoneDao.Properties.Phone.like("%" + query + "%");
        }else{
            whereCondition = PhoneDao.Properties.Name.like("%" + query + "%");
        }
        List<Phone> phones = getPhoneDao().queryBuilder()
                .where(whereCondition)
                .limit(limit + 1).build().list();

        if (phones.size() > 0){
            List<SearchResult> results = new ArrayList<>();
            for (int i = 0; i < phones.size(); i++){
                Phone phone = phones.get(i);
                SearchResult result = new SearchResult();
                result.isCollected = phone.getIsCollected();
                result.name = phone.getName();
                result.phone = phone.getPhone();
                results.add(result);
            }
            if (phones.size() > limit ){
                SearchResult r = new SearchResult();
                r.name = TOO_MUCH_DATA_NAME;
                r.phone = "";
                results.add(r);
            }
            return results;
        }
        return null;
    }

    private static boolean isNumeric(String str){
        for(int i = str.length();--i >= 0; ){
            int chr = str.charAt(i);
            if(chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }

    public static List<SearchResult> findWord(String q, String lastQuery) {
        String query = q;
        WhereCondition conditionLike;
        if (query.equals(TOO_MUCH_DATA_NAME)){
            query = lastQuery;
        }
        if (isNumeric(query)){
            conditionLike = PhoneDao.Properties.Phone.like("%" + query + "%");
        }else{
            conditionLike = PhoneDao.Properties.Name.like("%" + query + "%");
        }
        List<Phone> phones = getPhoneDao().queryBuilder()
                .where(conditionLike)
                .build().list();
        int phonesSize = 0;
        if (null != phones && phones.size() > 0){
            phonesSize = phones.size();
        }
        setHistory(query, 0, phonesSize);
        if (null != phones && phones.size() > 0){
            List<SearchResult> results = new ArrayList<>();
            for (int i = 0; i < phones.size(); i++){
                Phone phone = phones.get(i);
                SearchResult result = new SearchResult();
                result.isCollected = phone.getIsCollected();
                result.name = phone.getName();
                result.phone = phone.getPhone();
                results.add(result);
            }
            return results;
        }
        return null;
    }

    public static List<History> getHistory(int count) {
        return getHistoryDao().queryBuilder()
                .where(HistoryDao.Properties.IsDeleted.eq(0))
                .limit(count)
                .list();
    }

    private static void setHistory(String text, int isReported, int totalOfFoundData){
        List<History> hasText = getHistoryDao().queryBuilder()
                .where(HistoryDao.Properties.Text.eq(text), HistoryDao.Properties.IsDeleted.eq(0)).list();
        if (null == hasText || hasText.size() == 0){
            History entity = new History();
            entity.setText(text);
            entity.setTotalOfFoundData(totalOfFoundData);
            entity.setIsReported(isReported);
            entity.setIsDeleted(0);
            getHistoryDao().insert(entity);
        }else{
            resetHistory(hasText);
        }
    }

    private static void resetHistory(List<History> hasText) {
        getHistoryDao().updateInTx(hasText);
    }

    public static void clearHistory(){
        List<History> histories = getHistoryDao().queryBuilder()
                .where(HistoryDao.Properties.IsDeleted.eq(0)).list();
        if (null != histories && histories.size() != 0){
            for (int i=0; i < histories.size(); i++){
                histories.get(i).setIsDeleted(1);
            }
            getHistoryDao().updateInTx(histories);
        }
    }


}

