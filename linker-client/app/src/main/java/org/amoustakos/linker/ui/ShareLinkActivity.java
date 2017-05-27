package org.amoustakos.linker.ui;

import android.content.Intent;
import android.os.Bundle;

import org.amoustakos.linker.R;
import org.amoustakos.linker.io.DataManager;
import org.amoustakos.linker.io.db.RealmManager;
import org.amoustakos.linker.io.models.Server;
import org.amoustakos.linker.io.models.base.BaseResponse;
import org.amoustakos.linker.io.models.request.LinkRequest;
import org.amoustakos.linker.ui.base.BaseActivity;
import org.amoustakos.linker.util.RxUtil;
import org.amoustakos.linker.util.ValidationUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;

public class ShareLinkActivity extends BaseActivity {

    @Inject
    DataManager dataManager;
    @Inject
    RealmManager realmManager;

    private String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_share_link);
        ButterKnife.bind(this);


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

        //Testing
        test();
        finish();
    }

    /*
     ************ TESTING
     */
    private void test(){
        for(Server server : realmManager.getServers())
            checkAndSend(server);
    }

    /*
     ************ TESTING
     */

    //TODO: Add server fragment to activity
    //TODO: Handle server selection(s)
    //TODO: Add progress bar
    //TODO: If only 1 server just send


    private void checkAndSend(Server server){
        dataManager.isOnline(server)
                .compose(RxUtil.applyDefaultSchedulers())
                .doOnNext(online -> {
                    if(online != null && online)
                        sendLink(server);
                    //TODO: error message
                })
                .doOnError(e -> Timber.e(e, e.getMessage()))
                .onErrorReturn(e -> false)
                .subscribe();
    }

    private void sendLink(Server server){
        dataManager.sendLink(server, new LinkRequest(url))
                .compose(RxUtil.applyDefaultSchedulers())
                .doOnNext(baseResponse -> {
                    if(baseResponse != null)
                        Timber.i(baseResponse.statusMessage);
                    //TODO: error message
                })
                .doOnError(e -> Timber.e(e, e.getMessage()))
                .onErrorReturn(e -> new BaseResponse(-1, e.getMessage()))
                .subscribe();
    }



    /*
     * Helpers
     */
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
