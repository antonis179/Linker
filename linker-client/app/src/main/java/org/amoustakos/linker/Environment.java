package org.amoustakos.linker;

import android.content.Context;

import org.amoustakos.linker.injection.ApplicationContext;
import org.amoustakos.linker.io.db.Migration;
import org.amoustakos.linker.util.preferences.PreferencesUtil;

import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

@Singleton
public class Environment {
    private static final int REALM_SCHEMA_VERSION = 0;
	private final WeakReference<Context> context;

    @Inject
    public Environment(@ApplicationContext Context context) {
	    this.context = new WeakReference<>(context);
    }



    /*
     * Init methods
     */
    void init(){
        initPrefs();
        initLog();
        initRealm();
        initFonts();
    }

    private void initPrefs(){
	    PreferencesUtil.init(context.get());
    }

    private void initLog(){
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }
    private void initFonts(){
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("ubuntu/Ubuntu-condensed.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
    private void initRealm(){
	    Realm.init(context.get());
	    RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .schemaVersion(REALM_SCHEMA_VERSION)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }



}
