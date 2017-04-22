package org.amoustakos.linker.injection.component;

/**
 * Created by Antonis Moustakos on 3/4/2017.
 */

import android.app.Fragment;

import org.amoustakos.linker.injection.PerActivity;
import org.amoustakos.linker.injection.module.FragmentModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerActivity
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(Fragment fragment);

}
