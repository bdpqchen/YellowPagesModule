package com.bdpqchen.yellowpagesmodule.yellowpages.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.activity.HomeActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.adapter.ListViewCollectedAdapter;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DatabaseClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ListUtils;

import java.util.List;

import rx.Subscriber;


/**
 * Created by chen on 17-2-23.
 */

public class CollectedFragment extends Fragment implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnGroupExpandListener, ExpandableListView.OnChildClickListener {

    public String[] groupStrings = {"我的收藏"};

    public String[][] childStrings1;

    private ExpandableListView mExpandableListView;
    private ListViewCollectedAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.yp_fragment_expandable_list_view, container, false);
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expand_list_view);
        mExpandableListView.setOnGroupClickListener(this);
        mExpandableListView.setOnGroupCollapseListener(this);
        mExpandableListView.setOnGroupExpandListener(this);
        mExpandableListView.setOnChildClickListener(this);
        mAdapter = new ListViewCollectedAdapter(getContext());
        mExpandableListView.setAdapter(mAdapter);
        ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
        getCollectedData();
        return view;
    }

    public void getCollectedData(){
        Subscriber subscriber = new Subscriber<List<Phone>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                Log.i("getCollectedDataList", "onError()");
            }

            @Override
            public void onNext(List<Phone> phones) {
                mAdapter.addAllData(groupStrings, phones);
                ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
                HomeActivity.setProgressBarDismiss();
            }
        };
        DatabaseClient.getInstance().getCollectedData(subscriber);
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        if (mAdapter.getChildrenCount(0) != 0) {
            ListUtils.setCollapseListViewHeightBasedOnChildren(mExpandableListView, groupPosition);
            ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
        }
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        if (mAdapter.getChildrenCount(0) != 0) {
            ListUtils.setExpandedListViewHeightBasedOnChildren(mExpandableListView, groupPosition);
            ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
        }
    }
}
