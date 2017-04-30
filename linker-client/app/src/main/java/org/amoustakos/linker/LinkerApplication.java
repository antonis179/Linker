package org.amoustakos.linker;

import android.app.Application;
import android.content.Context;

import org.amoustakos.linker.injection.component.ApplicationComponent;
import org.amoustakos.linker.injection.component.DaggerApplicationComponent;
import org.amoustakos.linker.injection.module.ApplicationModule;


/**
 * Created by Antonis Moustakos on 3/3/2017.
 */
public class LinkerApplication extends Application {

    ApplicationComponent mApplicationComponent;


    /*
     * TODO:
     *  - Add uncaught exception handler
     *  - Add low memory handler
     */

    @Override
    public void onCreate() {
        super.onCreate();
        /*
         * Create the application component used throughout the app.
         */
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        getComponent().inject(this);

        /*
         * Init app environment.
         * logs, db, fonts depend on this.
         */
        getComponent().environment().init();
    }





    public static LinkerApplication get(Context context) {
        return (LinkerApplication) context.getApplicationContext();
    }
    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
//    public void setComponent(ApplicationComponent applicationComponent) {
//        mApplicationComponent = applicationComponent;
//    }
}