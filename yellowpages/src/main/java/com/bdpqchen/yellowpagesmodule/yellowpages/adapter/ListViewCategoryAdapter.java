package com.bdpqchen.yellowpagesmodule.yellowpages.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.TextFormatUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ToastUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

/**
 * Created by bdpqchen on 17-3-3.
 */

public class ListViewCategoryAdapter extends BaseAdapter {

    private Context mContext;

    private static List<Phone> phoneList;
    private List<Phone> mDataSet;

    public ListViewCategoryAdapter(Context context, List<Phone>phones){
        this.mContext = context;
        phoneList = phones;
    }

    public static void updateDataSet(List<Phone> phones){
        phoneList = phones;
    }

    @Override
    public int getCount() {
        if (null == phoneList){
            return 0;
        }
        return phoneList.size();
    }

    @Override
    public Object getItem(int position) {
        return phoneList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        final NormalViewHolder holder;
        if (phoneList.size() != 0){
            if (convertView == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.yp_item_elv_child_collected, parent);
                holder = new NormalViewHolder();
                holder.ivCollected = (ImageView) view.findViewById(R.id.iv_item_children_icon_collected);
                holder.ivUncollected = (ImageView) view.findViewById(R.id.iv_item_children_icon_uncollected);
                holder.ivPhone = (ImageView) view.findViewById(R.id.iv_item_children_icon_phone);
                holder.tvPhone = (TextView) view.findViewById(R.id.tv_item_collected_phone);
                holder.tvTitle = (TextView) view.findViewById(R.id.tv_item_collected_name);
                view.setTag(holder);
            }else {
                view = convertView;
                holder = (NormalViewHolder) view.getTag();
            }
            final Phone phone = phoneList.get(position);
            holder.tvTitle.setText(phone.getName());
            holder.tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            holder.tvPhone.setText(phone.getPhone());
            holder.tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setPrimaryClip(ClipData.newPlainText(null, phone.getPhone()));
                    ToastUtils.show((Activity) mContext, "已复制到剪切板");
                }
            });

            holder.ivCollected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.ZoomOut).duration(400).playOn(holder.ivCollected);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.ivCollected.setVisibility(View.GONE);
                            holder.ivUncollected.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                    DataManager.updateCollectState(phone.getName(), phone.getPhone());
                    ToastUtils.show((Activity) mContext, "收藏已取消");
                }
            });
            holder.ivUncollected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ivCollected.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.ZoomIn).duration(400).playOn(holder.ivCollected);
                    holder.ivUncollected.setVisibility(View.GONE);
                    DataManager.updateCollectState(phone.getName(), phone.getPhone());
                }
            });

            holder.ivPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNum = TextFormatUtils.getPhoneNum(phone.getPhone());
                    mFragmentCallBack.callPhone(phoneNum);
                }
            });

        }

        return convertView;
    }

    private class NormalViewHolder{
        TextView tvTitle, tvPhone;
        ImageView ivPhone, ivUncollected, ivCollected;
        public NormalViewHolder(){

        }

    }

}
