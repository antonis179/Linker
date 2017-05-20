package org.amoustakos.linker.injection.component;

import org.amoustakos.linker.injection.PerActivity;
import org.amoustakos.linker.injection.module.FragmentModule;
import org.amoustakos.linker.ui.DashboardFragment;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerActivity
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(DashboardFragment fragment);

}
