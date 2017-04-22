package org.amoustakos.linker.injection.component;


import org.amoustakos.linker.ui.MainActivity;
import org.amoustakos.linker.injection.PerActivity;
import org.amoustakos.linker.injection.module.ActivityModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

//    void inject(BaseActivity activity);

    void inject(MainActivity activity);

}
