package com.bdpqchen.yellowpagesmodule.yellowpages.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CollectedFragmentCallBack;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.SearchResult;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.TextFormatUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ToastUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import java.util.List;

public class SearchResultsListViewAdapter extends BaseAdapter {

    private Context mContext;

    private static List<SearchResult> searchResultList;
    private CollectedFragmentCallBack mFragmentCallBack;
    private OnItemClickListener mItemsOnClickListener;

    public interface OnItemClickListener{
        void onClick(SearchResult searchResult);
    }

    public SearchResultsListViewAdapter(Context context, List<SearchResult> resultList, CollectedFragmentCallBack callBack){
        this.mContext = context;
        searchResultList = resultList;
        mFragmentCallBack = callBack;
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener){
        this.mItemsOnClickListener = onClickListener;
    }

    public void swapData(List<SearchResult> resultList){
        searchResultList = resultList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == searchResultList){
            return 0;
        }
        return searchResultList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SearchResultsListViewAdapter.NormalViewHolder holder;
        if (searchResultList.size() != 0){
            if (convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.yp_item_elv_child_collected, parent, false);
                holder = new SearchResultsListViewAdapter.NormalViewHolder();
                holder.ivCollected = (ImageView) convertView.findViewById(R.id.iv_item_children_icon_collected);
                holder.ivUncollected = (ImageView) convertView.findViewById(R.id.iv_item_children_icon_uncollected);
                holder.ivPhone = (ImageView) convertView.findViewById(R.id.iv_item_children_icon_phone);
                holder.tvPhone = (TextView) convertView.findViewById(R.id.tv_item_collected_phone);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_collected_name);
                holder.rlItem  = (RelativeLayout) convertView.findViewById(R.id.rl_item_search_result);

                convertView.setTag(holder);
            }else {
                holder = (SearchResultsListViewAdapter.NormalViewHolder) convertView.getTag();
            }
            final SearchResult results = searchResultList.get(position);
            if (mItemsOnClickListener != null){
                holder.rlItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemsOnClickListener.onClick(searchResultList.get(position));
                    }
                });
            }
            if (results.isCollected == 0){
                holder.ivUncollected.setVisibility(View.VISIBLE);
                holder.ivCollected.setVisibility(View.INVISIBLE);
            }else{
                holder.ivUncollected.setVisibility(View.INVISIBLE);
                holder.ivCollected.setVisibility(View.VISIBLE);
            }
            holder.tvTitle.setText(results.name);
            holder.tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            holder.tvPhone.setText(results.phone);
            holder.tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cmb = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setPrimaryClip(ClipData.newPlainText(null, results.phone));
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
                    DataManager.updateCollectState(results.name, results.phone, false);
                    ToastUtils.show((Activity) mContext, "收藏已取消");
                }
            });
            holder.ivUncollected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ivCollected.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.ZoomIn).duration(400).playOn(holder.ivCollected);
                    holder.ivUncollected.setVisibility(View.GONE);
                    DataManager.updateCollectState(results.name, results.phone, false);
                }
            });

            holder.ivPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String resultsNum = TextFormatUtils.getPhoneNum(results.phone);
                    mFragmentCallBack.callPhone(resultsNum);
                }
            });

        }

        return convertView;
    }

    private class NormalViewHolder{
        TextView tvTitle, tvPhone;
        ImageView ivPhone, ivUncollected, ivCollected;
        RelativeLayout rlItem;
        NormalViewHolder(){
        }

    }
}
