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

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by chen on 17-2-23.
 */

public class DepartmentFragment extends Fragment implements View.OnClickListener {

    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.yp_fragment_department, container, false);
//        ButterKnife.inject(this, view);
        //the ButterKnife can not inject in a library. so, I had to add those fucking codes....
        //https://github.com/JakeWharton/butterknife/issues/100

        imageView1 = (ImageView) view.findViewById(R.id.department_iv_1);
        imageView2 = (ImageView) view.findViewById(R.id.department_iv_2);
        imageView3 = (ImageView) view.findViewById(R.id.department_iv_3);
        imageView4 = (ImageView) view.findViewById(R.id.department_iv_4);
        imageView5 = (ImageView) view.findViewById(R.id.department_iv_5);
        imageView6 = (ImageView) view.findViewById(R.id.department_iv_6);
        textView1  = (TextView) view.findViewById(R.id.department_tv_1);
        textView2  = (TextView) view.findViewById(R.id.department_tv_2);
        textView3  = (TextView) view.findViewById(R.id.department_tv_3);
        textView4  = (TextView) view.findViewById(R.id.department_tv_4);
        textView5  = (TextView) view.findViewById(R.id.department_tv_5);
        textView6  = (TextView) view.findViewById(R.id.department_tv_6);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);
        textView6.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);

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
        int i = v.getId();
        if (i == R.id.department_iv_1 || i == R.id.department_tv_1) {

        }else if (i == R.id.department_iv_2 || i == R.id.department_tv_2) {

        }else if (i == R.id.department_iv_3 || i == R.id.department_tv_3) {

        }else if (i == R.id.department_iv_4 || i == R.id.department_tv_4) {

        }else if (i == R.id.department_iv_5 || i == R.id.department_tv_5) {

        }else if (i == R.id.department_iv_6 || i == R.id.department_tv_6) {

        }
    }
}
