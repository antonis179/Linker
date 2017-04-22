package org.amoustakos.linker.injection.module;

import android.app.Fragment;
import android.content.Context;

import org.amoustakos.linker.injection.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Antonis Moustakos on 3/4/2017.
 */

@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }


    @Provides
    @ActivityContext
    Context providesContext() {
        return mFragment.getActivity();
    }

}
