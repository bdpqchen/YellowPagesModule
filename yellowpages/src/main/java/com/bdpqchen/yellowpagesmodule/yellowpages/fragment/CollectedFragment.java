package com.bdpqchen.yellowpagesmodule.yellowpages.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.adapter.ListViewCollectedAdapter;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DatabaseClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.network.RxSchedulersHelper;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ListUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by chen on 17-2-23.
 */

public class CollectedFragment extends Fragment implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnGroupExpandListener, ExpandableListView.OnChildClickListener {

    public String[] groupStrings = {"我的收藏"};

    public String[][] childStrings1;

    public String[][] childStrings = {
            {"唐三藏", "孙悟空", "猪八戒", "沙和尚"}
    };

    private ExpandableListView mExpandableListView;

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
        final ListViewCollectedAdapter adapter = new ListViewCollectedAdapter(getContext());
        mExpandableListView.setAdapter(adapter);
        ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.addAllData(groupStrings, childStrings);
                ListUtils.getInstance().setListViewHeightBasedOnChildren(mExpandableListView);

            }
        }, 3000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCollectedData();
                getDepartmentByCategory();
                DataManager.getDepartmentsByCategory(0);
            }
        }).start();
        return view;
    }

    private void getDepartmentByCategory() {
        List<Phone> phoneList = DataManager.getDepartmentsByCategory(0);
        Logger.i(String.valueOf(phoneList.size()));
        Log.i("phonelist.size", "phonelist.name");
        Logger.i(phoneList.get(0).getDepartment());
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
                Log.i("getCollectedDataList", "onNext()");
                Log.i("phones.size", String.valueOf(phones.size()));

            }
        };
        DatabaseClient.getInstance().getCollectedData(subscriber);
    }


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

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }
}
