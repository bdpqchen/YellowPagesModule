package com.bdpqchen.yellowpagesmodule.yellowpages.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.custom.DepartmentItemView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by chen on 17-2-23.
 */

public class DepartmentFragment extends Fragment implements View.OnClickListener {

    private DepartmentItemView mDepartment1, mDepartment2, mDepartment3, mDepartment4, mDepartment5, mDepartment6;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.yp_fragment_department, container, false);
//        ButterKnife.inject(this, view);
        //the ButterKnife can not inject in a library. so, I had to add those fucking codes....
        //https://github.com/JakeWharton/butterknife/issues/100
        mDepartment1 = (DepartmentItemView) view.findViewById(R.id.department1);
        mDepartment2 = (DepartmentItemView) view.findViewById(R.id.department2);
        mDepartment3 = (DepartmentItemView) view.findViewById(R.id.department3);
        mDepartment4 = (DepartmentItemView) view.findViewById(R.id.department4);
        mDepartment5 = (DepartmentItemView) view.findViewById(R.id.department5);
        mDepartment6 = (DepartmentItemView) view.findViewById(R.id.department6);



        ButterKnife.inject(this, view);
        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.reset(this);
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {

    }

}
