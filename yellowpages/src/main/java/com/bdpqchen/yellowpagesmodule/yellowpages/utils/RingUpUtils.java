package com.bdpqchen.yellowpagesmodule.yellowpages.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;


/**
 * Created by bdpqchen on 17-3-3.
 */

public class RingUpUtils {

    public static void ringUp(Context context, String phoneNum){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);
    }

    public static void permissionCheck(Context context, String phoneNum, int requestCode) {
        Activity activity = (Activity) context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int checkCallPhonePermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                //When using the Support library, you have to use the correct method calls.
                //http://stackoverflow.com/questions/32714787/android-m-permissions-onrequestpermissionsresult-not-being-called
                activity.requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, requestCode);
            }else{
                ringUp(context, phoneNum);
            }
        }else{
            RingUpUtils.ringUp(context, phoneNum);
        }
    }
}
