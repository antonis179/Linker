package org.amoustakos.linker.io;

import android.content.Context;
import android.content.SharedPreferences;

import org.amoustakos.linker.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class PreferencesHelper {
    public static final String PREF_FILE_NAME = "home_preferences";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }





    /*
     * Getters - Setters
     */





    /*
     * Helpers
     */
    public void clear() {
        mPref.edit().clear().apply();
    }
}
