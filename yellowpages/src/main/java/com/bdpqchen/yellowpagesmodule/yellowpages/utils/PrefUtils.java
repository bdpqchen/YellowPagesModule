package com.bdpqchen.yellowpagesmodule.yellowpages.utils;

import com.orhanobut.hawk.Hawk;

/**
 * Created by chen on 17-2-26.
 */

public class PrefUtils {


    private static final String IS_FIRST_OPEN = "is_first_open_then_init_the_database";
    private static final String DATABASE_VERSION_CODE = "database_version_code";

    public static boolean isFirstOpen(){
        return Hawk.get(IS_FIRST_OPEN, true);
    }
    public static void setIsFirstOpen(boolean b){
        Hawk.put(IS_FIRST_OPEN, b);
    }

    public static int getDataBaseVersionCode(){
        return Hawk.get(DATABASE_VERSION_CODE, 1);
    }

    public static void setVersionCode(int v){
        Hawk.put(DATABASE_VERSION_CODE, v);
    }



}
