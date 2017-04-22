package org.amoustakos.linker.util;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxUtil {

    /*
     * Schedulers
     */
    public  static <T> Observable.Transformer<T, T> applyDefaultSchedulers() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public  static <T> Observable.Transformer<T, T> applyForegroundSchedulers() {
        return tObservable -> tObservable.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public static <T> Observable.Transformer<T, T> applyBackgroundIOSchedulers() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
    public static <T> Observable.Transformer<T, T> applyComputationSchedulers() {
        return tObservable -> tObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }



    /*
     * Error handling
     */



    /*
     * Helpers
     */
    public static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
