package org.amoustakos.linker.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import org.amoustakos.linker.LinkerApplication;
import org.amoustakos.linker.injection.component.ConfigPersistentComponent;
import org.amoustakos.linker.injection.component.DaggerConfigPersistentComponent;
import org.amoustakos.linker.injection.component.DialogComponent;
import org.amoustakos.linker.injection.module.DialogModule;


public class BaseDialog extends Dialog{

    private ConfigPersistentComponent configPersistentComponent;


    /*
     * Constructors
     */
    public BaseDialog(@NonNull Context context) {
        super(context);
        init(context);
    }
    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }
    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }



    /*
     * Helpers
     */
    private void init(@NonNull Context context){
        configPersistentComponent = DaggerConfigPersistentComponent.builder()
                .applicationComponent(LinkerApplication.get(context).getComponent())
                .build();
    }

    public DialogComponent component(){
        return configPersistentComponent.dialogComponent(new DialogModule(this));
    }



}
