package org.amoustakos.linker.injection.component;

import org.amoustakos.linker.injection.PerActivity;
import org.amoustakos.linker.injection.module.DialogModule;
import org.amoustakos.linker.ui.dialogs.AddServerDialog;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Dialogs across the application
 */
@PerActivity
@Subcomponent(modules = DialogModule.class)
public interface DialogComponent {

    void inject(AddServerDialog dlg);

}
