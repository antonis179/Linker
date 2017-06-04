package org.amoustakos.linker.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.amoustakos.linker.R;
import org.amoustakos.linker.io.db.RealmManager;
import org.amoustakos.linker.io.models.Server;
import org.amoustakos.linker.ui.adapters.ServerAdapter;
import org.amoustakos.linker.ui.adapters.abs.RealmModelAdapter;
import org.amoustakos.linker.ui.base.BaseFragment;
import org.amoustakos.linker.ui.dialogs.AddServerDialog;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.realm.RealmResults;

public class DashboardFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener{

    @Inject
    RealmManager realmManager;

    FloatingActionButton fab;

    private AddServerDialog addSrvDlg = null;

    //Servers
    private RecyclerView serverRV;
    private ServerAdapter serverAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        fragmentComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Bind views
        fab = ButterKnife.findById(getActivity(), R.id.fab);
        serverRV = ButterKnife.findById(view, R.id.rv_server);

        //Dialogs
        addSrvDlg = new AddServerDialog(getActivity());

        //Listeners
        fab.setOnClickListener(v -> showAddSrvDlg());

        //Servers
        serverAdapter = new ServerAdapter(getActivity(), realmManager);
        setupRecycler();
        realmManager.setAutoRefresh(true);
        setRealmAdapter(realmManager.getServers());

        serverAdapter.getLongClicks()
                    .doOnNext(pos -> {
                        //TODO: Add confirmation dialog
                        realmManager.remove(serverAdapter.getItem(pos));
                        serverAdapter.notifyDataSetChanged();
                    })
                    .subscribe();

        getActivity().getApplication().registerActivityLifecycleCallbacks(addSrvDlg);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Add listeners
        fab.setOnClickListener(v -> showAddSrvDlg());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(addSrvDlg != null)
            getActivity().getApplication().unregisterActivityLifecycleCallbacks(addSrvDlg);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    /*
     * Helper methods
     */
    //Start server add dialog
    private synchronized void showAddSrvDlg(){
        addSrvDlg.setCancelable(true);
        addSrvDlg.setOnDismissListener(d -> {
            //TODO: reload servers
//            setRealmAdapter(realmManager.getServers());
        });
        if(!addSrvDlg.isShowing())
            addSrvDlg.show();
    }

    public void setRealmAdapter(RealmResults<Server> servers) {
        RealmModelAdapter<Server> realmAdapter = new RealmModelAdapter<>(servers);
        serverAdapter.setRealmAdapter(realmAdapter);
        serverAdapter.notifyDataSetChanged();
    }

    private void setupRecycler() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        serverRV.setLayoutManager(layoutManager);
        serverRV.setAdapter(serverAdapter);
    }


    /*
     * Menu
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
