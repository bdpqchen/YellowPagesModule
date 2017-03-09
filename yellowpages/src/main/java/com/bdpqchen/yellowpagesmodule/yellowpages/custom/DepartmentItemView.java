package com.bdpqchen.yellowpagesmodule.yellowpages.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;

/**
 * Created by bdpqchen on 17-3-7.
 */

public class DepartmentItemView extends LinearLayout implements View.OnClickListener {

    private TextView mTitle;
    private ImageView mIcon;
    private Drawable mItemBg;
    private Drawable mIconDrawable;
    private int  mtvTextSize;
    private LinearLayout mLlDepartment;
    private int mTvColor;
    private String mTvText;
    private int mTitleTextSize;

    public DepartmentItemView(Context context){
        this(context, null);
    }

    public DepartmentItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DepartmentItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.yp_custom_view_department, this, true);
        mTitle = (TextView) findViewById(R.id.tv_department);
        mIcon = (ImageView) findViewById(R.id.iv_department);
        mLlDepartment = (LinearLayout) findViewById(R.id.ll_department);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.yp_DepartmentItemView, defStyle, 0);
        mTvText = typedArray.getString(R.styleable.yp_DepartmentItemView_yp_text);
        mTvColor = typedArray.getColor(R.styleable.yp_DepartmentItemView_yp_text_color, ContextCompat.getColor(context, R.color.yp_font_black_700));
        mtvTextSize = typedArray.getDimensionPixelSize(R.styleable.yp_DepartmentItemView_yp_text_size, 16);
        mIconDrawable = typedArray.getDrawable(R.styleable.yp_DepartmentItemView_yp_icon_src);
//        mItemBg = typedArray.getResourceId(R.styleable.yp_DepartmentItemView_yp_background, -1);
        mItemBg = typedArray.getDrawable(R.styleable.yp_DepartmentItemView_yp_background);


        mLlDepartment.setBackground(mItemBg);
        mTitle.setTextSize(mtvTextSize);
        mTitle.setTextColor(mTvColor);
        mTitle.setText(mTvText);
        mIcon.setImageDrawable(mIconDrawable);

        typedArray.recycle();

    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mLlDepartment.setOnClickListener(l);
        super.setOnClickListener(l);
    }

    @Override
    public void onClick(View v) {
        
    }
}
