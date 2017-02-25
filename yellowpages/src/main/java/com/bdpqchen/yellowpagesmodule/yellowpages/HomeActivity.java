package com.bdpqchen.yellowpagesmodule.yellowpages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.FirstFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.SecondFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.ThirdFragment;
import com.orhanobut.logger.Logger;


public class HomeActivity extends BaseActivity {

//    @InjectView(R.id.toolbar)
    private Toolbar mToolbar;

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
        FirstFragment firstFragment = new FirstFragment();
        SecondFragment secondFragment = new SecondFragment();
        ThirdFragment thirdFragment = new ThirdFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_department, firstFragment);
        fragmentTransaction.add(R.id.fragment_container_collected, secondFragment);
        fragmentTransaction.add(R.id.fragment_container_list, thirdFragment);
        fragmentTransaction.commit();


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

            Intent intent = new Intent(this, SearchActivity.class);
            this.startActivity(intent);
            this.overridePendingTransition(R.anim.yp_activity_open, R.anim.yp_activity_close);
            Logger.d("clicked the toolbar");
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        revealFromCoordinates(motionEvent.getRawX(), motionEvent.getRawY());

        return false;

    }
}
