package com.bdpqchen.yellowpagesmodule.yellowpages.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.DataBean;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.network.NetworkClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.PrefUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by chen on 17-2-26.
 */

public class InitActivity extends BaseActivity {

    public static final String CATEGORY_1 = "校级部门";
    public static final String CATEGORY_2 = "院级部门";
    public static final String CATEGORY_3 = "其他";

    private ProgressDialog mProgressDialog;

    @Override
    public int getLayout() {
        return R.layout.yp_activity_init;
    }

    @Override
    protected Toolbar getToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.yp_app_name));
        return toolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);


        // TODO: 17-2-26 加判断是否存在数据库
        if (PrefUtils.isFirstOpen()) {

            initDatabase();
            showInitDialog();
            getDataList();

        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }


    }

    private void showInitDialog() {
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("首次使用，需要导入号码库，请等待...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(100);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

    }

    private void initDatabase() {
        long time = System.currentTimeMillis();
        List<Phone> phoneList = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            Phone phone = new Phone();
            phone.setCategory(1);
            phone.setDepartment("部门/学院/其他名称");
            phone.setIsCollected(0);
            phone.setName("号码名称");
            phone.setPhone(String.valueOf(i));
            phoneList.add(phone);
        }

        DataManager.insertBatch(phoneList);

        Logger.d(System.currentTimeMillis() - time);
    }


    public void getDataList() {

        Subscriber subscriber = new Subscriber<DataBean>() {
            @Override
            public void onCompleted() {
//                DataManager.deleteAll();
                mProgressDialog.incrementProgressBy(50);
            }

            @Override
            public void onError(Throwable e) {
                mProgressDialog.dismiss();
                mProgressDialog.incrementProgressBy(0);
                showInitErrorDialog();
            }

            @Override
            public void onNext(DataBean dataBean) {
                Logger.i("onNext()");
                initDatabase();
            }

        };
        NetworkClient.getInstance().getDataList(subscriber);
        mProgressDialog.incrementProgressBy(10);
    }



    private void showInitErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("出错提醒")
                .setCancelable(false)
                .setMessage("\t导入失败，请检查网络是否可用")
                .setNegativeButton("重新导入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showInitDialog();
                        getDataList();
                    }
                })
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }
}
