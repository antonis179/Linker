package org.amoustakos.linker.io;

import org.amoustakos.linker.io.db.RealmManager;
import org.amoustakos.linker.io.models.Server;
import org.amoustakos.linker.io.models.base.BaseResponse;
import org.amoustakos.linker.io.models.request.LinkRequest;
import org.amoustakos.linker.io.remote.ApiService;
import org.amoustakos.linker.util.network.HttpStatusCode;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;


@Singleton
public class DataManager {

    private final ApiService apiService;
    private final PreferencesHelper mPreferencesHelper;
    private final RealmManager mDatabaseHelper;

    @Inject
    public DataManager(
                            ApiService apiService,
                            PreferencesHelper preferencesHelper,
                            RealmManager databaseHelper
                       ) {
        this.apiService = apiService;
        this.mPreferencesHelper = preferencesHelper;
        this.mDatabaseHelper = databaseHelper;
    }


    /*
     * REMOTE
     */
    public Observable<BaseResponse> sendLink(String ip, String port, LinkRequest req){
        String url = String.format(ApiService.LINK_ENDPOINT, ip, port);
        return apiService.sendLink(url, req);
    }

    public Observable<BaseResponse> sendLink(Server server, LinkRequest req){
        String url = String.format(ApiService.LINK_ENDPOINT, server.ip, server.port);
        return apiService.sendLink(url, req);
    }

    public Observable<Boolean> isOnline(String ip, String port){
        String url = String.format(ApiService.STATUS_ENDPOINT, ip, port);
        return apiService.getStatus(url)
                         .map(baseResponse ->
                            baseResponse != null && HttpStatusCode.isSuccess(baseResponse.statusCode)
                         );
    }

    public Observable<Boolean> isOnline(Server server){
        String url = String.format(ApiService.STATUS_ENDPOINT, server.ip, server.port);
        return apiService.getStatus(url)
                .map(baseResponse ->
                        baseResponse != null && HttpStatusCode.isSuccess(baseResponse.statusCode)
                );
    }
}
