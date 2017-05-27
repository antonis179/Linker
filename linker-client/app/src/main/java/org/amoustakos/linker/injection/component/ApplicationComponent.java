package org.amoustakos.linker.injection.component;

import android.app.Application;
import android.content.Context;

import org.amoustakos.linker.Environment;
import org.amoustakos.linker.LinkerApplication;
import org.amoustakos.linker.injection.ApplicationContext;
import org.amoustakos.linker.injection.module.ApplicationModule;
import org.amoustakos.linker.io.DataManager;
import org.amoustakos.linker.io.PreferencesHelper;
import org.amoustakos.linker.io.db.RealmManager;
import org.amoustakos.linker.io.remote.ApiService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

//    void inject(SyncService syncService);
    void inject(LinkerApplication app);

    @ApplicationContext
    Context context();
    Application application();
    ApiService apiService();
    PreferencesHelper preferencesHelper();
    RealmManager databaseHelper();
    DataManager dataManager();
    Environment environment();

}
