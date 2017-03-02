package com.bdpqchen.yellowpagesmodule.yellowpages.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.activity.HomeActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ToastUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

/**
 * Created by bdpqchen on 17-3-1.
 */

public class ListViewCollectedAdapter extends BaseExpandableListAdapter {
    private static String[] mGroupArray;
    private static List<Phone> mChildList;
    private Context mContext;

    public ListViewCollectedAdapter(Context context){
        mContext = context;
    }

    public void addAllData(String[] groups, List<Phone> phoneList){
        if( groups != null){
            mGroupArray = groups;
        }
        if (phoneList != null){
            mChildList = phoneList;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        if (mGroupArray != null){
            return mGroupArray.length;
        }else{
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mChildList != null){
            return mChildList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupArray[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ListViewCollectedAdapter.GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.yp_item_elv_group, parent, false);
            groupViewHolder = new ListViewCollectedAdapter.GroupViewHolder();
            groupViewHolder.tvGroupName = (TextView) convertView.findViewById(R.id.tv_group_name);
            groupViewHolder.tvGroupLength = (TextView) convertView.findViewById(R.id.tv_group_length);
            groupViewHolder.ivDropDown = (ImageView) convertView.findViewById(R.id.iv_drop_down);
            groupViewHolder.ivDropRight = (ImageView) convertView.findViewById(R.id.iv_drop_right);
            convertView.setTag(groupViewHolder);
        }else{
            groupViewHolder = (ListViewCollectedAdapter.GroupViewHolder) convertView.getTag();
        }
        if (isExpanded){
            groupViewHolder.ivDropRight.setVisibility(View.GONE);
            groupViewHolder.ivDropDown.setVisibility(View.VISIBLE);
        }else{
            groupViewHolder.ivDropRight.setVisibility(View.VISIBLE);
            groupViewHolder.ivDropDown.setVisibility(View.GONE);
        }
        groupViewHolder.tvGroupName.setText(mGroupArray[groupPosition]);
        groupViewHolder.tvGroupLength.setText(mChildList.size() + "");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (mChildList.size() != 0) {
            final Phone phone = mChildList.get(childPosition);
            final ListViewCollectedAdapter.ChildViewHolder childViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.yp_item_elv_child_collected, parent, false);
                childViewHolder = new ListViewCollectedAdapter.ChildViewHolder();
                childViewHolder.tvChildTitle = (TextView) convertView.findViewById(R.id.tv_item_collected_name);
                childViewHolder.tvChildPhone = (TextView) convertView.findViewById(R.id.tv_item_collected_phone);
                childViewHolder.ivCallPhone = (ImageView) convertView.findViewById(R.id.iv_item_children_icon_phone);
                childViewHolder.ivCollected = (ImageView) convertView.findViewById(R.id.iv_item_children_icon_collected);
                childViewHolder.ivUncollected = (ImageView) convertView.findViewById(R.id.iv_item_children_icon_uncollected);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ListViewCollectedAdapter.ChildViewHolder) convertView.getTag();
            }
            childViewHolder.tvChildTitle.setText(phone.getName());
            childViewHolder.tvChildPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            childViewHolder.tvChildPhone.setText(phone.getPhone());
            childViewHolder.tvChildPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setPrimaryClip(ClipData.newPlainText(null, phone.getPhone()));
                    ToastUtils.show((Activity) mContext, "已复制到剪切板");
                }
            });
            childViewHolder.ivCollected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.ZoomOut).duration(400).playOn(childViewHolder.ivCollected);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            childViewHolder.ivCollected.setVisibility(View.GONE);
                            childViewHolder.ivUncollected.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                    DataManager.updateCollectState(phone.getName(), phone.getPhone());
                    ToastUtils.show((Activity) mContext, "收藏已取消");
                }
            });
            childViewHolder.ivUncollected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childViewHolder.ivCollected.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.ZoomIn).duration(400).playOn(childViewHolder.ivCollected);
                    childViewHolder.ivUncollected.setVisibility(View.GONE);
                    DataManager.updateCollectState(phone.getName(), phone.getPhone());
                }
            });
            childViewHolder.ivCallPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        if (mChildList.size() == 0){
            return false;
        }
        return true;
    }

    private static class GroupViewHolder{
        TextView tvGroupName, tvGroupLength;
        ImageView ivDropRight, ivDropDown;
    }

    private static class ChildViewHolder{
        TextView tvChildTitle, tvChildPhone;
        ImageView ivCallPhone, ivCollected, ivUncollected;

    }

}
