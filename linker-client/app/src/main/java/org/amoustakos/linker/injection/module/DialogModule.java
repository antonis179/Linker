package org.amoustakos.linker.injection.module;

import android.app.Dialog;
import android.content.Context;

import org.amoustakos.linker.injection.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class DialogModule {
    private Dialog mDialog;

    public DialogModule(Dialog dialog) {
        mDialog = dialog;
    }


    @Provides
    @ActivityContext
    Context providesContext() {
        return mDialog.getContext();
    }
}
