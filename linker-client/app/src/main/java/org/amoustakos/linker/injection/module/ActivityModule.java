package org.amoustakos.linker.injection.module;

import android.app.Activity;
import android.content.Context;

import org.amoustakos.linker.injection.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }

    /*
     * Activities
     */
    @Provides
    Activity provideActivity() {
        return mActivity;
    }


    /*
     * Adapters
     */
}
