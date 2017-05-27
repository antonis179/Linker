package org.amoustakos.linker.injection.module;

import android.app.Application;
import android.content.Context;

import org.amoustakos.linker.Environment;
import org.amoustakos.linker.injection.ApplicationContext;
import org.amoustakos.linker.io.DataManager;
import org.amoustakos.linker.io.PreferencesHelper;
import org.amoustakos.linker.io.db.RealmManager;
import org.amoustakos.linker.io.remote.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Environment provideEnvironment() {
        return new Environment(mApplication);
    }

    @Provides
    @Singleton
    ApiService provideApiService() {
        return ApiService.Creator.newApiService();
    }

    @Provides
    @Singleton
    RealmManager provideDatabaseHelper() {
        return new RealmManager();
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper() {
        return new PreferencesHelper(mApplication);
    }

    @Provides
    @Singleton
    DataManager provideDataManager(
                                        ApiService apiService,
                                        PreferencesHelper preferencesHelper,
                                        RealmManager databaseHelper
                                    ) {
        return new DataManager(apiService, preferencesHelper, databaseHelper);
    }

}
