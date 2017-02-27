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

            showInitDialog();
            getDataList();

        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

    }

    private void showInitDialog() {
        mProgressDialog.setProgress(0);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("首次使用，需要导入号码库，请等待...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(100);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

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
                Logger.i(String.valueOf(dataBean.getData().size()));
//                Logger.i(dataBean.getData().get(0).getCategory_name());
                Logger.i(String.valueOf(dataBean.getData().get(0).getCategory_list().size()));
                Logger.i(String.valueOf(dataBean.getData().get(0).getCategory_list().get(0).getDepartment().size()));
                Logger.i(dataBean.getData().get(1).getCategory_list().get(0).getDepartment().get(0).getDepartment_list().get(0).getItem_name());
                initDatabase(dataBean);
            }

        };
        NetworkClient.getInstance().getDataList(subscriber);
        mProgressDialog.incrementProgressBy(10);
    }

    private void initDatabase(DataBean dataBean) {
        long time = System.currentTimeMillis();
        List<Phone> phoneList = new ArrayList<>();

        for (int i = 0; i < dataBean.getData().size(); i++) {
            DataBean.DataBeanList dataBeanLists = dataBean.getData().get(i);
            for (int j = 0; j < dataBeanLists.getCategory_list().size(); j++){
                DataBean.DataBeanList.CategoryListBean categoryList = dataBeanLists.getCategory_list().get(j);
                for (int k = 0; k < categoryList.getDepartment().size(); k++){
                    DataBean.DataBeanList.CategoryListBean.DepartmentBean departmentList = categoryList.getDepartment().get(k);
                    for (int l = 0; l < departmentList.getDepartment_list().size(); l++){
                        DataBean.DataBeanList.CategoryListBean.DepartmentBean.DepartmentListBean list = departmentList.getDepartment_list().get(l);
                        Phone phone = new Phone();
                        phone.setCategory(dataBeanLists.getCategory_name());
                        phone.setPhone(list.getItem_phone());
                        phone.setName(list.getItem_name());
                        phone.setIsCollected(0);
                        phone.setDepartment(departmentList.getDepartment_name());
                        phoneList.add(phone);
                    }
                }
            }
/*
            Phone phone = new Phone();
//            phone.setCategory(1);
            phone.setDepartment("部门/学院/其他名称");
            phone.setIsCollected(i);
            phone.setName("号码名称");
            phone.setPhone("232");
            phoneList.add(phone);
*/
        }
//        DataManager.insertPhone(phone);

        DataManager.insertBatch(phoneList);
        Logger.i(String.valueOf(phoneList.size()));
        Logger.i("DataList.size", phoneList.size() + "");
        Logger.i(String.valueOf(phoneList.get(0).getIsCollected()));
        Logger.i(String.valueOf(phoneList.get(1).getIsCollected()));
        Logger.d(System.currentTimeMillis() - time);



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
