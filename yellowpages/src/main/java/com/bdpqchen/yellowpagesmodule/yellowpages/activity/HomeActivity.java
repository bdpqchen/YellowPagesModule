package com.bdpqchen.yellowpagesmodule.yellowpages.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.adapter.SearchResultsListAdapter;
import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.SearchHelper;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.DepartmentFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CollectedFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CategoryFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.SearchResult;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.WordSuggestion;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.orhanobut.logger.Logger;

import java.util.List;


public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    //    @InjectView(R.id.search_results_list)
    private RecyclerView mSearchResultsList;
    //    @InjectView(R.id.parent_view)
    private RelativeLayout mParentView;
    //    @InjectView(R.id.toolbar)
    private Toolbar mToolbar;
    private Context mContext;

    private String mLastQuery = "";


    private FloatingSearchView mSearchView;
    private SearchResultsListAdapter mSearchResultsAdapter;


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
        DepartmentFragment departmentFragment = new DepartmentFragment();

        CollectedFragment collectedFragment = new CollectedFragment();
        CategoryFragment categoryFragment = new CategoryFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_department, departmentFragment);
        fragmentTransaction.add(R.id.fragment_container_collected, collectedFragment);
        fragmentTransaction.add(R.id.fragment_container_list, categoryFragment);
        fragmentTransaction.commit();
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mParentView = (RelativeLayout) findViewById(R.id.parent_view);
        mSearchResultsList = (RecyclerView) findViewById(R.id.search_results_list);

        setupSearchView();
        setupResultsList();
    }

    private void setupSearchView() {

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.
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

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(SearchHelper.getHistory(mContext, 10));

                Logger.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
//                mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

                Logger.d(TAG, "onFocusCleared()");
            }
        });

        //handle menu clicks the same way as you would
        //in a regular activity
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

//                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
//                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                if (searchSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

//                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                /*textView.setTextColor(Color.parseColor(textColor));
                String text = searchSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));*/
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
