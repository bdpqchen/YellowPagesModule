package com.bdpqchen.yellowpagesmodule.yellowpages.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.ListView;
import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.adapter.ListViewCategoryAdapter;
import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DatabaseClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CollectedFragmentCallBack;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.RingUpUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscriber;

/**
 * Created by bdpqchen on 17-3-3.
 */

public class DepartmentActivity extends BaseActivity implements CollectedFragmentCallBack {

    private static final int REQUEST_CODE_CALL_PHONE = 22;
    private ListView mListView;
    private Toolbar mToolbar;
    private List<Phone> phoneList = null;
    private CollectedFragmentCallBack mFragmentCallBack;
    private Context mContext;
    private String callPhoneNum;
    private ListViewCategoryAdapter mAdapter;
    @Override
    public int getLayout() {
        return R.layout.yp_activity_department;
    }

    @Override
    protected Toolbar getToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");

        return mToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mFragmentCallBack = this;
        Intent intent = getIntent();
        String toolbarName = intent.getStringExtra("toolbar_title");
        mListView = (ListView) findViewById(R.id.lv_unit);
        mListView.addFooterView(new ViewStub(this));
        mAdapter = new ListViewCategoryAdapter(this, phoneList, mFragmentCallBack);
        mListView.setAdapter(mAdapter);
        Logger.i("receive intent" + intent.getStringExtra("toolbar_title"));
        mToolbar.setTitle(toolbarName);

        getDataList(toolbarName);

    }

    @Override
    public void callPhone(String phoneNum) {
        this.callPhoneNum = phoneNum;
        RingUpUtils.permissionCheck(this, phoneNum, REQUEST_CODE_CALL_PHONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    RingUpUtils.ringUp(mContext, callPhoneNum);
                }else{
                    ToastUtils.show(this, "请在权限管理中开启微北洋拨打电话权限");
                }
                break;
        }
    }

    private void getDataList(String toolbarName) {
        Subscriber subscriber = new Subscriber<List<Phone>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Logger.i("onError()---->getDataList()");
            }

            @Override
            public void onNext(List<Phone> phones) {
                Logger.i("onNext()---->getDataList()");
                phoneList = phones;
                mAdapter.updateDataSet(phoneList);
                Logger.i(String.valueOf(phoneList.size()));

            }
        };
        DatabaseClient.getInstance().getUnitListByDepartment(subscriber, toolbarName);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
        /*if(keyCode == KeyEvent.KEYCODE_BACK){

        }
*/
    }
}