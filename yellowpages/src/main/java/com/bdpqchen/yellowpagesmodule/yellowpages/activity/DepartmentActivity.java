package com.bdpqchen.yellowpagesmodule.yellowpages.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.adapter.ListViewCategoryAdapter;
import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DatabaseClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscriber;

/**
 * Created by bdpqchen on 17-3-3.
 */

public class DepartmentActivity extends BaseActivity{

    private ListView mListView;
    private Toolbar mToolbar;
    private List<Phone> phoneList = null;

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
        Intent intent = getIntent();
        String toolbarName = intent.getStringExtra("toolbar_title");
        mListView = (ListView) findViewById(R.id.lv_unit);
        ListViewCategoryAdapter adapter = new ListViewCategoryAdapter(this, phoneList);
        mListView.setAdapter(adapter);
        Logger.i("receive intent" + intent.getStringExtra("toolbar_title"));
        mToolbar.setTitle(toolbarName);

        getDataList(toolbarName);

    }

    private void getDataList(String toolbarName) {
        Subscriber subscriber = new Subscriber<List<Phone>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Phone> phones) {
                phoneList = phones;
                ListViewCategoryAdapter.updateDataSet(phoneList);

            }
        };
        DatabaseClient.getInstance().getUnitListByDepartment(subscriber, toolbarName);

    }


}
