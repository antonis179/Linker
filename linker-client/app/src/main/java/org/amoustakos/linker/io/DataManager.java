package org.amoustakos.linker.io;

import org.amoustakos.linker.io.db.RealmController;
import org.amoustakos.linker.io.remote.ApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {

    private final ApiService apiService;
    private final PreferencesHelper mPreferencesHelper;
    private final RealmController mDatabaseHelper;

    @Inject
    public DataManager(
                            ApiService apiService,
                            PreferencesHelper preferencesHelper,
                            RealmController databaseHelper
                        ) {
        this.apiService = apiService;
        this.mPreferencesHelper = preferencesHelper;
        this.mDatabaseHelper = databaseHelper;
    }



    // Example
//    public Observable<Ribot> syncRibots() {
//        return mRibotsService.getRibots()
//                .concatMap(new Func1<List<Ribot>, Observable<Ribot>>() {
//                    @Override
//                    public Observable<Ribot> call(List<Ribot> ribots) {
//                        return mDatabaseHelper.setRibots(ribots);
//                    }
//                });
//    }

}
