package org.amoustakos.linker.injection.component;


import org.amoustakos.linker.ui.MainActivity;
import org.amoustakos.linker.injection.PerActivity;
import org.amoustakos.linker.injection.module.ActivityModule;
import org.amoustakos.linker.ui.ShareLinkActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {


    void inject(MainActivity activity);
    void inject(ShareLinkActivity activity);

}
