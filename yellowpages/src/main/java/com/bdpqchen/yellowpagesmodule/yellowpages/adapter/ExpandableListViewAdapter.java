package com.bdpqchen.yellowpagesmodule.yellowpages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;


/**
 * Created by chen on 17-2-23.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter implements ExpandableListAdapter{

    private static String[] mGroupArray = null;
    private static String[][] mChildArray = null;
    private Context mContext;

    public ExpandableListViewAdapter(Context context){
        mContext = context;
    }

    public ExpandableListViewAdapter(String[] groupStrings, String[][] childStrings, Context context) {
        mChildArray = childStrings;
        mGroupArray = groupStrings;
        mContext = context;
    }

    public void addAllData(String[] groups, String[][] children){
        if( groups != null){
            mGroupArray = groups;
        }
        if (children != null){
            mChildArray = children;
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
        if (mChildArray != null){
            return mChildArray[groupPosition].length;
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
        return mChildArray[groupPosition][childPosition];
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
        GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.yp_item_elv_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvGroupName = (TextView) convertView.findViewById(R.id.tv_group_name);
            groupViewHolder.tvGroupLength = (TextView) convertView.findViewById(R.id.tv_group_length);
            groupViewHolder.ivDropDown = (ImageView) convertView.findViewById(R.id.iv_drop_down);
            groupViewHolder.ivDropRight = (ImageView) convertView.findViewById(R.id.iv_drop_right);
            convertView.setTag(groupViewHolder);
        }else{
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if (isExpanded){
            groupViewHolder.ivDropRight.setVisibility(View.GONE);
            groupViewHolder.ivDropDown.setVisibility(View.VISIBLE);
        }else{
            groupViewHolder.ivDropRight.setVisibility(View.VISIBLE);
            groupViewHolder.ivDropDown.setVisibility(View.GONE);
        }
        groupViewHolder.tvGroupName.setText(mGroupArray[groupPosition]);
        groupViewHolder.tvGroupLength.setText(mChildArray[groupPosition].length + "");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.yp_item_elv_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvChildTitle = (TextView) convertView.findViewById(R.id.tv_child_title);
            convertView.setTag(childViewHolder);
        }else{
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvChildTitle.setText(mChildArray[groupPosition][childPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class GroupViewHolder{
        TextView tvGroupName, tvGroupLength;
        ImageView ivDropRight, ivDropDown;
    }

    private static class ChildViewHolder{
        TextView tvChildTitle;
    }

}
