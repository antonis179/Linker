package org.amoustakos.linker.io;

import org.amoustakos.linker.io.db.RealmController;
import org.amoustakos.linker.io.models.Server;
import org.amoustakos.linker.io.models.base.BaseResponse;
import org.amoustakos.linker.io.models.request.LinkRequest;
import org.amoustakos.linker.io.remote.ApiService;
import org.amoustakos.linker.util.network.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.realm.RealmResults;


@Singleton
public class DataManager {

    private final ApiService apiService;
    private final PreferencesHelper mPreferencesHelper;
    private final RealmController mDatabaseHelper;

    @Inject
    public DataManager(
                            ApiService apiService,
                            PreferencesHelper preferencesHelper,
                            RealmController databaseHelper
                       ) {
        this.apiService = apiService;
        this.mPreferencesHelper = preferencesHelper;
        this.mDatabaseHelper = databaseHelper;
    }


    /*
     * LOCAL
     */
    public List<Server> getAllServersBlocking(){
        List<Server> servers = new ArrayList<>();
        RealmResults results = mDatabaseHelper.getByModel(Server.class);
        for(Object server : results)
            servers.add((Server) server);
        return servers;
    }


    /*
     * REMOTE
     */
    public Observable<BaseResponse> sendLink(String ip, String port, LinkRequest req){
        String url = String.format(ApiService.LINK_ENDPOINT, ip, port);
        return apiService.sendLink(url, req);
    }

    public Observable<Boolean> isOnline(String ip, String port){
        String url = String.format(ApiService.STATUS_ENDPOINT, ip, port);
        return apiService.getStatus(url)
                         .map(baseResponse ->
                            baseResponse != null && HttpStatusCode.isSuccess(baseResponse.statusCode)
                         );
    }
}
