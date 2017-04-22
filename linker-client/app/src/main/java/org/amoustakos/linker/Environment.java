package org.amoustakos.linker;

import android.content.Context;

import org.amoustakos.linker.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Antonis Moustakos on 3/11/2017.
 */
@Singleton
@ApplicationContext
public class Environment {
    private static final int REALM_SCHEMA_VERSION = 0;
    private final Context context;

    @Inject
    public Environment(Context context) {
        this.context = context;
    }



    /*
     * Init methods
     */
    public void init(){
        initLog();
        initRealm();
        initFonts();
    }

    private void initLog(){
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }
    private void initRealm(){
        Realm.init(context);
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .schemaVersion(REALM_SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()     //TODO: migration
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
    private void initFonts(){
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Ubuntu-condensed.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }


}
