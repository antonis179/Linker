package org.amoustakos.linker.util.preferences;

import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public final class PreferencesUtil {
    private static WeakReference<Context> context;


    /*
     * Constructors
     */
    public static void init(@NonNull Context context) {
        PreferencesUtil.context = new WeakReference<>(context);
    }



    /*
     * Getters - Setters
     */



}
