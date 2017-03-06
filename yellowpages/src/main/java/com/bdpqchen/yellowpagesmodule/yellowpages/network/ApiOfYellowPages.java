package com.bdpqchen.yellowpagesmodule.yellowpages.network;

import com.bdpqchen.yellowpagesmodule.yellowpages.model.DataBean;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.DatabaseVersion;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by chen on 17-2-26.
 */

public interface ApiOfYellowPages {

    @GET("test_final2")
    Observable<DataBean> getDataList();

    @GET("version")
    Observable<DatabaseVersion> getDbVersion();

}
