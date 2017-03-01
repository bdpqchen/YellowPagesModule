package com.bdpqchen.yellowpagesmodule.yellowpages.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.bdpqchen.yellowpagesmodule.yellowpages.activity.HomeActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.adapter.ExpandableListViewAdapter;
import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DatabaseClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.SearchResult;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ListUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscriber;

/**
 * Created by chen on 17-2-23.
 */

public class CategoryFragment extends Fragment implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnGroupExpandListener, ExpandableListView.OnChildClickListener {

    public String[] groupStrings = {"校级部门", "院级部门", "其他" };
    /*public String[][] childStrings = {
            {"唐三藏", "孙悟空", "猪八戒", "沙和尚"},
            {"宋江", "林冲", "李逵", "鲁智深"},
            {"曹操", "刘备", "孙权", "诸葛亮", "周瑜"},
            {"贾宝玉", "林黛玉", "薛宝钗", "王熙凤"},
            {"唐三藏", "孙悟空", "猪八戒", "沙和尚"},
            {"宋江", "林冲", "李逵", "鲁智深"},
            {"曹操", "刘备", "孙权", "诸葛亮", "周瑜"},
            {"贾宝玉", "林黛玉", "薛宝钗", "王熙凤"}
    };*/

    public List<SearchResult> searchResultList;

    private ExpandableListView mExpandableListView;
    private ExpandableListViewAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.yp_fragment_second, container, false);
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expand_list_view);
        mExpandableListView.setOnGroupClickListener(this);
        mExpandableListView.setOnGroupCollapseListener(this);
        mExpandableListView.setOnGroupExpandListener(this);
        mExpandableListView.setOnChildClickListener(this);
        mAdapter = new ExpandableListViewAdapter(getContext());
        mExpandableListView.setAdapter(mAdapter);
        ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);

        getCategoryData();

        return view;
    }

    private void getCategoryData() {
        Subscriber subscriber = new Subscriber<String[][]>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Logger.i("getCategorydata-----onError()");
            }

            @Override
            public void onNext(String[][] strings) {
                mAdapter.addAllData(groupStrings, strings);
                ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
                HomeActivity.setProgressBarDismiss();
                String[][] phoneList = strings;
                Logger.i(String.valueOf(phoneList.length));
                Log.i("phonelist.size", "phonelist.name");
                Logger.i(phoneList[0][0]);
                Logger.i(phoneList[1][1]);
            }

        };
        DatabaseClient.getInstance().getCategoryDataList(subscriber);
    }

    /*
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

            return false;
        }

    */
    @Override
    public void onGroupCollapse(int groupPosition) {
        ListUtils.setCollapseListViewHeightBasedOnChildren(mExpandableListView, groupPosition);
        ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        ListUtils.setExpandedListViewHeightBasedOnChildren(mExpandableListView, groupPosition);
        ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
    }

/*
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }
*/

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }
}
