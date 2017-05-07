package org.amoustakos.linker.util;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {

    /*
     * Schedulers
     */
    public static <T> ObservableTransformer<T, T> applyDefaultSchedulers() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public static <T> ObservableTransformer<T, T> applyForegroundSchedulers() {
        return tObservable -> tObservable.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public static <T> ObservableTransformer<T, T> applyBackgroundIOSchedulers() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
    public static <T> ObservableTransformer<T, T> applyComputationSchedulers() {
        return tObservable -> tObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }



    /*
     * Error handling
     */



    /*
     * Helpers
     */
}
