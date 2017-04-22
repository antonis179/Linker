package org.amoustakos.linker.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.amoustakos.linker.R;
import org.amoustakos.linker.ui.base.BaseFragment;

/**
 * Created by Antonis Moustakos on 3/16/2017.
 */
public class DashboardFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener{
    /*
     * TODO:
     */


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

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
     * Menu
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
