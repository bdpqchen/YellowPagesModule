package com.bdpqchen.yellowpagesmodule.yellowpages.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.adapter.SearchResultsListAdapter;
import com.bdpqchen.yellowpagesmodule.yellowpages.adapter.SearchResultsListViewAdapter;
import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.SearchHelper;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CategoryFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CollectedFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CollectedFragmentCallBack;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.DepartmentFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.DataBean;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.SearchResult;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.WordSuggestion;
import com.bdpqchen.yellowpagesmodule.yellowpages.network.NetworkClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.PrefUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.RingUpUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ToastUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;


public class HomeActivity extends BaseActivity implements CollectedFragmentCallBack{

    private static final int REQUEST_CODE_CALL_PHONE = 33;
    private static final String TAG = "HomeActivity";
    //    @InjectView(R.id.search_results_list)
    private ListView mSearchResultsList;
    //    @InjectView(R.id.parent_view)
    private RelativeLayout mParentView;
    //    @InjectView(R.id.toolbar)
    private Toolbar mToolbar;
    private Context mContext;
    private static ProgressBar mProgressBar;
    private FloatingSearchView mSearchView;
    private SearchResultsListViewAdapter mSearchResultsAdapter;

    private ProgressDialog mProgressDialog;
    private String mLastQuery = "";
    private boolean isInited = false;
    private static int loadFragmentTimes = 0;
    private CollectedFragmentCallBack mRingUpCallBack;
    private String callPhoneNum= "";


    @Override
    public int getLayout() {
        return R.layout.yp_activity_home;
    }

    @Override
    protected Toolbar getToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mToolbar.setTitle(getResources().getString(R.string.yp_app_name));

