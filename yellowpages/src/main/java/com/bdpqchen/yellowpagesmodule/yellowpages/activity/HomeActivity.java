package com.bdpqchen.yellowpagesmodule.yellowpages.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.adapter.SearchResultsListAdapter;
import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.SearchHelper;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CategoryFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CollectedFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.DepartmentFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.DataBean;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.SearchResult;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.WordSuggestion;
import com.bdpqchen.yellowpagesmodule.yellowpages.network.NetworkClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.PrefUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;


public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    //    @InjectView(R.id.search_results_list)
    private RecyclerView mSearchResultsList;
    //    @InjectView(R.id.parent_view)
    private RelativeLayout mParentView;
    //    @InjectView(R.id.toolbar)
    private Toolbar mToolbar;
    private Context mContext;
    private static ProgressBar mProgressBar;
    private FloatingSearchView mSearchView;
    private SearchResultsListAdapter mSearchResultsAdapter;
    private ProgressDialog mProgressDialog;

    private String mLastQuery = "";
    private boolean isInited = false;
    private static int loadFragmentTimes = 0;


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
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mParentView = (RelativeLayout) findViewById(R.id.parent_view);
        mSearchResultsList = (RecyclerView) findViewById(R.id.search_results_list);
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
                    SearchHelper.findSuggestions(mContext, newQuery, 20, new SearchHelper.OnFindSuggestionsListener() {

                        @Override
                        public void onResults(List<WordSuggestion> results) {
                            mSearchView.swapSuggestions(results);
                            mSearchView.hideProgress();
                        }

                    });
                }
                Logger.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                WordSuggestion wordSuggestion = (WordSuggestion) searchSuggestion;
                SearchHelper.findWord(mContext, wordSuggestion.getBody(),
                        new SearchHelper.OnFindWordListener() {
                            @Override
                            public void onResults(List<SearchResult> results) {
                                mSearchResultsAdapter.swapData(results);
                                mSearchResultsAdapter.setItemsOnClickListener(new SearchResultsListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onClick(SearchResult searchResult) {
                                        Logger.i("clicked the item, onSuggestionClicked()");
                                    }
                                });
                            }
                        });
                mLastQuery = searchSuggestion.getBody();
                Logger.d(TAG, "onSuggestionClicked()");
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                SearchHelper.findWord(mContext, query,
                        new SearchHelper.OnFindWordListener() {
                            @Override
                            public void onResults(List<SearchResult> results) {
                                mSearchResultsAdapter.swapData(results);
                                mSearchResultsAdapter.setItemsOnClickListener(new SearchResultsListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onClick(SearchResult searchResult) {
                                        Toast.makeText(mContext, "You clicked the item" + searchResult.name, Toast.LENGTH_LONG).show();
                                        Logger.d("you clicked the item" + searchResult.name);
                                    }
                                });
                            }
                        });
                Logger.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mSearchView.swapSuggestions(SearchHelper.getHistory(mContext, 10));
                Logger.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {
                Logger.d(TAG, "onFocusCleared()");
            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

/*                if (item.getItemId() == R.id.action_change_colors) {

                    mIsDarkSearchTheme = true;

                    //demonstrate setting colors for items
                    mSearchView.setBackgroundColor(Color.parseColor("#787878"));
                    mSearchView.setViewTextColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setHintTextColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
                    mSearchView.setDividerColor(Color.parseColor("#BEBEBE"));
                    mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                } else {

                    //just print action
                    Toast.makeText(getActivity().getApplicationContext(), item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

                Logger.d(TAG, "onHomeClicked()");
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
        mSearchResultsAdapter = new SearchResultsListAdapter();
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(mContext));
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

//            YoYo.with(Techniques.ZoomInRight)
            YoYo.with(Techniques.SlideInRight)
                    .duration(duration).playOn(findViewById(R.id.floating_search_view));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSearchView.setSearchFocused(true);

                }
            }, duration);
/*
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSearchView.setSearchFocused(false);
                    mSearchView.setVisibility(View.GONE);
                    mSearchResultsList.setVisibility(View.GONE);
                    mParentView.setVisibility(View.GONE);
//                    YoYo.with(Techniques.SlideOutLeft).duration(200).playOn((findViewById(R.id.floating_search_view)));
                }
            }, 4000);*/

            Logger.d("clicked the toolbar");
        }
        return super.onOptionsItemSelected(item);

    }

}
