package com.bdpqchen.yellowpagesmodule.yellowpages.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.custom.DepartmentItemView;

/**
 * Created by bdpqchen on 17-3-7.
 */

public class CustomViewActivity extends BaseActivity{


    private Toolbar mToolbar;


    @Override
    public int getLayout() {
        return R.layout.yp_custom_activity;
    }


    @Override
    protected Toolbar getToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("自定义View");
        return mToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DepartmentItemView departmentItemView = (DepartmentItemView) findViewById(R.id.custom_item_view);
//        departmentItemView



    }
}