        return mToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        this.mRingUpCallBack = this;
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mParentView = (RelativeLayout) findViewById(R.id.parent_view);
        mSearchResultsList = (ListView) findViewById(R.id.search_results_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        DepartmentFragment departmentFragment = new DepartmentFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_department, departmentFragment);
        fragmentTransaction.commit();
        setupSearchView();
        setupResultsList();
        if (!PrefUtils.isFirstOpen()) {
            setListViewShow();
        } else {
            mProgressDialog = new ProgressDialog(this);
            showInitDialog();
            getDataList();
        }
    }

    public static void setProgressBarDismiss(){
        loadFragmentTimes++;
        if(loadFragmentTimes >= 2){
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setListViewShow() {

        CollectedFragment collectedFragment = new CollectedFragment();
        CategoryFragment categoryFragment = new CategoryFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_collected, collectedFragment);
        fragmentTransaction.add(R.id.fragment_container_list, categoryFragment);
        fragmentTransaction.commit();
    }

    private void showInitDialog() {
//        mProgressDialog.setProgress(0);
        mProgressBar.incrementProgressBy(-10);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("首次使用，需要导入号码库，请等待...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(100);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

    }

    public void getDataList() {

        Subscriber subscriber = new Subscriber<DataBean>() {
            @Override
            public void onCompleted() {
                mProgressDialog.incrementProgressBy(50);
            }

            @Override
            public void onError(Throwable e) {
                mProgressDialog.dismiss();
                mProgressDialog.incrementProgressBy(0);
                showInitErrorDialog();
            }

            @Override
            public void onNext(DataBean dataBean) {
                Logger.i("onNext()");
                Logger.i(String.valueOf(dataBean.getCategory_list().size()));
                Logger.i(String.valueOf(dataBean.getCategory_list().get(0).getDepartment_list().size()));
                Logger.i(String.valueOf(dataBean.getCategory_list().get(0).getDepartment_list().get(0).getUnit_list().size()));
                initDatabase(dataBean);
            }

        };
        NetworkClient.getInstance().getDataList(subscriber);
        mProgressDialog.incrementProgressBy(10);
    }

    private void initDatabase(final DataBean dataBean) {
        final long time = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Phone> phoneList = new ArrayList<>();
                for (int i = 0; i < dataBean.getCategory_list().size(); i++) { //3个分类
                    mProgressDialog.incrementProgressBy(10);
                    DataBean.CategoryListBean categoryList = dataBean.getCategory_list().get(i);
                    Logger.d(i + "====i");
                    for (int j = 0; j < categoryList.getDepartment_list().size(); j++) {     //第i分类里的部门j
                        Logger.d(j + "===j");
                        DataBean.CategoryListBean.DepartmentListBean departmentList = categoryList.getDepartment_list().get(j);
                        for (int k = 0; k < departmentList.getUnit_list().size(); k++) {     //第k部门里的单位
                            Logger.d(k + "===k");
                            DataBean.CategoryListBean.DepartmentListBean.UnitListBean list = departmentList.getUnit_list().get(k);
                            Logger.d(k + "===l");
                            Phone phone = new Phone();
                            phone.setCategory(i);
                            phone.setPhone(list.getItem_phone());
                            phone.setName(list.getItem_name());
                            phone.setIsCollected(0);
                            phone.setDepartment(departmentList.getDepartment_name());
                            phoneList.add(phone);
                        }
                    }
                }
                DataManager.insertBatch(phoneList);
                mProgressDialog.incrementProgressBy(10);
                Logger.i(String.valueOf(phoneList.size()));
                Logger.i("DataList.size", phoneList.size() + "");
                Logger.i(String.valueOf(phoneList.get(0).getIsCollected()));
                Logger.i(String.valueOf(phoneList.get(1).getIsCollected()));
                Logger.d(System.currentTimeMillis() - time);
                setListViewShow();
                mProgressDialog.dismiss();
                PrefUtils.setIsFirstOpen(false);
            }
        }).start();

    }

    private void showInitErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("出错提醒")
                .setCancelable(false)
                .setMessage("\t导入失败，请检查网络是否可用")
                .setNegativeButton("重新导入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showInitDialog();
                        getDataList();
                    }
                })
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }


    private void setupSearchView() {

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();
                    SearchHelper.findSuggestions(newQuery, 5, new SearchHelper.OnFindSuggestionsListener() {

                        @Override
                        public void onResults(List<WordSuggestion> results) {
                            mSearchView.swapSuggestions(results);
                            mSearchView.hideProgress();
                        }

                    });
                }
                Logger.d("onSearchTextChanged()");
                mLastQuery = newQuery;
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                WordSuggestion wordSuggestion = (WordSuggestion) searchSuggestion;
                Logger.t(TAG).d(mLastQuery);
                String t = mLastQuery;
                mLastQuery = wordSuggestion.getBody();

                SearchHelper.findWord(wordSuggestion.getBody(), t,
                        new SearchHelper.OnFindWordListener() {
                            @Override
                            public void onResults(List<SearchResult> results) {
                                mSearchView.clearQuery();
                                mSearchResultsAdapter.swapData(results);
                                mSearchResultsAdapter.setItemsOnClickListener(new SearchResultsListViewAdapter.OnItemClickListener() {
                                    @Override
                                    public void onClick(SearchResult searchResult) {
                                        Logger.i("clicked the item, onSuggestionClicked()");

                                    }
                                });
                            }
                        });

                Logger.d("onSuggestionClicked()");
            }
            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                SearchHelper.findWord(query, mLastQuery,
                        new SearchHelper.OnFindWordListener() {
                            @Override
                            public void onResults(List<SearchResult> results) {
                                mSearchResultsAdapter.swapData(results);
                                mSearchResultsAdapter.setItemsOnClickListener(new SearchResultsListViewAdapter.OnItemClickListener() {
                                    @Override
                                    public void onClick(SearchResult searchResult) {
                                        Toast.makeText(mContext, "You clicked the item" + searchResult.name, Toast.LENGTH_LONG).show();
                                        Logger.d("you clicked the item" + searchResult.name);
                                    }
                                });
                            }
                        });
                Logger.d("onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mSearchView.swapSuggestions(SearchHelper.getHistory(10));
                Logger.d("onFocus()");
            }

            @Override
            public void onFocusCleared() {
                if (mLastQuery.equals(DataManager.TOO_MUCH_DATA_NAME)){
                    mSearchView.clearQuery();
                }
                Logger.d("onFocusCleared()");
            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.yp_action_clear_history) {
                    DataManager.clearHistory();
                    ToastUtils.show((Activity) mContext, "已清空搜索记录");
                }

            }
        });

        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

                Logger.d("onHomeClicked()");
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                WordSuggestion searchSuggestion = (WordSuggestion) item;
                if (searchSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }
            }

        });

        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultsList.setTranslationY(newHeight);
            }
        });

    }

    private void setupResultsList() {
        mSearchResultsAdapter = new SearchResultsListViewAdapter(this, null, mRingUpCallBack);
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.yp_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            int duration = 200;
            mSearchView.setVisibility(View.VISIBLE);
            mSearchResultsList.setVisibility(View.VISIBLE);
            mParentView.setVisibility(View.VISIBLE);

            YoYo.with(Techniques.SlideInRight)
                    .duration(duration).playOn(findViewById(R.id.floating_search_view));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSearchView.setSearchFocused(true);
                }
            }, duration);

            Logger.d("clicked the toolbar");
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void callPhone(String phoneNum) {
        this.callPhoneNum = phoneNum;
        RingUpUtils.permissionCheck(mContext, phoneNum, REQUEST_CODE_CALL_PHONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    RingUpUtils.ringUp(mContext, callPhoneNum);
                }else{
                    ToastUtils.show(this, "请在权限管理中开启微北洋拨打电话权限");
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Logger.i("received the key down");
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Logger.i("keycode back is triggered ");
            if (mSearchView.getVisibility() == View.GONE){
                Logger.i("finished view");
                finish();
            }else{
                mSearchView.setVisibility(View.GONE);
                mSearchResultsList.setVisibility(View.GONE);
                mParentView.setVisibility(View.GONE);
                Logger.i("set search view gone");
            }
        }

        return super.onKeyDown(keyCode, event);

    }
}
