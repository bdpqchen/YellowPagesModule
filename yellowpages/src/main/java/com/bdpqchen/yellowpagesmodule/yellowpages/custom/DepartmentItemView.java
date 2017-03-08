package com.bdpqchen.yellowpagesmodule.yellowpages.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.orhanobut.logger.Logger;

/**
 * Created by bdpqchen on 17-3-7.
 */

public class DepartmentItemView extends FrameLayout implements View.OnClickListener {

    private TextView mTitle;
    private ImageView mIcon;
    private LinearLayout mLlDepartment;
    public OnClickListener mClickListener;


    public DepartmentItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.yp_custom_view_department, this);
        mTitle = (TextView) findViewById(R.id.tv_department);
        mIcon  = (ImageView) findViewById(R.id.iv_department);
        mLlDepartment = (LinearLayout) findViewById(R.id.ll_department);
//        mLlDepartment.setOnClickListener(this);
//        mTitle.setOnClickListener(null);

    }

    public void setTitle(String title){
        mTitle.setText(title);
    }

    public void setIcon(int iconId){
        mIcon.setImageResource(iconId);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mClickListener = l;
        mLlDepartment.setOnClickListener(l);
        super.setOnClickListener(l);
    }



    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        Logger.i("custom view is clicked");
        if (viewId == R.id.tv_department){
            Logger.i("custom view tv is clicked");
        }else if (viewId == R.id.ll_department){
            Logger.i("custom view linear layout is clicked");
        }


    }
}
