package org.amoustakos.linker.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.amoustakos.linker.R;
import org.amoustakos.linker.io.db.RealmController;
import org.amoustakos.linker.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Antonis Moustakos on 3/16/2017.
 */
public class DashboardFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener{

    @Inject
    RealmController realmController;

    FloatingActionButton fab;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        fragmentComponent().inject(this);
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Bind views
        fab = ButterKnife.findById(getActivity(), R.id.fab);


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Add listeners
        fab.setOnClickListener(v -> addServerDlg());

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

//    @Override
//    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
//        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }


    /*
     * Helper methods
     */
    //Start server add dialog
    private void addServerDlg(){
        //TODO
    }
    //Add server
    private void addServer(){
        //TODO
    }


    /*
     * Menu
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
