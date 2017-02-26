package com.bdpqchen.yellowpagesmodule.yellowpages.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bdpqchen.yellowpagesmodule.yellowpages.R;


/**
 * Created by chen on 17-2-23.
 */

public class DepartmentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.yp_fragment_first, container, false);

        return view;
    }
}
