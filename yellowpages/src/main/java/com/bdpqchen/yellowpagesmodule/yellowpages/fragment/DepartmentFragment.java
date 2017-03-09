package com.bdpqchen.yellowpagesmodule.yellowpages.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.activity.DepartmentActivity;
import com.orhanobut.logger.Logger;


import static com.bdpqchen.yellowpagesmodule.yellowpages.activity.DepartmentActivity.INTENT_TOOLBAR_TITLE;


/**
 * Created by chen on 17-2-23.
 */

public class DepartmentFragment extends Fragment implements View.OnClickListener {

    private LinearLayout mDepartment1, mDepartment2, mDepartment3, mDepartment4, mDepartment5, mDepartment6;
    private TextView mTv1, mTv2, mTv3, mTv4, mTv5, mTv6;
    private String[] titles = new String[]{"1895综合服务大厅", "图书馆", "维修服务中心", "智能自行车", "学生宿舍管理中心", "天大医院"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.yp_fragment_department, container, false);
//        ButterKnife.inject(this, view);
        //the ButterKnife can not inject in a library. so, I had to add those fucking codes....
        //https://github.com/JakeWharton/butterknife/issues/100
        mTv1 = (TextView) view.findViewById(R.id.tv_title1);
        mTv2 = (TextView) view.findViewById(R.id.tv_title2);
        mTv3 = (TextView) view.findViewById(R.id.tv_title3);
        mTv4 = (TextView) view.findViewById(R.id.tv_title4);
        mTv5 = (TextView) view.findViewById(R.id.tv_title5);
        mTv6 = (TextView) view.findViewById(R.id.tv_title6);

        mDepartment1 = (LinearLayout) view.findViewById(R.id.department1);
        mDepartment2 = (LinearLayout) view.findViewById(R.id.department2);
        mDepartment3 = (LinearLayout) view.findViewById(R.id.department3);
        mDepartment4 = (LinearLayout) view.findViewById(R.id.department4);
        mDepartment5 = (LinearLayout) view.findViewById(R.id.department5);
        mDepartment6 = (LinearLayout) view.findViewById(R.id.department6);
        mDepartment1.setOnClickListener(this);
        mDepartment2.setOnClickListener(this);
        mDepartment3.setOnClickListener(this);
        mDepartment4.setOnClickListener(this);
        mDepartment5.setOnClickListener(this);
        mDepartment6.setOnClickListener(this);
        return view;
    }

    private void startActivityWithTitle(int i){
        String title = String.valueOf(titles[i - 1]);
        Intent intent = new Intent(getContext(), DepartmentActivity.class);
        intent.putExtra(INTENT_TOOLBAR_TITLE, title);
        Logger.i(title);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.department1){
            startActivityWithTitle(1);
        }else if (viewId == R.id.department2){
            startActivityWithTitle(2);
        }else if (viewId == R.id.department3){
            startActivityWithTitle(3);
        }else if (viewId == R.id.department4){
            startActivityWithTitle(4);
        }else if (viewId == R.id.department5){
            startActivityWithTitle(5);
        }else if (viewId == R.id.department6){
            startActivityWithTitle(6);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.reset(this);
    }

}
