package com.bdpqchen.yellowpagesmodule.yellowpages.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.DataBean;
import com.bdpqchen.yellowpagesmodule.yellowpages.network.ApiOfYellowPages;
import com.bdpqchen.yellowpagesmodule.yellowpages.network.NetworkClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.PrefUtils;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by chen on 17-2-26.
 */

public class InitActivity extends BaseActivity {

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
        if(PrefUtils.isFirstOpen()){

            initDatabase();
            showInitDialog();
            getDataList()

        }else{
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

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                int i = 0;
                while (i < 100) {
                    try {
                        Thread.sleep(200);
                        // 更新进度条的进度,可以在子线程中更新进度条进度
                        mProgressDialog.incrementProgressBy(1);
                        // dialog.incrementSecondaryProgressBy(10)//二级进度条更新方式
                        i++;
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                // 在进度条走完时删除Dialog
                mProgressDialog.dismiss();
            }
        }).start();*/
    }

    private void initDatabase() {
    }


    public void getDataList() {

        Subscriber subscriber = new Subscriber<DataBean>() {
            @Override
            public void onCompleted() {
                mProgressDialog.incrementProgressBy(50);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DataBean dataBean) {

            }

        };
        NetworkClient.getInstance().getDataList(subscriber);
        mProgressDialog.incrementProgressBy(10);
    }
}
