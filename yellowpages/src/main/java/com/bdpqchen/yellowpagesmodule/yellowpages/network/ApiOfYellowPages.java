package com.bdpqchen.yellowpagesmodule.yellowpages.network;

import com.bdpqchen.yellowpagesmodule.yellowpages.model.DataBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by chen on 17-2-26.
 */

public interface ApiOfYellowPages {

    @GET("list")
    Observable<DataBean> getDataList();


}
