package org.amoustakos.linker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.amoustakos.linker.R;
import org.amoustakos.linker.io.DataManager;
import org.amoustakos.linker.io.db.RealmManager;
import org.amoustakos.linker.io.models.Server;
import org.amoustakos.linker.io.models.base.BaseResponse;
import org.amoustakos.linker.io.models.request.LinkRequest;
import org.amoustakos.linker.ui.adapters.ServerAdapter;
import org.amoustakos.linker.ui.adapters.abs.RealmModelAdapter;
import org.amoustakos.linker.ui.base.BaseActivity;
import org.amoustakos.linker.util.RxUtil;
import org.amoustakos.linker.util.ValidationUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.RealmResults;
import timber.log.Timber;

public class ShareLinkActivity extends BaseActivity {

    @Inject
    DataManager dataManager;
    @Inject
    RealmManager realmManager;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    //Server
    @BindView(R.id.rv_server) RecyclerView serverRV;
    private ServerAdapter serverAdapter;

    private String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_share_link);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);


        //Get data from intent
        getData(getIntent());

        //Check Url
        if(url == null)
            return;
        if(!ValidationUtil.isValidURL(url)){
            //Show error
            finish();
            return;
        }

        //If 1 server send and finish
        RealmResults<Server> servers = realmManager.getServers();
        if(servers.size() == 1){
            checkAndSend(servers.get(0), true);
            finish();
            return;
        }

        //Setup recycler
        serverAdapter = new ServerAdapter(this, realmManager);
        setupRecycler();
        realmManager.setAutoRefresh(true);
        setRealmAdapter(servers);


        //Check for user choice
        final Observable<Integer> obs = serverAdapter.getPositionClicks();
        obs.doOnNext(pos -> {
                checkAndSend(serverAdapter.getItem(pos), true);
                obs.unsubscribeOn(AndroidSchedulers.mainThread()); //FIXME: remove once dlg is done
            })
            .subscribe();
    }

    //TODO: Implement multiple server selection
    //TODO: Add progress bar


    private void checkAndSend(Server server, boolean finish){
        dataManager.isOnline(server)
                .compose(RxUtil.applyDefaultSchedulers())
                .doOnNext(online -> {
                    if(online != null && online)
                        sendLink(server, finish);
                    //TODO: error message
                })
                .doOnError(e -> Timber.e(e, e.getMessage()))
                .onErrorReturn(e -> false)
                .subscribe();
    }

    private void sendLink(Server server, boolean finish){
        dataManager.sendLink(server, new LinkRequest(url))
                .compose(RxUtil.applyDefaultSchedulers())
                .doOnNext(baseResponse -> {
                    if(baseResponse != null)
                        Timber.i(baseResponse.statusMessage);
                    //TODO: error message
                    if(finish)
                        finish();
                })
                .doOnError(e -> Timber.e(e, e.getMessage()))
                .onErrorReturn(e -> new BaseResponse(-1, e.getMessage()))
                .subscribe();
    }



    /*
     * Helpers
     */
    public void setRealmAdapter(RealmResults<Server> servers) {
        RealmModelAdapter<Server> realmAdapter = new RealmModelAdapter<>(servers);
        serverAdapter.setRealmAdapter(realmAdapter);
        serverAdapter.notifyDataSetChanged();
    }

    private void setupRecycler() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        serverRV.setLayoutManager(layoutManager);
        serverRV.setAdapter(serverAdapter);
    }

    private void getData(Intent intent){
        if(intent == null){
            //TODO: show error dialog and finish
            finish();
        }
        else {
            String action = intent.getAction();
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(action) && type != null)
                if ("text/plain".equals(type))
                    url = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
    }

}
