package org.amoustakos.linker.injection.component;


import org.amoustakos.linker.injection.ConfigPersistent;
import org.amoustakos.linker.injection.module.ActivityModule;
import org.amoustakos.linker.injection.module.DialogModule;
import org.amoustakos.linker.injection.module.FragmentModule;
import org.amoustakos.linker.ui.base.BaseActivity;

import dagger.Component;

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes. Check {@link BaseActivity} to see how this components
 * survives configuration changes.
 * Use the {@link ConfigPersistent} scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {

    ActivityComponent activityComponent(ActivityModule activityModule);
    FragmentComponent fragmentComponent(FragmentModule fragmentModule);
    DialogComponent dialogComponent(DialogModule module);

}