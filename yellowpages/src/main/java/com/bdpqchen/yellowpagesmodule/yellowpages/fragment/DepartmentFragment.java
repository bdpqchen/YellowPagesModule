package com.bdpqchen.yellowpagesmodule.yellowpages.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.activity.DepartmentActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.custom.DepartmentItemView;
import com.orhanobut.logger.Logger;


import static com.bdpqchen.yellowpagesmodule.yellowpages.activity.DepartmentActivity.INTENT_TOOLBAR_TITLE;


/**
 * Created by chen on 17-2-23.
 */

public class DepartmentFragment extends Fragment implements DepartmentItemView.OnClickListener{

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
//        mDepartment1.setOnClickListener(this);
        mDepartment1.setDepartmentOnClickListener(this);

                /*
        mDepartment2.setOnClickListener(this);
        mDepartment3.setOnClickListener(this);
        mDepartment4.setOnClickListener(this);
        mDepartment5.setOnClickListener(this);
        mDepartment6.setOnClickListener(this);
*/

        return view;

    }

    private void startActivityWithTitle(DepartmentItemView departmentItemView){
        String title = departmentItemView.getText();
        Intent intent = new Intent(getContext(), DepartmentActivity.class);
        intent.putExtra(INTENT_TOOLBAR_TITLE, title);
        startActivity(intent);
    }

   /* @Override
    public void onClick(View v) {
        Logger.i("onClick()");
        int viewId = v.getId();
        if (viewId == R.id.department1){
            Logger.i("onClick()-------department1");
            startActivityWithTitle(mDepartment1);
        }else if (viewId == R.id.department2){
            startActivityWithTitle(mDepartment2);
        }else if (viewId == R.id.department3){
            startActivityWithTitle(mDepartment3);
        }else if (viewId == R.id.department4){
            startActivityWithTitle(mDepartment4);
        }else if (viewId == R.id.department5){
            startActivityWithTitle(mDepartment5);
        }else if (viewId == R.id.department6){
            startActivityWithTitle(mDepartment6);
        }
    }
*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.reset(this);
    }

    @Override
    public void onClick(DepartmentItemView view) {
        int id = view.getId();
        if (id == R.id.department1){
            Logger.i("department fragment depart1 clicked ");
        }
    }
}
