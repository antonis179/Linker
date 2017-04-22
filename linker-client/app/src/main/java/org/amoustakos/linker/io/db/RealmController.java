package org.amoustakos.linker.io.db;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

@Singleton
public class RealmController {
    private final Realm realm;

    @Inject
    public RealmController() {
        realm = Realm.getDefaultInstance();
    }

    public Realm getRealm() {
        return realm;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        realm.setAutoRefresh(autoRefresh);
    }

    /*
     * GET
     */
    public RealmResults getByModel(Class model) {
        return realm.where(model).findAll();
    }

    public RealmModel getByID(Class model, String column, String id) {
        return realm.where(model).equalTo(column, id).findFirst();
    }
    public RealmModel getByID(Class model, String column, int id) {
        return realm.where(model).equalTo(column, id).findFirst();
    }
    public RealmModel getByID(Class model, String column, double id) {
        return realm.where(model).equalTo(column, id).findFirst();
    }
    public RealmModel getByID(Class model, String column, float id) {
        return realm.where(model).equalTo(column, id).findFirst();
    }
    public boolean has(Class model) {
        return realm.where(model).count() > 0;
    }
    public Long getCount(Class model) {
        return realm.where(model).count();
    }

    /*
     * MANAGE
     */
    public RealmObject add(RealmObject object, boolean copy){
        getRealm().beginTransaction();
        RealmObject returned = null;
        if(copy)
            returned = getRealm().copyToRealm(object);
        else
            getRealm().insert(object);
        getRealm().commitTransaction();
        return returned;
    }
    public void remove(RealmObject object){
        object.deleteFromRealm();
    }
    public boolean clearAll(Class model) {
        realm.beginTransaction();
        boolean success = realm.where(model).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        return success;
    }

}
