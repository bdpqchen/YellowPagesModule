package com.bdpqchen.yellowpagesmodule.yellowpages.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.custom.DepartmentItemView;
import com.orhanobut.logger.Logger;

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
        Logger.i("activity is created.");
        DepartmentItemView departmentItemView = (DepartmentItemView) findViewById(R.id.custom_item_view);
//        departmentItemView
        departmentItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("you clicked the ll");
            }
        });


    }
}