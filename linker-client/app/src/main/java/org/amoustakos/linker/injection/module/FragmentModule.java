package org.amoustakos.linker.injection.module;

import android.app.Fragment;
import android.content.Context;

import org.amoustakos.linker.injection.ActivityContext;

import dagger.Module;
import dagger.Provides;


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
